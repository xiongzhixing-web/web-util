# JQuery Steps

>去掉了popover，感觉真心弹出提示真心不好用。

另外，空间的宽度过窄，暂时改成手动调节，需要使用者自定义宽度以及空间的宽度。

# 2016/05/29 更新-->增加响应式布局

布局方法：

>计算方法，每个原点之间的距离为x，共有y个原点。则：

- 进度条容器的宽度为 x*y px
- 进度条的宽度以及进度条容器宽度为x*(y-1) px
- 按钮偏移值为 x*y px


```css

/*添加相应式CSS*/
@media (min-width: 768px) {
  /*调节总的宽度*/
  .ystep-lg {
    width: 400px;
  }
  /*调节进度条宽度*/
  .ystep-lg .ystep-progress, .ystep-lg .ystep-progress-bar {
    width: 300px;
  }
  /*调节各个原点之间的跨度*/
  .ystep-lg li {
    margin-right: 0px;
  }
  /*调节按钮位置*/
  .step-button{
    left: 400px;
  }
}
@media (min-width: 992px) {
  /*调节总的宽度*/
  .ystep-lg {
    width: 600px;
  }
  /*调节进度条宽度*/
  .ystep-lg .ystep-progress, .ystep-lg .ystep-progress-bar {
    width: 450px;
  }
  /*调节各个原点之间的跨度*/
  .ystep-lg li {
    margin-right: 50px;
  }
  /*调节按钮位置*/
  .step-button{
    left: 600px;
  }
}
@media (min-width: 1200px) {
  /*调节总的宽度*/
  .ystep-lg {
    width: 800px;
  }
  /*调节进度条宽度*/
  .ystep-lg .ystep-progress, .ystep-lg .ystep-progress-bar {
    width: 600px;
  }
  /*调节各个原点之间的跨度*/
  .ystep-lg li {
    margin-right: 100px;
  }
  /*调节按钮位置*/
  .step-button{
    left: 800px;
  }
}
```
# 增加点击按钮事件，以及校验

```
$(".ystep").loadStep({
  steps:["发起","审核","募款","执行"],
  beforeChange: function(nowIndex,nextIndex){
    console.log("跳转前校验:"+nowIndex +" "+nextIndex);

    if(nowIndex === 1 && nextIndex === 2 ){
      return true;
    }else if(nowIndex === 2 && nextIndex === 3){
      return true;
    }else if(nowIndex === 3 && nextIndex === 4){
      return true;
    }
    return true;
  },
  afterChange: function(nowIndex,prevIndex){
    console.log("跳转后动作:"+nowIndex +" "+prevIndex);

    if(nowIndex <1 || nowIndex >5) return;
    
    /*for(var i = 1; i <= 5; i++){
      if(i === nowIndex){
        $("#page"+i).css("display","block");
      }else{
        $("#page"+i).css("display","none");
      }
    }*/
    
    if(nowIndex === 2 && prevIndex === 1){
      
    }else if(nowIndex === 3 && prevIndex === 2){
      
    }else if(nowIndex === 4 && prevIndex === 3){
      
    }
  }
});
```
