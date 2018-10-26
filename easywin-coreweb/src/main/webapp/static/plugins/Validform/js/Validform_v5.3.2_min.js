/*
    Validform version 5.3.2
	By sean during April 7, 2010 - March 26, 2013
	For more information, please visit http://validform.rjboy.cn
	Validform is available under the terms of the MIT license.
*/
$(document).ready(function(){
	if($(".select")!=null){
		$(".select").select();
	}
});
(function(d, f, b) {
    function h(n, m) {
        var l = (d(window).width() - n.outerWidth()) / 2, k = (d(window).height() - n.outerHeight()) / 2, k = (document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop) + (k > 0 ? k : 0);
        n.css({
            "left": l
        }).animate({
            "top": k
        }, {
            "duration": m,
            "queue": !1
        });
    }
    function c() {
        if (d("#Validform_msg").length !== 0) return !1;
        j = d('<div id="Validform_msg"><div class="Validform_title">' + e.tit + '<a class="Validform_close" href="javascript:void(0);">&chi;</a></div><div class="Validform_info"></div><div class="iframe"><iframe frameborder="0" scrolling="no" height="100%" width="100%"></iframe></div></div>').appendTo("body"), j.find("a.Validform_close").click(function() {
            return j.hide(), i = !0, g && g.focus().addClass("Validform_error"), !1;
        }).focus(function() {
            this.blur();
        }), d(window).bind("scroll resize", function() {
            !i && h(j, 400);
        });
    }
    var g = null, j = null, i = !0, e = {
        "tit": "提示信息",
        "w": {
            "*": "不能为空！",
            "*6-16": "请填写6到16位任意字符！",
            "zh2-4": "请填写2到4位中文！",
            "n": "请填写数字！",
            "zs": "请填写整数！",
            "n6-16": "请填写6到16位数字！",
            "ff": "请填写数字（最多2位小数）！",//数字可为浮点数
            "s": "不能输入特殊字符！",
            "sn": "不能输入特殊字符！",//注意是<>'"，不是所有的
            "s6-18": "请填写6到18位字符！",
            "p": "请填写邮政编码！",
            "m": "请填写手机号码！",
            "zj": "请填写座机号码！",
            "e": "邮箱地址格式不对！",
            "url": "请填写网址！",
            "f":"请填写数字或者小数！",
            "pn":"请填写正整数", 
            "pff":"请填写非负浮点数",
            "pnff":"请填写正数"
        },
        "def": "请填写正确信息！",
        "undef": "datatype未定义！",
        "reck": "两次输入的内容不一致！",
        "r": "通过信息验证！",
        "c": "正在检测信息…",
        "s": "请{填写|选择}{0|信息}！",
        "v": "所填信息没有经过验证，请稍后…",
        "p": "正在提交数据…"
    };
    d.Tipmsg = e;
    var a = function(l, n, k) {
        var n = d.extend({}, a.defaults, n);
        n.datatype && d.extend(a.util.dataType, n.datatype);
        var m = this;
        m.tipmsg = {
            "w": {}
        }, m.forms = l, m.objects = [];
        if (k === !0) return !1;
        l.each(function() {
            if (this.validform_inited == "inited") return !0;
            this.validform_inited = "inited";
            var p = this;
            p.settings = d.extend({}, n);
            var o = d(p);
            p.validform_status = "normal", o.data("tipmsg", m.tipmsg), o.delegate("[datatype]", "blur", function() {
                var q = arguments[1];
                a.util.check.call(this, o, q);
            }), o.delegate(":text", "keypress", function(q) {
                q.keyCode == 13 && o.find(":submit").length == 0 && o.submit();
            }), a.util.enhance.call(o, p.settings.tiptype, p.settings.usePlugin, p.settings.tipSweep), p.settings.btnSubmit && o.find(p.settings.btnSubmit).bind("click", function() {
                return o.trigger("submit"), !1;
            }), o.submit(function() {
                var q = a.util.submitForm.call(o, p.settings);
                q === b && (q = !0);
                if(q==true){
  				  $("select[multiple=multiple]").each(function(){
  				      var index = 0;
  				      var pp = $(this);
  				      $(this).children().each(function(i){
  				        var input = $('<input>');  
                          input.attr("name",pp.attr("list")+"["+index+"]."+pp.attr("listkey"));  
  	                    input.attr("type","hidden");  
  	                    input.attr("value",$(this).val());  
  	                    $(".subform").append(input); 
  	                    
  	                    var inputname = $('<input>');  
                          inputname.attr("name",pp.attr("list")+"["+index+"]."+pp.attr("listvalue"));  
  	                    inputname.attr("type","hidden");  
  	                    inputname.attr("value",$(this).text());  
  	                    $(".subform").append(inputname); 
  	                     
  	                    index ++;  
  				      });
  				    });
  				}			 
  				if(q){
  					var objsb=$("input[type=submit]");
  					if(objsb.length!=undefined){
  						for(var i=0;i<objsb.length;i++){
  							objsb[i].disabled=true;
  						}
  					}
  				}
                return q;
            }), o.find("[type='reset']").add(o.find(p.settings.btnReset)).bind("click", function() {
                a.util.resetForm.call(o);
            });
        }), (n.tiptype == 1 || (n.tiptype == 2 || n.tiptype == 3) && n.ajaxPost) && c();
    };
    a.defaults = {
        "tiptype": 1,
        "tipSweep": !1,
        "showAllError": !1,
        "postonce": !1,
        "ajaxPost": !1
    }, a.util = {
        "dataType": {
            "*": /[\w\W]+/,
            "*6-16": /^[\w\W]{6,16}$/,
            "zh2-4":/^[\u4E00-\u9FA5\uf900-\ufa2d]{2,4}$/,
            "n": /^\d+$/,
            "zs": /^-?(0|[1-9][0-9]*)$/,
            "n6-16": /^\d{6,16}$/,
			"ff":/^-?\d+\.?\d{0,2}$/,
            "s": /^[\u4E00-\u9FA5\uf900-\ufa2d\w\.\s]+$/,
            "sn": /^[^'|<|>|"]+$/,//注意是<>'"，不是所有的
            "s6-18": /^[\u4E00-\u9FA5\uf900-\ufa2d\w\.\s]{6,18}$/,
            "p": /^[0-9]{6}$/,
            "m": /^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$/,
            "zj": /^(((0\d{3}[\-])?\d{7}|(0\d{2}[\-])?\d{8}))([\-]\d{2,4})?$/,
            "e": /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/,
            "url": /^(\w+:\/\/)?\w+(\.\w+)+.*$/,
            "f":/^\d+\.{0,1}\d*$/,
            "pn": /^(0|[1-9][0-9]*)$/,
            "pff":/^\d+(\.\d+)?$/,
            "pnff":/^(?:[1-9][0-9]*(?:\.[0-9]+)?|0\.(?!0+$)[0-9]+)$/
        },
        "toString": Object.prototype.toString,
        "isEmpty": function(k) {
            return k === "" || k === d.trim(this.attr("tip"));
        },
        "getValue": function(m) {
            var l, k = this;
            return m.is(":radio") ? (l = k.find(":radio[name='" + m.attr("name") + "']:checked").val(), l = l === b ? "" : l) : m.is(":checkbox") ? (l = "", k.find(":checkbox[name='" + m.attr("name") + "']:checked").each(function() {
                l += d(this).val() + ",";
            }), l = l === b ? "" : l) : l = m.val(), l = d.trim(l), a.util.isEmpty.call(m, l) ? "" : l;
        },
        "enhance": function(l, m, n, k) {
            var o = this;
            o.find("[datatype]").each(function() {
            	//getParent($(this),"TD").append("<div style=\"display: none;\" class=\"Validform_checktip\"></div>");
            	/*var next = $(this).next();
            	if(next.length>0){
            		next.after("<div style=\"display: none;\" class=\"Validform_checktip\"></div>");
            	}else{
            		$(this).after("<div style=\"display: none;\" class=\"Validform_checktip\"></div>");
            	}*/
            	if($(this).parent().find(".Validform_checktip").length==0){
            		$(this).parent().append("<div style=\"display: none;\" class=\"Validform_checktip\"></div>");
            	}
                l == 2 ? d(this).parent().next().find(".Validform_checktip").length == 0 && (d(this).parent().next().append("<span class='Validform_checktip' />"), d(this).siblings(".Validform_checktip").remove()) : (l == 3 || l == 4) && d(this).siblings(".Validform_checktip").length == 0 && (d(this).parent().append("<span class='Validform_checktip' />"), d(this).parent().next().find(".Validform_checktip").remove());
            }), o.find("input[recheck]").each(function() {
                if (this.validform_inited == "inited") return !0;
                this.validform_inited = "inited";
                var q = d(this), p = o.find("input[name='" + d(this).attr("recheck") + "']");
                p.bind("keyup", function() {
                    if (p.val() == q.val() && p.val() != "") {
                        if (p.attr("tip") && p.attr("tip") == p.val()) return !1;
                        q.trigger("blur");
                    }
                }).bind("blur", function() {
                    if (p.val() != q.val() && q.val() != "") {
                        if (q.attr("tip") && q.attr("tip") == q.val()) return !1;
                        q.trigger("blur");
                    }
                });
            }), o.find("[tip]").each(function() {
                if (this.validform_inited == "inited") return !0;
                this.validform_inited = "inited";
                var q = d(this).attr("tip"), p = d(this).attr("altercss");
                d(this).focus(function() {
                    d(this).val() == q && (d(this).val(""), p && d(this).removeClass(p));
                }).blur(function() {
                    d.trim(d(this).val()) === "" && (d(this).val(q), p && d(this).addClass(p));
                });
            }), o.find(":checkbox[datatype],:radio[datatype]").each(function() {
                if (this.validform_inited == "inited") return !0;
                this.validform_inited = "inited";
                var q = d(this), p = q.attr("name");
                o.find("[name='" + p + "']").filter(":checkbox,:radio").bind("click", function() {
                    setTimeout(function() {
                        q.trigger("blur");
                    }, 0);
                });
            }), o.find("select[datatype][multiple]").bind("click", function() {
                var p = d(this);
                setTimeout(function() {
                    p.trigger("blur");
                }, 0);
            }), a.util.usePlugin.call(o, m, l, n, k);
        },
        "usePlugin": function(o, l, n, r) {
            var s = this, o = o || {};
            if (s.find("input[plugin='swfupload']").length && typeof swfuploadhandler != "undefined") {
                var k = {
                    "custom_settings": {
                        "form": s,
                        "showmsg": function(v, t, u) {
                            a.util.showmsg.call(s, v, l, {
                                "obj": s.find("input[plugin='swfupload']"),
                                "type": t,
                                "sweep": n
                            });
                        }
                    }
                };
                k = d.extend(!0, {}, o.swfupload, k), s.find("input[plugin='swfupload']").each(function(t) {
                    if (this.validform_inited == "inited") return !0;
                    this.validform_inited = "inited", d(this).val(""), swfuploadhandler.init(k, t);
                });
            }
            s.find("input[plugin='datepicker']").length && d.fn.datePicker && (o.datepicker = o.datepicker || {}, o.datepicker.format && (Date.format = o.datepicker.format, delete o.datepicker.format), o.datepicker.firstDayOfWeek && (Date.firstDayOfWeek = o.datepicker.firstDayOfWeek, delete o.datepicker.firstDayOfWeek), s.find("input[plugin='datepicker']").each(function(t) {
                if (this.validform_inited == "inited") return !0;
                this.validform_inited = "inited", o.datepicker.callback && d(this).bind("dateSelected", function() {
                    var u = (new Date(d.event._dpCache[this._dpId].getSelected()[0])).asString(Date.format);
                    o.datepicker.callback(u, this);
                }), d(this).datePicker(o.datepicker);
            })), s.find("input[plugin*='passwordStrength']").length && d.fn.passwordStrength && (o.passwordstrength = o.passwordstrength || {}, o.passwordstrength.showmsg = function(u, v, t) {
                a.util.showmsg.call(s, v, l, {
                    "obj": u,
                    "type": t,
                    "sweep": n
                });
            }, s.find("input[plugin='passwordStrength']").each(function(t) {
                if (this.validform_inited == "inited") return !0;
                this.validform_inited = "inited", d(this).passwordStrength(o.passwordstrength);
            }));
            if (r != "addRule" && o.jqtransform && d.fn.jqTransSelect) {
                if (s[0].jqTransSelected == "true") return;
                s[0].jqTransSelected = "true";
                var m = function(t) {
                    var u = d(".jqTransformSelectWrapper ul:visible");
                    u.each(function() {
                        var v = d(this).parents(".jqTransformSelectWrapper:first").find("select").get(0);
                        (!t || !v.oLabel || v.oLabel.get(0) != t.get(0)) && d(this).hide();
                    });
                }, p = function(t) {
                    d(t.target).parents(".jqTransformSelectWrapper").length === 0 && m(d(t.target));
                }, q = function() {
                    d(document).mousedown(p);
                };
                o.jqtransform.selector ? (s.find(o.jqtransform.selector).filter('input:submit, input:reset, input[type="button"]').jqTransInputButton(), s.find(o.jqtransform.selector).filter("input:text, input:password").jqTransInputText(), s.find(o.jqtransform.selector).filter("input:checkbox").jqTransCheckBox(), s.find(o.jqtransform.selector).filter("input:radio").jqTransRadio(), s.find(o.jqtransform.selector).filter("textarea").jqTransTextarea(), s.find(o.jqtransform.selector).filter("select").length > 0 && (s.find(o.jqtransform.selector).filter("select").jqTransSelect(), q())) : s.jqTransform(), s.find(".jqTransformSelectWrapper").find("li a").click(function() {
                    d(this).parents(".jqTransformSelectWrapper").find("select").trigger("blur");
                });
            }
        },
        "getNullmsg": function(o) {
            var n = this, m = /[\u4E00-\u9FA5\uf900-\ufa2da-zA-Z\s]+/g, k, l = o[0].settings.label || ".Validform_label";
            l = n.siblings(l).eq(0).text() || n.siblings().find(l).eq(0).text() || n.parent().siblings(l).eq(0).text() || n.parent().siblings().find(l).eq(0).text(), l = l.replace(/\s(?![a-zA-Z])/g, "").match(m), l = l ? l.join("") : [ "" ], m = /\{(.+)\|(.+)\}/, k = o.data("tipmsg").s || e.s;
            if (l != "") {
                k = k.replace(/\{0\|(.+)\}/, l);
                if (n.attr("recheck")) return k = k.replace(/\{(.+)\}/, ""), n.attr("nullmsg", k), k;
            } else k = n.is(":checkbox,:radio,select") ? k.replace(/\{0\|(.+)\}/, "") : k.replace(/\{0\|(.+)\}/, "$1");
            return k = n.is(":checkbox,:radio,select") ? k.replace(m, "$2") : k.replace(m, "$1"), n.attr("nullmsg", k), k;
        },
        "getErrormsg": function(s, n, u) {
            var o = /^(.+?)((\d+)-(\d+))?$/, m = /^(.+?)(\d+)-(\d+)$/, l = /(.*?)\d+(.+?)\d+(.*)/, q = n.match(o), t, r;
            if (u == "recheck") return r = s.data("tipmsg").reck || e.reck, r;
            var p = d.extend({}, e.w, s.data("tipmsg").w);
            if (q[0] in p) return s.data("tipmsg").w[q[0]] || e.w[q[0]];
            for (var k in p) if (k.indexOf(q[1]) != -1 && m.test(k)) return r = (s.data("tipmsg").w[k] || e.w[k]).replace(l, "$1" + q[3] + "$2" + q[4] + "$3"), s.data("tipmsg").w[q[0]] = r, r;
            return s.data("tipmsg").def || e.def;
        },
        "_regcheck": function(t, n, u, A) {
            var A = A, y = null, v = !1, o = /\/.+\//g, k = /^(.+?)(\d+)-(\d+)$/, l = 3;
            if (o.test(t)) {
                var s = t.match(o)[0].slice(1, -1), r = t.replace(o, ""), q = RegExp(s, r);
                v = q.test(n);
            } else if (a.util.toString.call(a.util.dataType[t]) == "[object Function]") v = a.util.dataType[t](n, u, A, a.util.dataType), v === !0 || v === b ? v = !0 : (y = v, v = !1); else {
                if (!(t in a.util.dataType)) {
                    var m = t.match(k), z;
                    if (!m) v = !1, y = A.data("tipmsg").undef || e.undef; else for (var B in a.util.dataType) {
                        z = B.match(k);
                        if (!z) continue;
                        if (m[1] === z[1]) {
                            var w = a.util.dataType[B].toString(), r = w.match(/\/[mgi]*/g)[1].replace("/", ""), x = new RegExp("\\{" + z[2] + "," + z[3] + "\\}", "g");
                            w = w.replace(/\/[mgi]*/g, "/").replace(x, "{" + m[2] + "," + m[3] + "}").replace(/^\//, "").replace(/\/$/, ""), a.util.dataType[t] = new RegExp(w, r);
                            break;
                        }
                    }
                }
                a.util.toString.call(a.util.dataType[t]) == "[object RegExp]" && (v = a.util.dataType[t].test(n));
            }
            if (v) {
                l = 2, y = u.attr("sucmsg") || A.data("tipmsg").r || e.r;
                if (u.attr("recheck")) {
                    var p = A.find("input[name='" + u.attr("recheck") + "']:first");
                    n != p.val() && (v = !1, l = 3, y = u.attr("errormsg") || a.util.getErrormsg.call(u, A, t, "recheck"));
                }
            } else y = y || u.attr("errormsg") || a.util.getErrormsg.call(u, A, t), a.util.isEmpty.call(u, n) && (y = u.attr("nullmsg") || a.util.getNullmsg.call(u, A));
            return {
                "passed": v,
                "type": l,
                "info": y
            };
        },
        "regcheck": function(n, s, m) {
            var t = this, k = null, l = !1, r = 3;
            if (m.attr("ignore") === "ignore" && a.util.isEmpty.call(m, s)) return m.data("cked") && (k = ""), {
                "passed": !0,
                "type": 4,
                "info": k
            };
            m.data("cked", "cked");
            var u = a.util.parseDatatype(n), q;
            for (var p = 0; p < u.length; p++) {
                for (var o = 0; o < u[p].length; o++) {
                    q = a.util._regcheck(u[p][o], s, m, t);
                    if (!q.passed) break;
                }
                if (q.passed) break;
            }
            return q;
        },
        "parseDatatype": function(r) {
            var q = /\/.+?\/[mgi]*(?=(,|$|\||\s))|[\w\*-]+/g, o = r.match(q), p = r.replace(q, "").replace(/\s*/g, "").split(""), l = [], k = 0;
            l[0] = [], l[0].push(o[0]);
            for (var s = 0; s < p.length; s++) p[s] == "|" && (k++, l[k] = []), l[k].push(o[s + 1]);
            return l;
        },
        "showmsg": function(n, l, m, k) {
            if (n == b) return;
            if (k == "bycheck" && m.sweep && (m.obj && !m.obj.is(".Validform_error") || typeof l == "function")) return;
            d.extend(m, {
                "curform": this
            });
            if (typeof l == "function") {
                l(n, m, a.util.cssctl);
                return;
            }
            (l == 1 || k == "byajax" && l != 4) && j.find(".Validform_info").html(n);
            if (l == 1 && k != "bycheck" && m.type != 2 || k == "byajax" && l != 4) i = !1, j.find(".iframe").css("height", j.outerHeight()), j.show(), h(j, 100);
            l == 2 && m.obj && (m.obj.parent().next().find(".Validform_checktip").html(n), a.util.cssctl(m.obj.parent().next().find(".Validform_checktip"), m.type)), (l == 3 || l == 4) && m.obj && (m.obj.siblings(".Validform_checktip").html(n), a.util.cssctl(m.obj.siblings(".Validform_checktip"), m.type));
        },
        "cssctl": function(l, k) {
            switch (k) {
              case 1:
                l.removeClass("Validform_right Validform_wrong").addClass("Validform_checktip Validform_loading");
                break;
              case 2:
                l.removeClass("Validform_wrong Validform_loading").addClass("Validform_checktip Validform_right");
                break;
              case 4:
                l.removeClass("Validform_right Validform_wrong Validform_loading").addClass("Validform_checktip");
                break;
              default:
                l.removeClass("Validform_right Validform_loading").addClass("Validform_checktip Validform_wrong");
            }
        },
        "check": function(v, t, n) {
            var o = v[0].settings, t = t || "", k = a.util.getValue.call(v, d(this));
            if (o.ignoreHidden && d(this).is(":hidden") || d(this).data("dataIgnore") === "dataIgnore") return !0;
            if (o.dragonfly && !d(this).data("cked") && a.util.isEmpty.call(d(this), k) && d(this).attr("ignore") != "ignore") return !1;
            var s = a.util.regcheck.call(v, d(this).attr("datatype"), k, d(this));
            if (k == this.validform_lastval && !d(this).attr("recheck") && t == "") return s.passed ? !0 : !1;
            this.validform_lastval = k;
            var r;
            g = r = d(this);
            if (!s.passed) return a.util.abort.call(r[0]), n || (a.util.showmsg.call(v, s.info, o.tiptype, {
                "obj": d(this),
                "type": s.type,
                "sweep": o.tipSweep
            }, "bycheck"), !o.tipSweep && r.addClass("Validform_error")), !1;
            var q = d(this).attr("ajaxurl");
            if (q && !a.util.isEmpty.call(d(this), k) && !n) {
                var m = d(this);
                t == "postform" ? m[0].validform_subpost = "postform" : m[0].validform_subpost = "";
                if (m[0].validform_valid === "posting" && k == m[0].validform_ckvalue) return "ajax";
                m[0].validform_valid = "posting", m[0].validform_ckvalue = k, a.util.showmsg.call(v, v.data("tipmsg").c || e.c, o.tiptype, {
                    "obj": m,
                    "type": 1,
                    "sweep": o.tipSweep
                }, "bycheck"), a.util.abort.call(r[0]);
                var u = d.extend(!0, {}, o.ajaxurl || {}), p = {
                    "type": "POST",
                    "cache": !1,
                    "url": q,
                    "data": "param=" + encodeURIComponent(k) + "&name=" + encodeURIComponent(d(this).attr("name")),
                    "success": function(x) {
                        d.trim(x.status) === "y" ? (m[0].validform_valid = "true", x.info && m.attr("sucmsg", x.info), a.util.showmsg.call(v, m.attr("sucmsg") || v.data("tipmsg").r || e.r, o.tiptype, {
                            "obj": m,
                            "type": 2,
                            "sweep": o.tipSweep
                        }, "bycheck"), r.removeClass("Validform_error"), g = null, m[0].validform_subpost == "postform" && v.trigger("submit")) : (m[0].validform_valid = x.info, a.util.showmsg.call(v, x.info, o.tiptype, {
                            "obj": m,
                            "type": 3,
                            "sweep": o.tipSweep
                        }), r.addClass("Validform_error")), r[0].validform_ajax = null;
                    },
                    "error": function(x) {
                        if (x.status == "200") return x.responseText == "y" ? u.success({
                            "status": "y"
                        }) : u.success({
                            "status": "n",
                            "info": x.responseText
                        }), !1;
                        if (x.statusText !== "abort") {
                            var y = "status: " + x.status + "; statusText: " + x.statusText;
                            a.util.showmsg.call(v, y, o.tiptype, {
                                "obj": m,
                                "type": 3,
                                "sweep": o.tipSweep
                            }), r.addClass("Validform_error");
                        }
                        return m[0].validform_valid = x.statusText, r[0].validform_ajax = null, !0;
                    }
                };
                if (u.success) {
                    var w = u.success;
                    u.success = function(x) {
                        p.success(x), w(x, m);
                    };
                }
                if (u.error) {
                    var l = u.error;
                    u.error = function(x) {
                        p.error(x) && l(x, m);
                    };
                }
                return u = d.extend({}, p, u, {
                    "dataType": "json"
                }), r[0].validform_ajax = d.ajax(u), "ajax";
            }
            return q && a.util.isEmpty.call(d(this), k) && (a.util.abort.call(r[0]), r[0].validform_valid = "true"), n || (a.util.showmsg.call(v, s.info, o.tiptype, {
                "obj": d(this),
                "type": s.type,
                "sweep": o.tipSweep
            }, "bycheck"), r.removeClass("Validform_error")), g = null, !0;
        },
        "submitForm": function(o, l, k, r, t) {
            var w = this;
            if (w[0].validform_status === "posting") return !1;
            if (o.postonce && w[0].validform_status === "posted") return !1;
            var v = o.beforeCheck && o.beforeCheck(w);
            if (v === !1) return !1;
            var s = !0, n;
            w.find("[datatype]").each(function() {
                if (l) return !1;
                if (o.ignoreHidden && d(this).is(":hidden") || d(this).data("dataIgnore") === "dataIgnore") return !0;
                var z = a.util.getValue.call(w, d(this)), A;
                g = A = d(this), n = a.util.regcheck.call(w, d(this).attr("datatype"), z, d(this));
                if (!n.passed) return a.util.showmsg.call(w, n.info, o.tiptype, {
                    "obj": d(this),
                    "type": n.type,
                    "sweep": o.tipSweep
                }), A.addClass("Validform_error"), o.showAllError ? (s && (s = !1), !0) : (A.focus(), s = !1, !1);
                if (d(this).attr("ajaxurl") && !a.util.isEmpty.call(d(this), z)) {
                    if (this.validform_valid !== "true") {
                        var y = d(this);
                        return a.util.showmsg.call(w, w.data("tipmsg").v || e.v, o.tiptype, {
                            "obj": y,
                            "type": 3,
                            "sweep": o.tipSweep
                        }), A.addClass("Validform_error"), y.trigger("blur", [ "postform" ]), o.showAllError ? (s && (s = !1), !0) : (s = !1, !1);
                    }
                } else d(this).attr("ajaxurl") && a.util.isEmpty.call(d(this), z) && (a.util.abort.call(this), this.validform_valid = "true");
                a.util.showmsg.call(w, n.info, o.tiptype, {
                    "obj": d(this),
                    "type": n.type,
                    "sweep": o.tipSweep
                }), A.removeClass("Validform_error"), g = null;
            }), o.showAllError && w.find(".Validform_error:first").focus();
            if (s) {
                var q = o.beforeSubmit && o.beforeSubmit(w);
                if (q === !1) return !1;
                w[0].validform_status = "posting";
                if (!o.ajaxPost && r !== "ajaxPost") {
                    o.postonce || (w[0].validform_status = "normal");
                    var k = k || o.url;
                    return k && w.attr("action", k), o.callback && o.callback(w);
                }
                var u = d.extend(!0, {}, o.ajaxpost || {});
                u.url = k || u.url || o.url || w.attr("action"), a.util.showmsg.call(w, w.data("tipmsg").p || e.p, o.tiptype, {
                    "obj": w,
                    "type": 1,
                    "sweep": o.tipSweep
                }, "byajax"), t ? u.async = !1 : t === !1 && (u.async = !0);
                if (u.success) {
                    var x = u.success;
                    u.success = function(y) {
                        o.callback && o.callback(y), w[0].validform_ajax = null, d.trim(y.status) === "y" ? w[0].validform_status = "posted" : w[0].validform_status = "normal", x(y, w);
                    };
                }
                if (u.error) {
                    var m = u.error;
                    u.error = function(y) {
                        o.callback && o.callback(y), w[0].validform_status = "normal", w[0].validform_ajax = null, m(y, w);
                    };
                }
                var p = {
                    "type": "POST",
                    "async": !0,
                    "data": w.serializeArray(),
                    "success": function(y) {
                        d.trim(y.status) === "y" ? (w[0].validform_status = "posted", a.util.showmsg.call(w, y.info, o.tiptype, {
                            "obj": w,
                            "type": 2,
                            "sweep": o.tipSweep
                        }, "byajax")) : (w[0].validform_status = "normal", a.util.showmsg.call(w, y.info, o.tiptype, {
                            "obj": w,
                            "type": 3,
                            "sweep": o.tipSweep
                        }, "byajax")), o.callback && o.callback(y), w[0].validform_ajax = null;
                    },
                    "error": function(y) {
                        var z = "status: " + y.status + "; statusText: " + y.statusText;
                        a.util.showmsg.call(w, z, o.tiptype, {
                            "obj": w,
                            "type": 3,
                            "sweep": o.tipSweep
                        }, "byajax"), o.callback && o.callback(y), w[0].validform_status = "normal", w[0].validform_ajax = null;
                    }
                };
                u = d.extend({}, p, u, {
                    "dataType": "json"
                }), w[0].validform_ajax = d.ajax(u);
            }
            return !1;
        },
        "resetForm": function() {
            var k = this;
            k.each(function() {
                this.reset && this.reset(), this.validform_status = "normal";
            }), k.find(".Validform_right").text(""), k.find(".passwordStrength").children().removeClass("bgStrength"), k.find(".Validform_checktip").removeClass("Validform_wrong Validform_right Validform_loading"), k.find(".Validform_error").removeClass("Validform_error"), k.find("[datatype]").removeData("cked").removeData("dataIgnore").each(function() {
                this.validform_lastval = null;
            }), k.eq(0).find("input:first").focus();
        },
        "abort": function() {
            this.validform_ajax && this.validform_ajax.abort();
        }
    }, d.Datatype = a.util.dataType, a.prototype = {
        "dataType": a.util.dataType,
        "eq": function(l) {
            var k = this;
            return l >= k.forms.length ? null : (l in k.objects || (k.objects[l] = new a(d(k.forms[l]).get(), {}, !0)), k.objects[l]);
        },
        "resetStatus": function() {
            var k = this;
            return d(k.forms).each(function() {
                this.validform_status = "normal";
            }), this;
        },
        "setStatus": function(k) {
            var l = this;
            return d(l.forms).each(function() {
                this.validform_status = k || "posting";
            }), this;
        },
        "getStatus": function() {
            var l = this, k = d(l.forms)[0].validform_status;
            return k;
        },
        "ignore": function(k) {
            var l = this, k = k || "[datatype]";
            return d(l.forms).find(k).each(function() {
                d(this).data("dataIgnore", "dataIgnore").removeClass("Validform_error");
            }), this;
        },
        "unignore": function(k) {
            var l = this, k = k || "[datatype]";
            return d(l.forms).find(k).each(function() {
                d(this).removeData("dataIgnore");
            }), this;
        },
        "addRule": function(n) {
            var m = this, n = n || [];
            for (var l = 0; l < n.length; l++) {
                var p = d(m.forms).find(n[l].ele);
                for (var k in n[l]) k !== "ele" && p.attr(k, n[l][k]);
            }
            return d(m.forms).each(function() {
                var o = d(this);
                a.util.enhance.call(o, this.settings.tiptype, this.settings.usePlugin, this.settings.tipSweep, "addRule");
            }), this;
        },
        "ajaxPost": function(k, m, l) {
            var n = this;
            return d(n.forms).each(function() {
                (this.settings.tiptype == 1 || this.settings.tiptype == 2 || this.settings.tiptype == 3) && c(), a.util.submitForm.call(d(n.forms[0]), this.settings, k, l, "ajaxPost", m);
            }), this;
        },
        "submitForm": function(k, l) {
            var m = this;
            return d(m.forms).each(function() {
                var n = a.util.submitForm.call(d(this), this.settings, k, l);
                n === b && (n = !0), n === !0 && this.submit();
            }), this;
        },
        "resetForm": function() {
            var k = this;
            return a.util.resetForm.call(d(k.forms)), this;
        },
        "abort": function() {
            var k = this;
            return d(k.forms).each(function() {
                a.util.abort.call(this);
            }), this;
        },
        "check": function(m, k) {
            var k = k || "[datatype]", o = this, n = d(o.forms), l = !0;
            return n.find(k).each(function() {
                a.util.check.call(this, n, "", m) || (l = !1);
            }), l;
        },
        "config": function(k) {
            var l = this;
            return k = k || {}, d(l.forms).each(function() {
                var m = d(this);
                this.settings = d.extend(!0, this.settings, k), a.util.enhance.call(m, this.settings.tiptype, this.settings.usePlugin, this.settings.tipSweep);
            }), this;
        }
    }, d.fn.Validform = function(k) {
        return new a(this, k);
    }, d.Showmsg = function(k) {
        c(), a.util.showmsg.call(f, k, 1, {});
    }, d.Hidemsg = function() {
        j.hide(), i = !0;
    };
})(jQuery, window);