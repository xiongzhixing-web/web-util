package com.tsingning.sevenmall.goods.srv.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageInfo;
import com.tsingning.sevenmall.common.core.cache.RedisCache;
import com.tsingning.sevenmall.common.core.constant.GlobalErrorCode;
import com.tsingning.sevenmall.common.core.exception.SystemException;
import com.tsingning.sevenmall.common.core.utils.CommUtils;
import com.tsingning.sevenmall.goods.constant.LocalConstant;
import com.tsingning.sevenmall.goods.constant.LocalErrorCode;
import com.tsingning.sevenmall.goods.constant.RedisKey;
import com.tsingning.sevenmall.goods.core.model.bean.*;
import com.tsingning.sevenmall.goods.core.model.utils.HttpBeanUtils;
import com.tsingning.sevenmall.goods.core.model.utils.LocalCommUtils;
import com.tsingning.sevenmall.goods.domain.GoodsCache;
import com.tsingning.sevenmall.goods.domain.GoodsStockCache;
import com.tsingning.sevenmall.goods.domain.InventoryCacheBean;
import com.tsingning.sevenmall.goods.domain.OrderInventoryBean;
import com.tsingning.sevenmall.goods.srv.service.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

import static com.tsingning.sevenmall.goods.constant.LocalConstant.*;

/**
 * 缓存统一管理
 */
@Component
public class CacheManager {
    private static Logger logger = Logger.getLogger(CacheManager.class);
    @Resource
    private IGoodsService goodsService;
    @Resource
    private IBrandService brandService;
    @Resource
    private IGoodsSkuService goodsSkuService;
    @Resource
    private ISysConfigService sysConfigService;
    @Resource
    private IGoodsCategoryService goodsCategoryService;

    private static RedisCache I() {
        return RedisCache.getInstance();
    }

    /**
     * 获取品牌映射
     */
    public static long brandNameExpire = 0L;
    public static Map<Integer, Brand> mapBrandName = new HashMap<>();

    public Map<Integer, Brand> getBrandMap() {
        if (brandNameExpire + 60 * 1000 < System.currentTimeMillis()) {
            synchronized (CacheManager.class) {
                if (brandNameExpire + 60 * 1000 < System.currentTimeMillis()) {
                    try {
                        PageInfo<Brand> pageInfo = brandService.getListByPage(new HashMap(), 1, 0);
                        if (pageInfo.getList() != null && pageInfo.getList().size() > 0) {

                            mapBrandName.clear();
                            for (Brand vo : pageInfo.getList()) {
                                mapBrandName.put(vo.getBrandId(), vo);
                            }
                        }

                        brandNameExpire = System.currentTimeMillis();
                    } catch (Exception e) {
                        logger.error("获取品牌名称异常", e);
                    }
                }
            }
        }
        return mapBrandName;
    }

    /**
     * 获取品牌名称
     */
    public String getBrandName(Integer brandId) {
        this.getBrandMap();
        if (mapBrandName.containsKey(brandId)) {
            return mapBrandName.get(brandId).getBrandName();
        }

        return "";
    }

    /**
     * 获取供应商名称
     */
    public static long supplierNameExpire = 0L;
    public static Map<Integer, String> mapSupplierName = new HashMap<>();

    public String getSupplierName(Integer supplierId) {

        if (mapSupplierName.containsKey(supplierId)) {
            return mapSupplierName.get(supplierId);
        }

        if (supplierNameExpire + 60 * 1000 < System.currentTimeMillis()) {
            List<Map> supplierList = HttpBeanUtils.getSupplier(sysConfigService.getConfig(LocalConstant.USER_ADMIN_DOMAIN), null);
            if (supplierList != null) {
                synchronized (CacheManager.class) {
                    mapSupplierName.clear();
                    for (Map map : supplierList) {
                        mapSupplierName.put((Integer) map.get("userId"), (String) map.get("userName"));
                    }

                    supplierNameExpire = System.currentTimeMillis();
                }

                if (mapSupplierName.containsKey(supplierId)) {
                    return mapSupplierName.get(supplierId);
                }
            }
        }

        return "";
    }

    /**
     * 从缓存删除商品数据
     */
    public void delGoodsFromCache(final Integer goodsId) {
        if (CommUtils.valid(goodsId)) {
            List goodsIds = new ArrayList();
            goodsIds.add(goodsId);
            delGoodsFromCache(goodsIds);
        }
    }

    public void delGoodsFromCache(final List<Integer> goodsIds) {
        if (goodsIds != null && goodsIds.size() > 0) {
            I().delHField(RedisKey.GOODS_CACHE_HASH, LocalCommUtils.listToStrArray(goodsIds));

            HttpBeanUtils.syncGoodsToOrder(sysConfigService.getConfig(ORDER_API_DOMAIN), goodsIds);
        }
    }

    /**
     * 从缓存获取商品列表
     *
     * @param goodsId
     * @return
     * @throws Exception
     */
    public GoodsCache getGoodsFromCache(Integer goodsId) throws Exception {
        List idsList = new ArrayList();
        idsList.add(goodsId);
        Map<String, GoodsCache> cacheMap = getGoodsFromCache(idsList);
        if (cacheMap.size() <= 0) {
            throw new SystemException(LocalErrorCode.GOODS_ID_ERR, "商品不存在");
        }

        return cacheMap.get(goodsId.toString());
    }

    private GoodsCache newGoodsCache(final Goods goods) {
        GoodsCache goodsCache = new GoodsCache();
        goodsCache.copyFromGoods(sysConfigService.getConfig(IMAGE_VISIT_DOMAIN), goods);
        goodsCache.setBrandName(getBrandName(goods.getBrandId()));
        goodsCache.setSupplierName(getSupplierName(goods.getSupplierId()));
        goodsCache.addGoodsSpec(goodsService.getGoodsSpecList(goods.getGoodsId()));
        return goodsCache;
    }

    public Map<String, GoodsCache> getGoodsFromCache(List<Integer> goodsIds) throws Exception {
        Map<String, GoodsCache> goodsMap = new HashMap();
        if (goodsIds == null || goodsIds.size() == 0) {
            return goodsMap;
        }

        //从缓存获取数据
        Map<String, String> cacheMap = new HashMap();
        List<String> cacheList = I().getHashMap(RedisKey.GOODS_CACHE_HASH, LocalCommUtils.listToStrArray(goodsIds));
        for (int i = 0; i < cacheList.size(); ++i) {//取
            if (cacheList.get(i) == null) {
                Goods goods = goodsService.getGoodsAllInfo(goodsIds.get(i));
                if (goods != null) {//存
                    GoodsCache goodsCache = newGoodsCache(goods);
                    goodsMap.put(goodsCache.getGoodsId().toString(), goodsCache);
                    cacheMap.put(goodsCache.getGoodsId().toString(), JSON.toJSONString(goodsCache));
                }
            } else {
                GoodsCache goodsCache = JSON.parseObject(cacheList.get(i), new TypeReference<GoodsCache>() {
                });
                goodsMap.put(goodsCache.getGoodsId().toString(), goodsCache);
            }
        }

        if (cacheMap.size() > 0) {
            I().setHashMap(RedisKey.GOODS_CACHE_HASH, cacheMap);
        }

        return goodsMap;
    }

    /**
     * 从缓存删除库存数据
     */
    public static void delInventoryFromCache(List<String> fieldsList) {
        if (fieldsList != null && fieldsList.size() > 0) {
            I().delHField(RedisKey.GOODS_CACHE_HASH, fieldsList.toArray(new String[0]));
        }
    }

    /**
     * 删除单个库存缓存
     */
    public static void delStockFromCache(Long skuId) {
        if (skuId != null) {
            I().delHField(RedisKey.GOODS_CACHE_HASH, skuId.toString());
        }
    }

    /**
     * 从缓存克除库存
     */
    public long reduceInventoryFromCache(Map<String, Integer> mapReduce) {
        if (mapReduce.size() == 0) return 0;
        return I().hIncrby(RedisKey.GOODS_CACHE_HASH, mapReduce);
    }

    /**
     * 获取库存相关配置键值
     */
    private String[] getInventoryFieldKey(final OrderInventoryBean orderBean) {
        List<String> filedKeyList = new ArrayList();
        filedKeyList.add(RedisKey.getSalableFieldKey(orderBean.getSkuId(), orderBean.getActiveId()));
        filedKeyList.add(RedisKey.getOccupyFieldKey(orderBean.getSkuId(), orderBean.getActiveId()));
        filedKeyList.add(String.valueOf(orderBean.getGoodsId()));
        return filedKeyList.toArray(new String[0]);
    }

    /**
     * 从缓存获取库存
     */
    public void getInventoryFromCache(Map<String, InventoryCacheBean> inventoryMap) throws Exception {
        if (inventoryMap == null || inventoryMap.size() == 0) {
            return;
        }

        try {
            Map cacheMap = new HashMap();
            List<String> inventoryList = I().getHashMap(RedisKey.GOODS_CACHE_HASH, InventoryCacheBean.getRedisFieldKey(inventoryMap));
            int index = 0;
            for (InventoryCacheBean inventoryCache : inventoryMap.values()) {
                if (inventoryList.get(index) != null) {
                    inventoryCache.setSalableInventory(Integer.valueOf(inventoryList.get(index)));
                    inventoryCache.setOccupyInventory(Integer.valueOf(inventoryList.get(index + 1)));
                } else {
                    GoodsSkuStock skuStock = goodsSkuService.getGoodsSkuStockBySkuId(inventoryCache.getSkuId(), inventoryCache.getActiveId());
                    if (skuStock == null) {
                        throw new SystemException(LocalErrorCode.SKU_ID_ERR, "库存不存在");
                    }

                    if (inventoryCache.getActiveId() == null) {//未参与活动
                        inventoryCache.setSalableInventory(skuStock.getSalableInventory());
                        inventoryCache.setOccupyInventory(skuStock.getOccupyInventory());
                    } else {//参与活动
                        boolean bFind = false;
                        for (GoodsStockActive stockActive : skuStock.getStockActiveList()) {
                            if (stockActive.getActiveId().equals(inventoryCache.getActiveId())) {//参与活动
                                inventoryCache.setSalableInventory(stockActive.getSalableInventory());
                                inventoryCache.setOccupyInventory(stockActive.getOccupyInventory());
                                bFind = true;
                                break;
                            }
                        }

                        if (!bFind) {
                            throw new SystemException(LocalErrorCode.ACTIVE_ID_ERR, "活动不存在");
                        }
                    }

                    //数据缓存
                    cacheMap.put(RedisKey.getSalableFieldKey(inventoryCache.getSkuId(), inventoryCache.getActiveId()), inventoryCache.getSalableInventory().toString());
                    cacheMap.put(RedisKey.getOccupyFieldKey(inventoryCache.getSkuId(), inventoryCache.getActiveId()), inventoryCache.getOccupyInventory().toString());
                }
                index += 2;
            }

            if (cacheMap.size() > 0) {
                I().setHashMap(RedisKey.GOODS_CACHE_HASH, cacheMap);
            }
        } catch (SystemException se) {
            logger.error("从缓存获取库存异常:", se);
            throw se;
        } catch (Exception e) {
            logger.error("从缓存获取库存异常:", e);
            throw new SystemException(LocalErrorCode.SKU_ID_ERR, "库存不存在");
        }
    }

    /**
     * 从缓存获取库存
     */
    public void getSkuInventoryFromCache(Map<String, OrderInventoryBean> inventoryMap) throws Exception {
        if (inventoryMap.size() == 0) {
            return;
        }

        try {
            Map cacheMap = new HashMap();
            for (OrderInventoryBean orderBean : inventoryMap.values()) {
                List<String> cacheList = I().getHashMap(RedisKey.GOODS_CACHE_HASH, getInventoryFieldKey(orderBean));
                if (cacheList.get(2) != null) {
                    orderBean.setGoodsCache(JSON.parseObject(cacheList.get(2), new TypeReference<GoodsCache>() {
                    }));
                } else {//商品缓存
                    Goods goods = goodsService.getGoodsAllInfo(orderBean.getGoodsId());
                    if (goods == null) {
                        throw new SystemException(LocalErrorCode.GOODS_ID_ERR, "商品不存在");
                    }
                    GoodsCache goodsCache = newGoodsCache(goods);
                    cacheMap.put(String.valueOf(orderBean.getGoodsId()), JSON.toJSONString(goodsCache));
                }

                if (cacheList.get(0) != null) {
                    orderBean.setSalableInventory(Integer.valueOf(cacheList.get(0)));
                    orderBean.setOccupyInventory(Integer.valueOf(cacheList.get(1)));
                } else {
                    //获取库存
                    GoodsSkuStock skuStock = goodsSkuService.getGoodsSkuStockBySkuId(orderBean.getSkuId(), orderBean.getActiveId());
                    if (skuStock == null) {
                        throw new SystemException(LocalErrorCode.SKU_ID_ERR, "库存不存在");
                    }
                    GoodsStockCache stockCache = new GoodsStockCache();
                    stockCache.copyFromSkuStock(skuStock, null);

                    if (orderBean.getActiveId() == null) {//未参与活动
                        orderBean.setSalableInventory(skuStock.getSalableInventory());
                        orderBean.setOccupyInventory(skuStock.getOccupyInventory());
                    } else {//参与活动
                        boolean bFind = false;
                        for (GoodsStockActive stockActive : skuStock.getStockActiveList()) {
                            if (stockActive.getActiveId().equals(orderBean.getActiveId())) {//参与活动
                                orderBean.setSalableInventory(stockActive.getSalableInventory());
                                orderBean.setOccupyInventory(stockActive.getOccupyInventory());
                                bFind = true;
                                break;
                            }
                        }

                        if (!bFind) {
                            throw new SystemException(LocalErrorCode.ACTIVE_ID_ERR, "活动不存在");
                        }

                        if (!stockCache.getActiveId().equals(orderBean.getActiveId())) {
                            throw new SystemException(LocalErrorCode.SKU_ACTIVE_NOT_START, "活动未开启");
                        }
                    }

                    //数据缓存
                    cacheMap.put(RedisKey.getSalableFieldKey(orderBean.getSkuId(), orderBean.getActiveId()), orderBean.getSalableInventory().toString());
                    cacheMap.put(RedisKey.getOccupyFieldKey(orderBean.getSkuId(), orderBean.getActiveId()), orderBean.getOccupyInventory().toString());
                }
            }

            if (cacheMap.size() > 0) {
                I().setHashMap(RedisKey.GOODS_CACHE_HASH, cacheMap);
            }
        } catch (Exception e) {
            logger.error("从缓存获取库存异常:", e);
            throw new SystemException(LocalErrorCode.SKU_ID_ERR, "库存不存在");
        }
    }

    /**
     * 根据分类级别生成ID
     */
    public int createGCId(int level) throws Exception {
        if (level < 0 || level > 2) {
            throw new SystemException(GlobalErrorCode.SYSTEM_ERROR, "商品分类级别错误！");
        }

        String sKey = RedisKey.GC_ID_LEVEL_FIELD + String.format("%d", level);
        long gcId = RedisCache.getInstance().hIncrby(RedisKey.AUTO_INCR_ID_HASH, sKey, 1);
        if (gcId <= 1) {
            Integer levelMaxId = goodsCategoryService.getMaxIdByLevel(level);
            if (levelMaxId != 0) {
                String sMaxId = levelMaxId.toString();
                levelMaxId = Integer.parseInt(sMaxId.substring(1));
            }
            gcId = Long.valueOf(levelMaxId) + 1;

            Map mapFields = new HashMap();
            mapFields.put(sKey, Long.toString(gcId));
            RedisCache.getInstance().setHashMap(RedisKey.AUTO_INCR_ID_HASH, mapFields);
        }

        return Long.valueOf(String.format("%d%d", level, gcId)).intValue();
    }

    /**
     * 根据分类级别商品货号
     */
    public String createMerchantCode(Integer gcId) throws Exception {

        final String strGcId = String.format("%d", gcId);
        final String sKey = RedisKey.MERCHANT_CODE_FIELD + strGcId;
        long merchantCode = RedisCache.getInstance().hIncrby(RedisKey.AUTO_INCR_ID_HASH, sKey, 1);
        if (merchantCode <= 1) {
            SysConfig sysConfig = (SysConfig) sysConfigService.getObjByKey(sKey);
            if (sysConfig == null) {
                sysConfig = new SysConfig();
                sysConfig.setConfigType(SYS_CONFIG_TYPE_ID);
                sysConfig.setConfigName("商品货号ID");
                sysConfig.setValueType(SYS_DATA_TYPE_NUMBER);
                sysConfig.setConfigKey(sKey);
                sysConfig.setConfigValue("10");
                sysConfig.setIsShow(GOODS_BOOLEAN_NO);
                sysConfig.setAddTime(new Date());
                sysConfig.setUpdateTime(sysConfig.getAddTime());
                sysConfigService.add(sysConfig);

                merchantCode = 0;
            } else {
                merchantCode = Long.valueOf(sysConfig.getConfigValue());
                sysConfig.setConfigValue(String.format("%d", merchantCode + 10));
                sysConfig.setUpdateTime(new Date());
                sysConfigService.updateLocal(sysConfig);
            }

            Map mapFields = new HashMap();
            mapFields.put(sKey, Long.toString(merchantCode));
            RedisCache.getInstance().setHashMap(RedisKey.AUTO_INCR_ID_HASH, mapFields);
        }

        if (merchantCode % 10 == 0) {
            SysConfig sysConfig = (SysConfig) sysConfigService.getObjByKey(sKey);
            sysConfig.setConfigValue(String.format("%d", merchantCode + 10));
            sysConfig.setUpdateTime(new Date());
            sysConfigService.updateLocal(sysConfig);
        }

        return String.format("%d%04d", gcId, merchantCode);
    }
}
