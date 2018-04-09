
jQuery["postJSON"] = function( url, data, callback ) {
    // shift arguments if data argument was omitted
    if ( jQuery.isFunction( data ) ) {
        callback = data;
        data = undefined;
    }

    return jQuery.ajax({
        url: url,
        type: "POST",
        contentType:"application/json; charset=utf-8",
        dataType: "json",
        data: data,
        success: callback
    });
};

jQuery["loadPanel"] = function (id, url) {
    $(id).load(url, function (result) {
        $(result).find("script").appendTo(id);
    });
}

jQuery["formDataArray"] = function (formArray, callback) {
    var dataArray = {};
    $.each(formArray, function () {
        if (dataArray[this.name]) {
            if (!dataArray[this.name].push) {
                dataArray[this.name] = [dataArray[this.name]];
            }
            dataArray[this.name].push(this.value || '');
        } else {
            dataArray[this.name] = this.value || '';
        }
    });

    if ( jQuery.isFunction( callback ) ) {
        callback(dataArray);
    }

    return dataArray;
}

jQuery["serializeJson"] = function (formArray, callback) {
    return JSON.stringify($.formDataArray(formArray, callback));
}

jQuery["addTab"] = function (label, tabSrc) {
    console.log("new tab:" + label + " " + tabSrc);

    var tabs = $("#page-wrapper", window.parent.document);
    $('.J_menuTab.active', tabs).removeClass('active');
    $('#content-main iframe', tabs).hide();
    $('#content-main', tabs).append('<iframe class="J_iframe" width="100%" height="100%" src="' + tabSrc + '" data-id="' + tabSrc + '" frameborder="0" seamless></iframe>');
    $('.page-tabs-content', tabs).append('<a href="javascript:;" class="active J_menuTab" data-id="' + tabSrc + '">' + label + ' <i class="fa fa-times-circle"></i></a>');
}

jQuery.validator.addMethod("isPhone", function(value, element) {
    var length = value.length;
    return this.optional(element) || (length == 11 && /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/.test(value));
}, "请正确填写您的手机号码!");

jQuery.validator.addMethod("notSpecialCharacter", function(value, element) {
    var length = value.length;
    return this.optional(element) || /^[^\s'"!@#$%]+$/.test(value);
}, "请正确填写,不能包含空格和特殊字符!");

function clearGcSelect(id) {
    $(id).empty();
    $(id).append('<option value="-1"></option>');
}

/**/
function initGcSelect(ids, gcId) {
    $(ids[0]).change(function () {
        var pid = $(this).children('option:selected').val();
        if(pid == "-1"){
            clearGcSelect(ids[1]);
            clearGcSelect(ids[2]);
            return;
        }
        $.get("/admin/goods_category.do", {id: pid}, function (data, state) {
            if (state != "success" || data.code != "0") {
                alert("系统错误:" + data);
                console.log(state + " " + data)
            }
            $(ids[1]).empty();
            $.each(data.data, function (n, value) {
                $(ids[1]).append("<option value='" + value.gcId + "'>" + value.gcName + "</option>");
            });
            $(ids[1]).trigger("change");
        }, "json");
    });

    $(ids[1]).change(function () {
        var pid = $(this).children('option:selected').val();
        if(pid == "-1"){
            clearGcSelect(ids[2]);
            return;
        }
        $.get("/admin/goods_category.do", {id: pid}, function (data, state) {
            if (state != "success" || data.code != "0") {
                alert("系统错误:" + data);
                console.log(state + " " + data)
            }
            $(ids[2]).empty();
            $.each(data.data, function (n, value) {
                $(ids[2]).append("<option value='" + value.gcId + "'>" + value.gcName + "</option>");
            });
        }, "json");
    });

    if(typeof(gcId) != 'undefined' && gcId != -1){
        $.get("/admin/get_level_category.do", {id: gcId}, function (data, state) {
            if (state != "success" || data.code != "0") {
                alert("系统错误:" + data.msg);
            }else{
                var resData = data.data;
                clearGcSelect(ids[0]);
                $.each(resData.gcTopList, function (n, value) {
                    var isCheck = resData.gcTopId == value.gcId ? "selected" : "";
                    $(ids[0]).append("<option value='" + value.gcId + "' " + isCheck + ">" + value.gcName + "</option>");
                });

                clearGcSelect(ids[1]);
                $.each(resData.gcChildList, function (n, value) {
                    var isCheck = resData.gcChildId == value.gcId ? "selected" : "";
                    $(ids[1]).append("<option value='" + value.gcId + "' " + isCheck + ">" + value.gcName + "</option>");
                });

                clearGcSelect(ids[2]);
                $.each(resData.gcLeafList, function (n, value) {
                    var isCheck = resData.gcLeafId == value.gcId ? "selected" : "";
                    $(ids[2]).append("<option value='" + value.gcId + "' " + isCheck + ">" + value.gcName + "</option>");
                });
            }
        });
    }
}

/***/
function initEditGoodsField(idSelector, postName, errCallBack) {
    $(idSelector).editable("/admin/goods_update_field.do", {
        type: "text", name: postName, submit: "确定", tooltip: "单击编辑", onblur: "cancel",
        submitdata: function (value, settings) {
            if(typeof($(this).attr('rel')) != 'undefined'){
                return {id: $(this).attr('rel'), _method: "post"};
            }
            return {id: $(this).parent("tr").attr('rel'), _method: "post"};
        },
        onsubmit: function (settings, original) {
            $(this).validate({
                rules: {
                    value: {required: !0, minlength: 2, maxlength: 10}
                },
                messages: {
                    value: {
                        required: '不能为空',
                        minlength: '不能少于2字符',
                        maxlength: '名字过长了吧',
                    }
                }
            });
            return $(this).valid();
        },
        callback: function(value, settings) {
            console.log(value);
            var jsData = $.parseJSON(value)
            if (jsData.code == "0") {
                $(this).html(jsData.data);
            } else {
                alert(jsData.msg);
                errCallBack();
            }
        }
    });
}

function initPageNav(id, pageInfo, goToPageFunName) {
    var outStr = '<div class="col-sm-6">';
    outStr += '<div class="dataTables_info" role="alert" aria-live="polite" aria-relevant="all">';
    outStr += '<div class="dataTables_info" role="alert" aria-live="polite" aria-relevant="all" style="float:left;">';
    outStr += '<label style="float:left;padding-top: 2px;">显示 '+pageInfo.startRow+' 到 '+pageInfo.endRow+' 项，共 '+pageInfo.total+' 项，</label>';
    outStr += '<label style="float:left;padding-top: 2px;">共 '+pageInfo.pages+' 页</label>';
    outStr += '</div></div></div>';

    outStr += '<div class="col-sm-6">';
    outStr += '<div class="dataTables_paginate paging_simple_numbers">';
    outStr += '<div style="float:right">';
    outStr += '<ul class="pagination" style="float:right;margin-top:0px;">';
    outStr += '<li class="paginate_button '+(pageInfo.isFirstPage?'disabled':'')+'" aria-controls="DataTables_Table_0" tabindex="0">';
    outStr += '<a href="'+(pageInfo.isFirstPage?'#':'javascript:'+goToPageFunName+'(1);')+'">首页</a></li>';
    outStr += '<li class="paginate_button previous '+(pageInfo.isFirstPage?'disabled':'')+'" aria-controls="DataTables_Table_0" tabindex="0">';
    outStr += '<a href="'+(pageInfo.isFirstPage?'#':('javascript:'+goToPageFunName+'('+pageInfo.prePage+');'))+'">上一页</a></li>';
    outStr += '<li class="paginate_button previous '+(pageInfo.isLastPage?'disabled':'')+'" aria-controls="DataTables_Table_0" tabindex="0">';
    outStr += '<a href="'+(pageInfo.isLastPage?'#':('javascript:'+goToPageFunName+'('+pageInfo.nextPage+');'))+'">下一页</a></li>';
    outStr += '<li class="paginate_button '+(pageInfo.isLastPage?'disabled':'')+'" aria-controls="DataTables_Table_0" tabindex="0">';
    outStr += '<a href="'+(pageInfo.isLastPage?'#':('javascript:'+goToPageFunName+'('+pageInfo.pages+');'))+'">末页</a></li>';
    outStr += '</ul></div></div></div>';

    $(id).append(outStr);
}

(function ($) {

    window.Ewin = function () {
        var html = '<div id="[Id]" class="modal fade" role="dialog" aria-labelledby="modalLabel">' +
            '<div class="modal-dialog modal-sm">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
            '<h4 class="modal-title" id="modalLabel">[Title]</h4>' +
            '</div>' +
            '<div class="modal-body">' +
            '<p>[Message]</p>' +
            '</div>' +
            '<div class="modal-footer">' +
            '<button type="button" class="btn btn-default cancel" data-dismiss="modal">[BtnCancel]</button>' +
            '<button type="button" class="btn btn-primary ok" data-dismiss="modal">[BtnOk]</button>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>';


        var dialogHtml = '<div id="[Id]" class="modal fade" role="dialog" aria-labelledby="modalLabel">' +
            '<div class="modal-dialog">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
            '<h4 class="modal-title" id="modalLabel">[Title]</h4>' +
            '</div>' +
            '<div class="modal-body">' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>';
        var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
        var generateId = function () {
            var date = new Date();
            return 'mdl' + date.valueOf();
        }
        var init = function (options) {
            options = $.extend({}, {
                title: "操作提示",
                message: "提示内容",
                btnok: "确定",
                btncl: "取消",
                width: 200,
                auto: false
            }, options || {});
            var modalId = generateId();
            var content = html.replace(reg, function (node, key) {
                return {
                    Id: modalId,
                    Title: options.title,
                    Message: options.message,
                    BtnOk: options.btnok,
                    BtnCancel: options.btncl
                }[key];
            });
            $('body').append(content);
            $('#' + modalId).modal({
                width: options.width,
                backdrop: 'static'
            });
            $('#' + modalId).on('hide.bs.modal', function (e) {
                $('body').find('#' + modalId).remove();
            });
            return modalId;
        }

        return {
            alert: function (options) {
                if (typeof options == 'string') {
                    options = {
                        message: options
                    };
                }
                var id = init(options);
                var modal = $('#' + id);
                modal.find('.ok').removeClass('btn-success').addClass('btn-primary');
                modal.find('.cancel').hide();

                return {
                    id: id,
                    on: function (callback) {
                        if (callback && callback instanceof Function) {
                            modal.find('.ok').click(function () { callback(true); });
                        }
                    },
                    hide: function (callback) {
                        if (callback && callback instanceof Function) {
                            modal.on('hide.bs.modal', function (e) {
                                callback(e);
                            });
                        }
                    }
                };
            },
            confirm: function (options) {
                var id = init(options);
                var modal = $('#' + id);
                modal.find('.ok').removeClass('btn-primary').addClass('btn-success');
                modal.find('.cancel').show();
                return {
                    id: id,
                    on: function (callback) {
                        if (callback && callback instanceof Function) {
                            modal.find('.ok').click(function () { callback(true); });
                            modal.find('.cancel').click(function () { callback(false); });
                        }
                    },
                    hide: function (callback) {
                        if (callback && callback instanceof Function) {
                            modal.on('hide.bs.modal', function (e) {
                                callback(e);
                            });
                        }
                    }
                };
            },
            dialog: function (options) {
                options = $.extend({}, {
                    title: 'title',
                    url: '',
                    width: 800,
                    height: 550,
                    onReady: function () { },
                    onShown: function (e) { }
                }, options || {});
                var modalId = generateId();

                var content = dialogHtml.replace(reg, function (node, key) {
                    return {
                        Id: modalId,
                        Title: options.title
                    }[key];
                });
                $('body').append(content);
                var target = $('#' + modalId);
                target.find('.modal-body').load(options.url);
                if (options.onReady())
                    options.onReady.call(target);
                target.modal();
                target.on('shown.bs.modal', function (e) {
                    if (options.onReady(e))
                        options.onReady.call(target, e);
                });
                target.on('hide.bs.modal', function (e) {
                    $('body').find(target).remove();
                });
            }
        }
    }();
})(jQuery);