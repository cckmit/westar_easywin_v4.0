(function(d) {
    function e(a) {
        var b = a || window.event,
        c = [].slice.call(arguments, 1),
        f = 0,
        e = 0,
        g = 0;
        a = d.event.fix(b);
        a.type = "mousewheel";
        b.wheelDelta && (f = b.wheelDelta / 120);
        b.detail && (f = -b.detail / 3);
        g = f;
        void 0 !== b.axis && b.axis === b.HORIZONTAL_AXIS && (g = 0, e = -1 * f);
        void 0 !== b.wheelDeltaY && (g = b.wheelDeltaY / 120);
        void 0 !== b.wheelDeltaX && (e = -1 * b.wheelDeltaX / 120);
        c.unshift(a, f, e, g);
        return (d.event.dispatch || d.event.handle).apply(this, c)
    }
    var c = ["DOMMouseScroll", "mousewheel"];
    if (d.event.fixHooks) for (var h = c.length; h;) d.event.fixHooks[c[--h]] = d.event.mouseHooks;
    d.event.special.mousewheel = {
        setup: function() {
            if (this.addEventListener) for (var a = c.length; a;) this.addEventListener(c[--a], e, !1);
            else this.onmousewheel = e
        },
        teardown: function() {
            if (this.removeEventListener) for (var a = c.length; a;) this.removeEventListener(c[--a], e, !1);
            else this.onmousewheel = null
        }
    };
    d.fn.extend({
        mousewheel: function(a) {
            return a ? this.bind("mousewheel", a) : this.trigger("mousewheel")
        },
        unmousewheel: function(a) {
            return this.unbind("mousewheel", a)
        }
    })
})(jQuery); (function(d) {
    "undefined" !== typeof module && module.exports ? module.exports = d: d(jQuery, window, document)
})(function(d) { (function(S) {
        var Z = "undefined" !== typeof module && module.exports,
        D = "https:" == document.location.protocol ? "https:": "http:";
        "function" === typeof define && define.amd || (Z ? require("jquery-mousewheel")(d) : d.event.special.mousewheel || d("head").append(decodeURI("%3Cscript src\x3d" + D + "//cdnjs.cloudflare.com/ajax/libs/jquery-mousewheel/3.1.13/jquery.mousewheel.min.js%3E%3C/script%3E")));
        S()
    })(function() {
        var S = {
            setTop: 0,
            setLeft: 0,
            axis: "y",
            scrollbarPosition: "inside",
            scrollInertia: 0,
            autoDraggerLength: !0,
            alwaysShowScrollbar: 0,
            snapOffset: 0,
            mouseWheel: {
                enable: !0,
                scrollAmount: 120,
                axis: "y",
                deltaFactor: "auto",
                disableOver: ["select", "option", "keygen", "datalist", "textarea"]
            },
            scrollButtons: {
                scrollType: "stepless",
                scrollAmount: "auto"
            },
            keyboard: {
                enable: !0,
                scrollType: "stepless",
                scrollAmount: "auto"
            },
            contentTouchScroll: 25,
            documentTouchScroll: !0,
            advanced: {
                autoScrollOnFocus: "input,textarea,select,button,datalist,keygen,a[tabindex],area,object,[contenteditable\x3d'true']",
                updateOnContentResize: !0,
                updateOnImageLoad: "auto",
                autoUpdateTimeout: 60
            },
            theme: "light",
            callbacks: {
                onTotalScrollOffset: 0,
                onTotalScrollBackOffset: 0,
                alwaysTriggerOffsets: !0
            }
        },
        Z = 0,
        D = {},
        L = window.attachEvent && !window.addEventListener ? 1 : 0,
        C = !1,
        M,
        p = "mCSB_dragger_onDrag mCSB_scrollTools_onDrag mCS_img_loaded mCS_disabled mCS_destroyed mCS_no_scrollbar mCS-autoHide mCS-dir-rtl mCS_no_scrollbar_y mCS_no_scrollbar_x mCS_y_hidden mCS_x_hidden mCSB_draggerContainer mCSB_buttonUp mCSB_buttonDown mCSB_buttonLeft mCSB_buttonRight".split(" "),
        B = {
            init: function(a) {
                a = d.extend(!0, {},
                S, a);
                var c = E.call(this);
                if (a.live) {
                    var b = a.liveSelector || this.selector || ".mCustomScrollbar",
                    k = d(b);
                    if ("off" === a.live) {
                        T(b);
                        return
                    }
                    D[b] = setTimeout(function() {
                        k.mCustomScrollbar(a);
                        "once" === a.live && k.length && T(b)
                    },
                    500)
                } else T(b);
                a.setWidth = a.set_width ? a.set_width: a.setWidth;
                a.setHeight = a.set_height ? a.set_height: a.setHeight;
                a.axis = a.horizontalScroll ? "x": ia(a.axis);
                a.scrollInertia = 0 < a.scrollInertia && 17 > a.scrollInertia ? 17 : a.scrollInertia;
                "object" !== typeof a.mouseWheel && 1 == a.mouseWheel && (a.mouseWheel = {
                    enable: !0,
                    scrollAmount: "auto",
                    axis: "y",
                    preventDefault: !1,
                    deltaFactor: "auto",
                    normalizeDelta: !1,
                    invert: !1
                });
                a.mouseWheel.scrollAmount = a.mouseWheelPixels ? a.mouseWheelPixels: a.mouseWheel.scrollAmount;
                a.mouseWheel.normalizeDelta = a.advanced.normalizeMouseWheelDelta ? a.advanced.normalizeMouseWheelDelta: a.mouseWheel.normalizeDelta;
                a.scrollButtons.scrollType = ja(a.scrollButtons.scrollType);
                da(a);
                return d(c).each(function() {
                    var b = d(this);
                    if (!b.data("mCS")) {
                        b.data("mCS", {
                            idx: ++Z,
                            opt: a,
                            scrollRatio: {
                                y: null,
                                x: null
                            },
                            overflowed: null,
                            contentReset: {
                                y: null,
                                x: null
                            },
                            bindEvents: !1,
                            tweenRunning: !1,
                            sequential: {},
                            langDir: b.css("direction"),
                            cbOffsets: null,
                            trigger: null,
                            poll: {
                                size: {
                                    o: 0,
                                    n: 0
                                },
                                img: {
                                    o: 0,
                                    n: 0
                                },
                                change: {
                                    o: 0,
                                    n: 0
                                }
                            }
                        });
                        var c = b.data("mCS"),
                        f = c.opt,
                        e = b.data("mcs-axis"),
                        k = b.data("mcs-scrollbar-position"),
                        h = b.data("mcs-theme");
                        e && (f.axis = e);
                        k && (f.scrollbarPosition = k);
                        h && (f.theme = h, da(f));
                        var k = d(this),
                        e = k.data("mCS"),
                        h = e.opt,
                        n = h.autoExpandScrollbar ? " " + p[1] + "_expand": "",
                        n = ["\x3cdiv id\x3d'mCSB_" + e.idx + "_scrollbar_vertical' class\x3d'mCSB_scrollTools mCSB_" + e.idx + "_scrollbar mCS-" + h.theme + " mCSB_scrollTools_vertical" + n + "'\x3e\x3cdiv class\x3d'" + p[12] + "'\x3e\x3cdiv id\x3d'mCSB_" + e.idx + "_dragger_vertical' class\x3d'mCSB_dragger' style\x3d'position:absolute;' oncontextmenu\x3d'return false;'\x3e\x3cdiv class\x3d'mCSB_dragger_bar' /\x3e\x3c/div\x3e\x3cdiv class\x3d'mCSB_draggerRail' /\x3e\x3c/div\x3e\x3c/div\x3e", "\x3cdiv id\x3d'mCSB_" + e.idx + "_scrollbar_horizontal' class\x3d'mCSB_scrollTools mCSB_" + e.idx + "_scrollbar mCS-" + h.theme + " mCSB_scrollTools_horizontal" + n + "'\x3e\x3cdiv class\x3d'" + p[12] + "'\x3e\x3cdiv id\x3d'mCSB_" + e.idx + "_dragger_horizontal' class\x3d'mCSB_dragger' style\x3d'position:absolute;' oncontextmenu\x3d'return false;'\x3e\x3cdiv class\x3d'mCSB_dragger_bar' /\x3e\x3c/div\x3e\x3cdiv class\x3d'mCSB_draggerRail' /\x3e\x3c/div\x3e\x3c/div\x3e"],
                        q = "yx" === h.axis ? "mCSB_vertical_horizontal": "x" === h.axis ? "mCSB_horizontal": "mCSB_vertical",
                        n = "yx" === h.axis ? n[0] + n[1] : "x" === h.axis ? n[1] : n[0],
                        U = "yx" === h.axis ? "\x3cdiv id\x3d'mCSB_" + e.idx + "_container_wrapper' class\x3d'mCSB_container_wrapper' /\x3e": "",
                        t = h.autoHideScrollbar ? " " + p[6] : "",
                        A = "x" !== h.axis && "rtl" === e.langDir ? " " + p[7] : "";
                        h.setWidth && k.css("width", h.setWidth);
                        h.setHeight && k.css("height", h.setHeight);
                        h.setLeft = "y" !== h.axis && "rtl" === e.langDir ? "989999px": h.setLeft;
                        k.addClass("mCustomScrollbar _mCS_" + e.idx + t + A).wrapInner("\x3cdiv id\x3d'mCSB_" + e.idx + "' class\x3d'mCustomScrollBox mCS-" + h.theme + " " + q + "'\x3e\x3cdiv id\x3d'mCSB_" + e.idx + "_container' class\x3d'mCSB_container' style\x3d'position:relative; top:" + h.setTop + "; left:" + h.setLeft + ";' dir\x3d" + e.langDir + " /\x3e\x3c/div\x3e");
                        q = d("#mCSB_" + e.idx);
                        t = d("#mCSB_" + e.idx + "_container");
                        "y" === h.axis || h.advanced.autoExpandHorizontalScroll || t.css("width", ea(t));
                        "outside" === h.scrollbarPosition ? ("static" === k.css("position") && k.css("position", "relative"), k.css("overflow", "visible"), q.addClass("mCSB_outside").after(n)) : (q.addClass("mCSB_inside").append(n), t.wrap(U));
                        h = d(this).data("mCS");
                        k = h.opt;
                        h = d(".mCSB_" + h.idx + "_scrollbar:first");
                        n = V(k.scrollButtons.tabindex) ? "tabindex\x3d'" + k.scrollButtons.tabindex + "'": "";
                        n = ["\x3ca href\x3d'#' class\x3d'" + p[13] + "' oncontextmenu\x3d'return false;' " + n + " /\x3e", "\x3ca href\x3d'#' class\x3d'" + p[14] + "' oncontextmenu\x3d'return false;' " + n + " /\x3e", "\x3ca href\x3d'#' class\x3d'" + p[15] + "' oncontextmenu\x3d'return false;' " + n + " /\x3e", "\x3ca href\x3d'#' class\x3d'" + p[16] + "' oncontextmenu\x3d'return false;' " + n + " /\x3e"];
                        n = ["x" === k.axis ? n[2] : n[0], "x" === k.axis ? n[3] : n[1], n[2], n[3]];
                        k.scrollButtons.enable && h.prepend(n[0]).append(n[1]).next(".mCSB_scrollTools").prepend(n[2]).append(n[3]);
                        e = [d("#mCSB_" + e.idx + "_dragger_vertical"), d("#mCSB_" + e.idx + "_dragger_horizontal")];
                        e[0].css("min-height", e[0].height());
                        e[1].css("min-width", e[1].width());
                        c && f.callbacks.onCreate && "function" === typeof f.callbacks.onCreate && f.callbacks.onCreate.call(this);
                        d("#mCSB_" + c.idx + "_container img:not(." + p[2] + ")").addClass(p[2]);
                        B.update.call(null, b)
                    }
                })
            },
            update: function(a, c) {
                var b = a || E.call(this);
                return d(b).each(function() {
                    var a = d(this);
                    if (a.data("mCS")) {
                        var b = a.data("mCS"),
                        g = b.opt,
                        f = d("#mCSB_" + b.idx + "_container"),
                        e = d("#mCSB_" + b.idx),
                        m = [d("#mCSB_" + b.idx + "_dragger_vertical"), d("#mCSB_" + b.idx + "_dragger_horizontal")];
                        if (f.length) {
                            b.tweenRunning && F(a);
                            c && b && g.callbacks.onBeforeUpdate && "function" === typeof g.callbacks.onBeforeUpdate && g.callbacks.onBeforeUpdate.call(this);
                            a.hasClass(p[3]) && a.removeClass(p[3]);
                            a.hasClass(p[4]) && a.removeClass(p[4]);
                            e.css("max-height", "none");
                            e.height() !== a.height() && e.css("max-height", a.height());
                            var h = d(this).data("mCS"),
                            e = h.opt,
                            h = d("#mCSB_" + h.idx + "_container");
                            if (e.advanced.autoExpandHorizontalScroll && "y" !== e.axis) {
                                h.css({
                                    width: "auto",
                                    "min-width": 0,
                                    "overflow-x": "scroll"
                                });
                                var n = Math.ceil(h[0].scrollWidth);
                                3 === e.advanced.autoExpandHorizontalScroll || 2 !== e.advanced.autoExpandHorizontalScroll && n > h.parent().width() ? h.css({
                                    width: n,
                                    "min-width": "100%",
                                    "overflow-x": "inherit"
                                }) : h.css({
                                    "overflow-x": "inherit",
                                    position: "absolute"
                                }).wrap("\x3cdiv class\x3d'mCSB_h_wrapper' style\x3d'position:relative; left:0; width:999999px;' /\x3e").css({
                                    width: Math.ceil(h[0].getBoundingClientRect().right + .4) - Math.floor(h[0].getBoundingClientRect().left),
                                    "min-width": "100%",
                                    position: "relative"
                                }).unwrap()
                            }
                            "y" === g.axis || g.advanced.autoExpandHorizontalScroll || f.css("width", ea(f));
                            var q = d(this).data("mCS"),
                            e = d("#mCSB_" + q.idx),
                            n = d("#mCSB_" + q.idx + "_container"),
                            h = null == q.overflowed ? n.height() : n.outerHeight(!1),
                            q = null == q.overflowed ? n.width() : n.outerWidth(!1),
                            U = n[0].scrollHeight,
                            n = n[0].scrollWidth;
                            U > h && (h = U);
                            n > q && (q = n);
                            e = [h > e.height(), q > e.width()];
                            b.overflowed = e;
                            fa.call(this);
                            g.autoDraggerLength && (e = d(this).data("mCS"), h = d("#mCSB_" + e.idx), n = d("#mCSB_" + e.idx + "_container"), e = [d("#mCSB_" + e.idx + "_dragger_vertical"), d("#mCSB_" + e.idx + "_dragger_horizontal")], h = [h.height() / n.outerHeight(!1), h.width() / n.outerWidth(!1)], h = [parseInt(e[0].css("min-height")), Math.round(h[0] * e[0].parent().height()), parseInt(e[1].css("min-width")), Math.round(h[1] * e[1].parent().width())], n = L && h[3] < h[2] ? h[2] : h[3], e[0].css({
                                height: L && h[1] < h[0] ? h[0] : h[1],
                                "max-height": e[0].parent().height() - 10
                            }).find(".mCSB_dragger_bar").css({
                                "line-height": h[0] + "px"
                            }), e[1].css({
                                width: n,
                                "max-width": e[1].parent().width() - 10
                            }));
                            e = d(this).data("mCS");
                            n = d("#mCSB_" + e.idx);
                            q = d("#mCSB_" + e.idx + "_container");
                            h = [d("#mCSB_" + e.idx + "_dragger_vertical"), d("#mCSB_" + e.idx + "_dragger_horizontal")];
                            n = [q.outerHeight(!1) - n.height(), q.outerWidth(!1) - n.width()];
                            h = [n[0] / (h[0].parent().height() - h[0].height()), n[1] / (h[1].parent().width() - h[1].width())];
                            e.scrollRatio = {
                                y: h[0],
                                x: h[1]
                            };
                            ka.call(this);
                            f = [Math.abs(f[0].offsetTop), Math.abs(f[0].offsetLeft)];
                            "x" !== g.axis && (b.overflowed[0] ? m[0].height() > m[0].parent().height() ? I.call(this) : (u(a, f[0].toString(), {
                                dir: "y",
                                dur: 0,
                                overwrite: "none"
                            }), b.contentReset.y = null) : (I.call(this), "y" === g.axis ? W.call(this) : "yx" === g.axis && b.overflowed[1] && u(a, f[1].toString(), {
                                dir: "x",
                                dur: 0,
                                overwrite: "none"
                            })));
                            "y" !== g.axis && (b.overflowed[1] ? m[1].width() > m[1].parent().width() ? I.call(this) : (u(a, f[1].toString(), {
                                dir: "x",
                                dur: 0,
                                overwrite: "none"
                            }), b.contentReset.x = null) : (I.call(this), "x" === g.axis ? W.call(this) : "yx" === g.axis && b.overflowed[0] && u(a, f[0].toString(), {
                                dir: "y",
                                dur: 0,
                                overwrite: "none"
                            })));
                            c && b && (2 === c && g.callbacks.onImageLoad && "function" === typeof g.callbacks.onImageLoad ? g.callbacks.onImageLoad.call(this) : 3 === c && g.callbacks.onSelectorChange && "function" === typeof g.callbacks.onSelectorChange ? g.callbacks.onSelectorChange.call(this) : g.callbacks.onUpdate && "function" === typeof g.callbacks.onUpdate && g.callbacks.onUpdate.call(this));
                            $.call(this)
                        }
                    }
                })
            },
            scrollTo: function(a, c) {
                if ("undefined" != typeof a && null != a) {
                    var b = E.call(this);
                    return d(b).each(function() {
                        var b = d(this);
                        if (b.data("mCS")) {
                            var l = b.data("mCS"),
                            g = l.opt,
                            f = d.extend(!0, {},
                            {
                                trigger: "external",
                                scrollInertia: g.scrollInertia,
                                scrollEasing: "mcsEaseInOut",
                                moveDragger: !1,
                                timeout: 60,
                                callbacks: !0,
                                onStart: !0,
                                onUpdate: !0,
                                onComplete: !0
                            },
                            c),
                            e = aa.call(this, a),
                            m = 0 < f.scrollInertia && 17 > f.scrollInertia ? 17 : f.scrollInertia;
                            e[0] = ga.call(this, e[0], "y");
                            e[1] = ga.call(this, e[1], "x");
                            f.moveDragger && (e[0] *= l.scrollRatio.y, e[1] *= l.scrollRatio.x);
                            f.dur = la() ? 0 : m;
                            setTimeout(function() {
                                null !== e[0] && "undefined" !== typeof e[0] && "x" !== g.axis && l.overflowed[0] && (f.dir = "y", f.overwrite = "all", u(b, e[0].toString(), f));
                                null !== e[1] && "undefined" !== typeof e[1] && "y" !== g.axis && l.overflowed[1] && (f.dir = "x", f.overwrite = "none", u(b, e[1].toString(), f))
                            },
                            f.timeout)
                        }
                    })
                }
            },
            stop: function() {
                var a = E.call(this);
                return d(a).each(function() {
                    var a = d(this);
                    a.data("mCS") && F(a)
                })
            },
            disable: function(a) {
                var c = E.call(this);
                return d(c).each(function() {
                    var b = d(this);
                    b.data("mCS") && (b.data("mCS"), $.call(this, "remove"), W.call(this), a && I.call(this), fa.call(this, !0), b.addClass(p[3]))
                })
            },
            destroy: function() {
                var a = E.call(this);
                return d(a).each(function() {
                    var c = d(this);
                    if (c.data("mCS")) {
                        var b = c.data("mCS"),
                        k = b.opt,
                        l = d("#mCSB_" + b.idx),
                        g = d("#mCSB_" + b.idx + "_container"),
                        f = d(".mCSB_" + b.idx + "_scrollbar");
                        k.live && T(k.liveSelector || d(a).selector);
                        $.call(this, "remove");
                        W.call(this);
                        I.call(this);
                        c.removeData("mCS");
                        G(this, "mcs");
                        f.remove();
                        g.find("img." + p[2]).removeClass(p[2]);
                        l.replaceWith(g.contents());
                        c.removeClass("mCustomScrollbar _mCS_" + b.idx + " " + p[6] + " " + p[7] + " " + p[5] + " " + p[3]).addClass(p[4])
                    }
                })
            }
        },
        E = function() {
            return "object" !== typeof d(this) || 1 > d(this).length ? ".mCustomScrollbar": this
        },
        da = function(a) {
            a.autoDraggerLength = -1 < d.inArray(a.theme, ["rounded", "rounded-dark", "rounded-dots", "rounded-dots-dark"]) ? !1 : a.autoDraggerLength;
            a.autoExpandScrollbar = -1 < d.inArray(a.theme, "rounded-dots rounded-dots-dark 3d 3d-dark 3d-thick 3d-thick-dark inset inset-dark inset-2 inset-2-dark inset-3 inset-3-dark".split(" ")) ? !1 : a.autoExpandScrollbar;
            a.scrollButtons.enable = -1 < d.inArray(a.theme, ["minimal", "minimal-dark"]) ? !1 : a.scrollButtons.enable;
            a.autoHideScrollbar = -1 < d.inArray(a.theme, ["minimal", "minimal-dark"]) ? !0 : a.autoHideScrollbar;
            a.scrollbarPosition = -1 < d.inArray(a.theme, ["minimal", "minimal-dark"]) ? "outside": a.scrollbarPosition
        },
        T = function(a) {
            D[a] && (clearTimeout(D[a]), G(D, a))
        },
        ia = function(a) {
            return "yx" === a || "xy" === a || "auto" === a ? "yx": "x" === a || "horizontal" === a ? "x": "y"
        },
        ja = function(a) {
            return "stepped" === a || "pixels" === a || "step" === a || "click" === a ? "stepped": "stepless"
        },
        ea = function(a) {
            var c = [a[0].scrollWidth, Math.max.apply(Math, a.children().map(function() {
                return d(this).outerWidth(!0)
            }).get())];
            a = a.parent().width();
            return c[0] > a ? c[0] : c[1] > a ? c[1] : "100%"
        },
        X = function(a, c, b) {
            b = b ? p[0] + "_expanded": "";
            var d = a.closest(".mCSB_scrollTools");
            "active" === c ? (a.toggleClass(p[0] + " " + b), d.toggleClass(p[1]), a[0]._draggable = a[0]._draggable ? 0 : 1) : a[0]._draggable || ("hide" === c ? (a.removeClass(p[0]), d.removeClass(p[1])) : (a.addClass(p[0]), d.addClass(p[1])))
        },
        I = function() {
            var a = d(this),
            c = a.data("mCS"),
            b = c.opt,
            k = d("#mCSB_" + c.idx),
            l = d("#mCSB_" + c.idx + "_container"),
            g = [d("#mCSB_" + c.idx + "_dragger_vertical"), d("#mCSB_" + c.idx + "_dragger_horizontal")];
            F(a);
            if ("x" !== b.axis && !c.overflowed[0] || "y" === b.axis && c.overflowed[0]) g[0].add(l).css("top", 0),
            u(a, "_resetY");
            if ("y" !== b.axis && !c.overflowed[1] || "x" === b.axis && c.overflowed[1]) b = dx = 0,
            "rtl" === c.langDir && (b = k.width() - l.outerWidth(!1), dx = Math.abs(b / c.scrollRatio.x)),
            l.css("left", b),
            g[1].css("left", dx),
            u(a, "_resetX")
        },
        ka = function() {
            var a = d(this),
            c = a.data("mCS"),
            b = c.opt;
            if (!c.bindEvents) {
                ma.call(this);
                b.contentTouchScroll && na.call(this);
                oa.call(this);
                if (b.mouseWheel.enable) {
                    var k = function() {
                        l = setTimeout(function() {
                            d.event.special.mousewheel ? (clearTimeout(l), pa.call(a[0])) : k()
                        },
                        100)
                    },
                    l;
                    k()
                }
                qa.call(this);
                ra.call(this);
                b.advanced.autoScrollOnFocus && sa.call(this);
                b.scrollButtons.enable && ta.call(this);
                b.keyboard.enable && ua.call(this);
                c.bindEvents = !0
            }
        },
        W = function() {
            var a = d(this),
            c = a.data("mCS"),
            b = c.opt,
            k = "mCS_" + c.idx,
            l = ".mCSB_" + c.idx + "_scrollbar",
            l = d("#mCSB_" + c.idx + ",#mCSB_" + c.idx + "_container,#mCSB_" + c.idx + "_container_wrapper," + l + " ." + p[12] + ",#mCSB_" + c.idx + "_dragger_vertical,#mCSB_" + c.idx + "_dragger_horizontal," + l + "\x3ea"),
            g = d("#mCSB_" + c.idx + "_container");
            b.advanced.releaseDraggableSelectors && l.add(d(b.advanced.releaseDraggableSelectors));
            b.advanced.extraDraggableSelectors && l.add(d(b.advanced.extraDraggableSelectors));
            c.bindEvents && (d(document).add(d(!N() || top.document)).unbind("." + k), l.each(function() {
                d(this).unbind("." + k)
            }), clearTimeout(a[0]._focusTimeout), G(a[0], "_focusTimeout"), clearTimeout(c.sequential.step), G(c.sequential, "step"), clearTimeout(g[0].onCompleteTimeout), G(g[0], "onCompleteTimeout"), c.bindEvents = !1)
        },
        fa = function(a) {
            var c = d(this),
            b = c.data("mCS"),
            k = b.opt,
            l = d("#mCSB_" + b.idx + "_container_wrapper"),
            l = l.length ? l: d("#mCSB_" + b.idx + "_container"),
            g = [d("#mCSB_" + b.idx + "_scrollbar_vertical"), d("#mCSB_" + b.idx + "_scrollbar_horizontal")],
            f = [g[0].find(".mCSB_dragger"), g[1].find(".mCSB_dragger")];
            "x" !== k.axis && (b.overflowed[0] && !a ? (g[0].add(f[0]).add(g[0].children("a")).css("display", "block"), l.removeClass(p[8] + " " + p[10])) : (k.alwaysShowScrollbar ? (2 !== k.alwaysShowScrollbar && f[0].css("display", "none"), l.removeClass(p[10])) : (g[0].css("display", "none"), l.addClass(p[10])), l.addClass(p[8])));
            "y" !== k.axis && (b.overflowed[1] && !a ? (g[1].add(f[1]).add(g[1].children("a")).css("display", "block"), l.removeClass(p[9] + " " + p[11])) : (k.alwaysShowScrollbar ? (2 !== k.alwaysShowScrollbar && f[1].css("display", "none"), l.removeClass(p[11])) : (g[1].css("display", "none"), l.addClass(p[11])), l.addClass(p[9])));
            b.overflowed[0] || b.overflowed[1] ? c.removeClass(p[5]) : c.addClass(p[5])
        },
        r = function(a) {
            var c = a.type,
            b = a.target.ownerDocument !== document ? [d(frameElement).offset().top, d(frameElement).offset().left] : null,
            k = N() && a.target.ownerDocument !== top.document ? [d(a.view.frameElement).offset().top, d(a.view.frameElement).offset().left] : [0, 0];
            switch (c) {
            case "pointerdown":
            case "MSPointerDown":
            case "pointermove":
            case "MSPointerMove":
            case "pointerup":
            case "MSPointerUp":
                return b ? [a.originalEvent.pageY - b[0] + k[0], a.originalEvent.pageX - b[1] + k[1], !1] : [a.originalEvent.pageY, a.originalEvent.pageX, !1];
            case "touchstart":
            case "touchmove":
            case "touchend":
                return c = a.originalEvent.touches[0] || a.originalEvent.changedTouches[0],
                b = a.originalEvent.touches.length || a.originalEvent.changedTouches.length,
                a.target.ownerDocument !== document ? [c.screenY, c.screenX, 1 < b] : [c.pageY, c.pageX, 1 < b];
            default:
                return b ? [a.pageY - b[0] + k[0], a.pageX - b[1] + k[1], !1] : [a.pageY, a.pageX, !1]
            }
        },
        ma = function() {
            function a(a) {
                var b = e.find("iframe");
                b.length && b.css("pointer-events", a ? "auto": "none")
            }
            function c(a, d, c, g) {
                e[0].idleTimer = 233 > l.scrollInertia ? 250 : 0;
                if (h.attr("id") === f[1]) {
                    var m = "x";
                    a = (h[0].offsetLeft - d + g) * k.scrollRatio.x
                } else m = "y",
                a = (h[0].offsetTop - a + c) * k.scrollRatio.y;
                u(b, a.toString(), {
                    dir: m,
                    drag: !0
                })
            }
            var b = d(this),
            k = b.data("mCS"),
            l = k.opt,
            g = "mCS_" + k.idx,
            f = ["mCSB_" + k.idx + "_dragger_vertical", "mCSB_" + k.idx + "_dragger_horizontal"],
            e = d("#mCSB_" + k.idx + "_container"),
            m = d("#" + f[0] + ",#" + f[1]),
            h,
            n,
            q,
            p = l.advanced.releaseDraggableSelectors ? m.add(d(l.advanced.releaseDraggableSelectors)) : m,
            t = l.advanced.extraDraggableSelectors ? d(!N() || top.document).add(d(l.advanced.extraDraggableSelectors)) : d(!N() || top.document);
            m.bind("mousedown." + g + " touchstart." + g + " pointerdown." + g + " MSPointerDown." + g,
            function(c) {
                c.stopImmediatePropagation();
                c.preventDefault();
                if (!c.which || 1 === c.which) {
                    C = !0;
                    L && (document.onselectstart = function() {
                        return ! 1
                    });
                    a(!1);
                    F(b);
                    h = d(this);
                    var e = h.offset(),
                    f = r(c)[0] - e.top;
                    c = r(c)[1] - e.left;
                    var g = h.height() + e.top,
                    e = h.width() + e.left;
                    f < g && 0 < f && c < e && 0 < c && (n = f, q = c);
                    X(h, "active", l.autoExpandScrollbar)
                }
            }).bind("touchmove." + g,
            function(a) {
                a.stopImmediatePropagation();
                a.preventDefault();
                var b = h.offset(),
                d = r(a)[0] - b.top;
                a = r(a)[1] - b.left;
                c(n, q, d, a)
            });
            d(document).add(t).bind("mousemove." + g + " pointermove." + g + " MSPointerMove." + g,
            function(a) {
                if (h) {
                    var b = h.offset(),
                    d = r(a)[0] - b.top;
                    a = r(a)[1] - b.left;
                    n === d && q === a || c(n, q, d, a)
                }
            }).add(p).bind("mouseup." + g + " touchend." + g + " pointerup." + g + " MSPointerUp." + g,
            function(b) {
                h && (X(h, "active", l.autoExpandScrollbar), h = null);
                C = !1;
                L && (document.onselectstart = null);
                a(!0)
            })
        },
        na = function() {
            function a(a) {
                if (!Y(a) || C || r(a)[2]) M = 0;
                else {
                    M = 1;
                    E = D = 0;
                    t = 1;
                    f.removeClass("mCS_touch_action");
                    var b = q.offset();
                    A = r(a)[0] - b.top;
                    v = r(a)[1] - b.left;
                    J = [r(a)[0], r(a)[1]]
                }
            }
            function c(a) {
                if (Y(a) && !C && !r(a)[2] && (m.documentTouchScroll || a.preventDefault(), a.stopImmediatePropagation(), (!E || D) && t)) {
                    Q = R();
                    var b = n.offset(),
                    c = r(a)[0] - b.top,
                    b = r(a)[1] - b.left;
                    y.push(c);
                    s.push(b);
                    J[2] = Math.abs(r(a)[0] - J[0]);
                    J[3] = Math.abs(r(a)[1] - J[1]);
                    if (e.overflowed[0]) var d = p[0].parent().height() - p[0].height(),
                    d = 0 < A - c && c - A > -(d * e.scrollRatio.y) && (2 * J[3] < J[2] || "yx" === m.axis);
                    if (e.overflowed[1]) var h = p[1].parent().width() - p[1].width(),
                    h = 0 < v - b && b - v > -(h * e.scrollRatio.x) && (2 * J[2] < J[3] || "yx" === m.axis);
                    d || h ? (L || a.preventDefault(), D = 1) : (E = 1, f.addClass("mCS_touch_action"));
                    L && a.preventDefault();
                    H = "yx" === m.axis ? [A - c, v - b] : "x" === m.axis ? [null, v - b] : [A - c, null];
                    q[0].idleTimer = 250;
                    e.overflowed[0] && g(H[0], 0, "mcsLinearOut", "y", "all", !0);
                    e.overflowed[1] && g(H[1], 0, "mcsLinearOut", "x", G, !0)
                }
            }
            function b(a) {
                if (!Y(a) || C || r(a)[2]) M = 0;
                else {
                    M = 1;
                    a.stopImmediatePropagation();
                    F(f);
                    z = R();
                    var b = n.offset();
                    w = r(a)[0] - b.top;
                    x = r(a)[1] - b.left;
                    y = [];
                    s = []
                }
            }
            function k(a) {
                if (Y(a) && !C && !r(a)[2]) {
                    t = 0;
                    a.stopImmediatePropagation();
                    E = D = 0;
                    ba = R();
                    var b = n.offset(),
                    c = r(a)[0] - b.top,
                    b = r(a)[1] - b.left;
                    if (! (30 < ba - Q)) {
                        K = 1E3 / (ba - z);
                        var d = (a = 2.5 > K) ? [y[y.length - 2], s[s.length - 2]] : [0, 0];
                        O = a ? [c - d[0], b - d[1]] : [c - w, b - x];
                        c = [Math.abs(O[0]), Math.abs(O[1])];
                        K = a ? [Math.abs(O[0] / 4), Math.abs(O[1] / 4)] : [K, K];
                        a = [Math.abs(q[0].offsetTop) - O[0] * l(c[0] / K[0], K[0]), Math.abs(q[0].offsetLeft) - O[1] * l(c[1] / K[1], K[1])];
                        H = "yx" === m.axis ? [a[0], a[1]] : "x" === m.axis ? [null, a[1]] : [a[0], null];
                        B = [4 * c[0] + m.scrollInertia, 4 * c[1] + m.scrollInertia];
                        a = parseInt(m.contentTouchScroll) || 0;
                        H[0] = c[0] > a ? H[0] : 0;
                        H[1] = c[1] > a ? H[1] : 0;
                        e.overflowed[0] && g(H[0], B[0], "mcsEaseOut", "y", G, !1);
                        e.overflowed[1] && g(H[1], B[1], "mcsEaseOut", "x", G, !1)
                    }
                }
            }
            function l(a, b) {
                var c = [1.5 * b, 2 * b, b / 1.5, b / 2];
                return 90 < a ? 4 < b ? c[0] : c[3] : 60 < a ? 3 < b ? c[3] : c[2] : 30 < a ? 8 < b ? c[1] : 6 < b ? c[0] : 4 < b ? b: c[2] : 8 < b ? b: c[3]
            }
            function g(a, b, c, d, h, e) {
                a && u(f, a.toString(), {
                    dur: b,
                    scrollEasing: c,
                    dir: d,
                    overwrite: h,
                    drag: e
                })
            }
            var f = d(this),
            e = f.data("mCS"),
            m = e.opt,
            h = "mCS_" + e.idx,
            n = d("#mCSB_" + e.idx),
            q = d("#mCSB_" + e.idx + "_container"),
            p = [d("#mCSB_" + e.idx + "_dragger_vertical"), d("#mCSB_" + e.idx + "_dragger_horizontal")],
            t,
            A,
            v,
            w,
            x,
            y = [],
            s = [],
            z,
            Q,
            ba,
            O,
            K,
            H,
            B,
            G = "yx" === m.axis ? "none": "all",
            J = [],
            D,
            E,
            I = q.find("iframe"),
            P = ["touchstart." + h + " pointerdown." + h + " MSPointerDown." + h, "touchmove." + h + " pointermove." + h + " MSPointerMove." + h, "touchend." + h + " pointerup." + h + " MSPointerUp." + h],
            L = void 0 !== document.body.style.touchAction;
            q.bind(P[0],
            function(b) {
                a(b)
            }).bind(P[1],
            function(a) {
                c(a)
            });
            n.bind(P[0],
            function(a) {
                b(a)
            }).bind(P[2],
            function(a) {
                k(a)
            });
            I.length && I.each(function() {
                d(this).load(function() {
                    N(this) && d(this.contentDocument || this.contentWindow.document).bind(P[0],
                    function(c) {
                        a(c);
                        b(c)
                    }).bind(P[1],
                    function(a) {
                        c(a)
                    }).bind(P[2],
                    function(a) {
                        k(a)
                    })
                })
            })
        },
        oa = function() {
            function a(a, b, d) {
                l.type = d && m ? "stepped": "stepless";
                l.scrollAmount = 10;
                ca(c, a, b, "mcsLinearOut", d ? 60 : null)
            }
            var c = d(this),
            b = c.data("mCS"),
            k = b.opt,
            l = b.sequential,
            g = "mCS_" + b.idx,
            f = d("#mCSB_" + b.idx + "_container"),
            e = f.parent(),
            m;
            f.bind("mousedown." + g,
            function(a) {
                M || m || (m = 1, C = !0)
            }).add(document).bind("mousemove." + g,
            function(c) {
                if (!M && m && (window.getSelection ? window.getSelection().toString() : document.selection && "Control" != document.selection.type && document.selection.createRange().text)) {
                    var d = f.offset(),
                    g = r(c)[0] - d.top + f[0].offsetTop;
                    c = r(c)[1] - d.left + f[0].offsetLeft;
                    0 < g && g < e.height() && 0 < c && c < e.width() ? l.step && a("off", null, "stepped") : ("x" !== k.axis && b.overflowed[0] && (0 > g ? a("on", 38) : g > e.height() && a("on", 40)), "y" !== k.axis && b.overflowed[1] && (0 > c ? a("on", 37) : c > e.width() && a("on", 39)))
                }
            }).bind("mouseup." + g + " dragend." + g,
            function(b) {
                M || (m && (m = 0, a("off", null)), C = !1)
            })
        },
        pa = function() {
            function a(a, e) {
                F(c);
                if (! (0 < d(a.target).closest(".j_no_scrollbar").length)) {
                    var l = c,
                    q = a.target,
                    p = q.nodeName.toLowerCase(),
                    l = l.data("mCS").opt.mouseWheel.disableOver,
                    t = ["select", "textarea"];
                    if (! ( - 1 < d.inArray(p, l)) || -1 < d.inArray(p, t) && !d(q).is(":focus")) {
                        l = "auto" !== k.mouseWheel.deltaFactor ? parseInt(k.mouseWheel.deltaFactor) : L && 100 > a.deltaFactor ? 100 : a.deltaFactor || 100;
                        q = k.scrollInertia;
                        if ("x" === k.axis || "x" === k.mouseWheel.axis) var p = "x",
                        l = [Math.round(l * b.scrollRatio.x), parseInt(k.mouseWheel.scrollAmount)],
                        l = "auto" !== k.mouseWheel.scrollAmount ? l[1] : l[0] >= g.width() ? .9 * g.width() : l[0],
                        t = Math.abs(d("#mCSB_" + b.idx + "_container")[0].offsetLeft),
                        A = f[1][0].offsetLeft,
                        v = f[1].parent().width() - f[1].width(),
                        w = a.deltaX || a.deltaY || e;
                        else p = "y",
                        l = [Math.round(l * b.scrollRatio.y), parseInt(k.mouseWheel.scrollAmount)],
                        l = "auto" !== k.mouseWheel.scrollAmount ? l[1] : l[0] >= g.height() ? .9 * g.height() : l[0],
                        t = Math.abs(d("#mCSB_" + b.idx + "_container")[0].offsetTop),
                        A = f[0][0].offsetTop,
                        v = f[0].parent().height() - f[0].height(),
                        w = a.deltaY || e;
                        if (("y" !== p || b.overflowed[0]) && ("x" !== p || b.overflowed[1])) {
                            if (k.mouseWheel.invert || a.webkitDirectionInvertedFromDevice) w = -w;
                            k.mouseWheel.normalizeDelta && (w = 0 > w ? -1 : 1);
                            if (0 < w && 0 !== A || 0 > w && A !== v || k.mouseWheel.preventDefault) a.stopImmediatePropagation(),
                            a.preventDefault();
                            2 > a.deltaFactor && !k.mouseWheel.normalizeDelta && (l = a.deltaFactor, q = 17);
                            u(c, (t - w * l).toString(), {
                                dir: p,
                                dur: q
                            })
                        }
                    }
                }
            }
            if (d(this).data("mCS")) {
                var c = d(this),
                b = c.data("mCS"),
                k = b.opt,
                l = "mCS_" + b.idx,
                g = d("#mCSB_" + b.idx),
                f = [d("#mCSB_" + b.idx + "_dragger_vertical"), d("#mCSB_" + b.idx + "_dragger_horizontal")],
                e = d("#mCSB_" + b.idx + "_container").find("iframe");
                e.length && e.each(function() {
                    d(this).load(function() {
                        N(this) && d(this.contentDocument || this.contentWindow.document).bind("mousewheel." + l,
                        function(b, c) {
                            a(b, c)
                        })
                    })
                });
                g.bind("mousewheel." + l,
                function(b, c) {
                    a(b, c)
                })
            }
        },
        N = function(a) {
            var c = null;
            if (a) try {
                d = a.contentDocument || a.contentWindow.document,
                c = d.body.innerHTML
            } catch(b) {} else try {
                var d = top.document,
                c = d.body.innerHTML
            } catch(l) {}
            return null !== c
        },
        qa = function() {
            var a = d(this),
            c = a.data("mCS"),
            b = "mCS_" + c.idx,
            k = d("#mCSB_" + c.idx + "_container"),
            l = k.parent(),
            g;
            d(".mCSB_" + c.idx + "_scrollbar ." + p[12]).bind("mousedown." + b + " touchstart." + b + " pointerdown." + b + " MSPointerDown." + b,
            function(a) {
                C = !0;
                d(a.target).hasClass("mCSB_dragger") || (g = 1)
            }).bind("touchend." + b + " pointerup." + b + " MSPointerUp." + b,
            function(a) {
                C = !1
            }).bind("click." + b,
            function(b) {
                if (g && (g = 0, d(b.target).hasClass(p[12]) || d(b.target).hasClass("mCSB_draggerRail"))) {
                    F(a);
                    var e = d(this),
                    m = e.find(".mCSB_dragger");
                    if (0 < e.parent(".mCSB_scrollTools_horizontal").length) {
                        if (!c.overflowed[1]) return;
                        e = "x";
                        b = b.pageX > m.offset().left ? -1 : 1;
                        b = Math.abs(k[0].offsetLeft) - .9 * b * l.width()
                    } else {
                        if (!c.overflowed[0]) return;
                        e = "y";
                        b = b.pageY > m.offset().top ? -1 : 1;
                        b = Math.abs(k[0].offsetTop) - .9 * b * l.height()
                    }
                    u(a, b.toString(), {
                        dir: e,
                        scrollEasing: "mcsEaseInOut"
                    })
                }
            })
        },
        sa = function() {
            var a = d(this),
            c = a.data("mCS"),
            b = c.opt,
            k = "mCS_" + c.idx,
            l = d("#mCSB_" + c.idx + "_container"),
            g = l.parent();
            l.bind("focusin." + k,
            function(c) {
                var e = d(document.activeElement);
                c = l.find(".mCustomScrollBox").length;
                e.is(b.advanced.autoScrollOnFocus) && (F(a), clearTimeout(a[0]._focusTimeout), a[0]._focusTimer = c ? 17 * c: 0, a[0]._focusTimeout = setTimeout(function() {
                    var c = [z(e)[0], z(e)[1]],
                    d = [l[0].offsetTop, l[0].offsetLeft],
                    d = [0 <= d[0] + c[0] && d[0] + c[0] < g.height() - e.outerHeight(!1), 0 <= d[1] + c[1] && d[0] + c[1] < g.width() - e.outerWidth(!1)],
                    f = "yx" !== b.axis || d[0] || d[1] ? "all": "none";
                    "x" === b.axis || d[0] || u(a, c[0].toString(), {
                        dir: "y",
                        scrollEasing: "mcsEaseInOut",
                        overwrite: f,
                        dur: 0
                    });
                    "y" === b.axis || d[1] || u(a, c[1].toString(), {
                        dir: "x",
                        scrollEasing: "mcsEaseInOut",
                        overwrite: f,
                        dur: 0
                    })
                },
                a[0]._focusTimer))
            })
        },
        ra = function() {
            var a = d(this).data("mCS"),
            c = "mCS_" + a.idx,
            b = d("#mCSB_" + a.idx + "_container").parent();
            b.bind("scroll." + c,
            function(c) {
                0 === b.scrollTop() && 0 === b.scrollLeft() || d(".mCSB_" + a.idx + "_scrollbar").css("visibility", "hidden")
            })
        },
        ta = function() {
            var a = d(this),
            c = a.data("mCS"),
            b = c.opt,
            k = c.sequential,
            l = "mCS_" + c.idx;
            d(".mCSB_" + c.idx + "_scrollbar\x3ea").bind("mousedown." + l + " touchstart." + l + " pointerdown." + l + " MSPointerDown." + l + " mouseup." + l + " touchend." + l + " pointerup." + l + " MSPointerUp." + l + " mouseout." + l + " pointerout." + l + " MSPointerOut." + l + " click." + l,
            function(g) {
                function f(c, d) {
                    k.scrollAmount = b.scrollButtons.scrollAmount;
                    ca(a, c, d)
                }
                g.preventDefault();
                if (!g.which || 1 === g.which) {
                    var e = d(this).attr("class");
                    k.type = b.scrollButtons.scrollType;
                    switch (g.type) {
                    case "mousedown":
                    case "touchstart":
                    case "pointerdown":
                    case "MSPointerDown":
                        if ("stepped" === k.type) break;
                        C = !0;
                        c.tweenRunning = !1;
                        f("on", e);
                        break;
                    case "mouseup":
                    case "touchend":
                    case "pointerup":
                    case "MSPointerUp":
                    case "mouseout":
                    case "pointerout":
                    case "MSPointerOut":
                        if ("stepped" === k.type) break;
                        C = !1;
                        k.dir && f("off", e);
                        break;
                    case "click":
                        if ("stepped" !== k.type || c.tweenRunning) break;
                        f("on", e)
                    }
                }
            })
        },
        ua = function() {
            function a(a) {
                function g(a, d) {
                    l.type = k.keyboard.scrollType;
                    l.scrollAmount = k.keyboard.scrollAmount;
                    "stepped" === l.type && b.tweenRunning || ca(c, a, d)
                }
                switch (a.type) {
                case "blur":
                    b.tweenRunning && l.dir && g("off", null);
                    break;
                case "keydown":
                case "keyup":
                    var f = a.keyCode ? a.keyCode: a.which,
                    h = "on";
                    if ("x" !== k.axis && (38 === f || 40 === f) || "y" !== k.axis && (37 === f || 39 === f)) {
                        if ((38 === f || 40 === f) && !b.overflowed[0] || (37 === f || 39 === f) && !b.overflowed[1]) break;
                        "keyup" === a.type && (h = "off");
                        d(document.activeElement).is("input,textarea,select,datalist,keygen,[contenteditable\x3d'true']") || (a.preventDefault(), a.stopImmediatePropagation(), g(h, f))
                    } else if (33 === f || 34 === f) {
                        if (b.overflowed[0] || b.overflowed[1]) a.preventDefault(),
                        a.stopImmediatePropagation();
                        "keyup" === a.type && (F(c), f = 34 === f ? -1 : 1, "x" === k.axis || "yx" === k.axis && b.overflowed[1] && !b.overflowed[0] ? (a = "x", f = Math.abs(e[0].offsetLeft) - .9 * f * m.width()) : (a = "y", f = Math.abs(e[0].offsetTop) - .9 * f * m.height()), u(c, f.toString(), {
                            dir: a,
                            scrollEasing: "mcsEaseInOut"
                        }))
                    } else if ((35 === f || 36 === f) && !d(document.activeElement).is("input,textarea,select,datalist,keygen,[contenteditable\x3d'true']")) {
                        if (b.overflowed[0] || b.overflowed[1]) a.preventDefault(),
                        a.stopImmediatePropagation();
                        "keyup" === a.type && ("x" === k.axis || "yx" === k.axis && b.overflowed[1] && !b.overflowed[0] ? (a = "x", f = 35 === f ? Math.abs(m.width() - e.outerWidth(!1)) : 0) : (a = "y", f = 35 === f ? Math.abs(m.height() - e.outerHeight(!1)) : 0), u(c, f.toString(), {
                            dir: a,
                            scrollEasing: "mcsEaseInOut"
                        }))
                    }
                }
            }
            var c = d(this),
            b = c.data("mCS"),
            k = b.opt,
            l = b.sequential,
            g = "mCS_" + b.idx,
            f = d("#mCSB_" + b.idx),
            e = d("#mCSB_" + b.idx + "_container"),
            m = e.parent(),
            h = e.find("iframe"),
            n = ["blur." + g + " keydown." + g + " keyup." + g];
            h.length && h.each(function() {
                d(this).load(function() {
                    N(this) && d(this.contentDocument || this.contentWindow.document).bind(n[0],
                    function(b) {
                        a(b)
                    })
                })
            });
            f.attr("tabindex", "0").bind(n[0],
            function(b) {
                a(b)
            })
        },
        ca = function(a, c, b, k, l) {
            function g(b) {
                e.snapAmount && (m.scrollAmount = e.snapAmount instanceof Array ? "x" === m.dir[0] ? e.snapAmount[1] : e.snapAmount[0] : e.snapAmount);
                var c = "stepped" !== m.type,
                d = l ? l: b ? c ? q / 1.5 : r: 1E3 / 60,
                n = b ? c ? 7.5 : 40 : 2.5,
                p = [Math.abs(h[0].offsetTop), Math.abs(h[0].offsetLeft)],
                y = [10 < f.scrollRatio.y ? 10 : f.scrollRatio.y, 10 < f.scrollRatio.x ? 10 : f.scrollRatio.x],
                n = "x" === m.dir[0] ? p[1] + m.dir[1] * y[1] * n: p[0] + m.dir[1] * y[0] * n,
                y = "x" === m.dir[0] ? p[1] + m.dir[1] * parseInt(m.scrollAmount) : p[0] + m.dir[1] * parseInt(m.scrollAmount),
                n = "auto" !== m.scrollAmount ? y: n,
                c = k ? k: b ? c ? "mcsLinearOut": "mcsEaseInOut": "mcsLinear";
                b && 17 > d && (n = "x" === m.dir[0] ? p[1] : p[0]);
                u(a, n.toString(), {
                    dir: m.dir[0],
                    scrollEasing: c,
                    dur: d,
                    onComplete: b ? !0 : !1
                });
                b ? m.dir = !1 : (clearTimeout(m.step), m.step = setTimeout(function() {
                    g()
                },
                d))
            }
            var f = a.data("mCS"),
            e = f.opt,
            m = f.sequential,
            h = d("#mCSB_" + f.idx + "_container"),
            n = "stepped" === m.type ? !0 : !1,
            q = 26 > e.scrollInertia ? 26 : e.scrollInertia,
            r = 1 > e.scrollInertia ? 17 : e.scrollInertia;
            switch (c) {
            case "on":
                m.dir = [b === p[16] || b === p[15] || 39 === b || 37 === b ? "x": "y", b === p[13] || b === p[15] || 38 === b || 37 === b ? -1 : 1];
                F(a);
                if (V(b) && "stepped" === m.type) break;
                g(n);
                break;
            case "off":
                clearTimeout(m.step),
                G(m, "step"),
                F(a),
                (n || f.tweenRunning && m.dir) && g(!0)
            }
        },
        aa = function(a) {
            var c = d(this).data("mCS").opt,
            b = [];
            "function" === typeof a && (a = a());
            a instanceof Array ? b = 1 < a.length ? [a[0], a[1]] : "x" === c.axis ? [null, a[0]] : [a[0], null] : (b[0] = a.y ? a.y: a.x || "x" === c.axis ? null: a, b[1] = a.x ? a.x: a.y || "y" === c.axis ? null: a);
            "function" === typeof b[0] && (b[0] = b[0]());
            "function" === typeof b[1] && (b[1] = b[1]());
            return b
        },
        ga = function(a, c) {
            if (null != a && "undefined" != typeof a) {
                var b = d(this),
                k = b.data("mCS"),
                l = k.opt,
                k = d("#mCSB_" + k.idx + "_container"),
                g = k.parent(),
                f = typeof a;
                c || (c = "x" === l.axis ? "x": "y");
                var l = "x" === c ? k.outerWidth(!1) : k.outerHeight(!1),
                e = "x" === c ? k[0].offsetLeft: k[0].offsetTop,
                m = "x" === c ? "left": "top";
                switch (f) {
                case "function":
                    return a();
                case "object":
                    b = a.jquery ? a: d(a);
                    if (!b.length) break;
                    return "x" === c ? z(b)[1] : z(b)[0];
                case "string":
                case "number":
                    if (V(a)) return Math.abs(a);
                    if ( - 1 !== a.indexOf("%")) return Math.abs(l * parseInt(a) / 100);
                    if ( - 1 !== a.indexOf("-\x3d")) return Math.abs(e - parseInt(a.split("-\x3d")[1]));
                    if ( - 1 !== a.indexOf("+\x3d")) return b = e + parseInt(a.split("+\x3d")[1]),
                    0 <= b ? 0 : Math.abs(b);
                    if ( - 1 !== a.indexOf("px") && V(a.split("px")[0])) return Math.abs(a.split("px")[0]);
                    if ("top" === a || "left" === a) return 0;
                    if ("bottom" === a) return Math.abs(g.height() - k.outerHeight(!1));
                    if ("right" === a) return Math.abs(g.width() - k.outerWidth(!1));
                    if ("first" === a || "last" === a) return b = k.find(":" + a),
                    "x" === c ? z(b)[1] : z(b)[0];
                    if (d(a).length) return "x" === c ? z(d(a))[1] : z(d(a))[0];
                    k.css(m, a);
                    B.update.call(null, b[0])
                }
            }
        },
        $ = function(a) {
            function c() {
                clearTimeout(m[0].autoUpdate);
                0 === g.parents("html").length ? g = null: m[0].autoUpdate = setTimeout(function() {
                    if (e.advanced.updateOnSelectorChange && (f.poll.change.n = k(), f.poll.change.n !== f.poll.change.o)) {
                        f.poll.change.o = f.poll.change.n;
                        l(3);
                        return
                    }
                    if (e.advanced.updateOnContentResize && (f.poll.size.n = g[0].scrollHeight + g[0].scrollWidth + m[0].offsetHeight + g[0].offsetHeight + g[0].offsetWidth, f.poll.size.n !== f.poll.size.o)) {
                        f.poll.size.o = f.poll.size.n;
                        l(1);
                        return
                    }
                    if (e.advanced.updateOnImageLoad && ("auto" !== e.advanced.updateOnImageLoad || "y" !== e.axis) && (f.poll.img.n = m.find("img").length, f.poll.img.n !== f.poll.img.o)) {
                        f.poll.img.o = f.poll.img.n;
                        m.find("img").each(function() {
                            b(this)
                        });
                        return
                    } (e.advanced.updateOnSelectorChange || e.advanced.updateOnContentResize || e.advanced.updateOnImageLoad) && c()
                },
                e.advanced.autoUpdateTimeout)
            }
            function b(a) {
                function b(a, c) {
                    return function() {
                        return c.apply(a, arguments)
                    }
                }
                function c() {
                    this.onload = null;
                    d(a).addClass(p[2]);
                    l(2)
                }
                if (d(a).hasClass(p[2])) l();
                else {
                    var e = new Image;
                    e.onload = b(e, c);
                    e.src = a.src
                }
            }
            function k() { ! 0 === e.advanced.updateOnSelectorChange && (e.advanced.updateOnSelectorChange = "*");
                var a = 0,
                b = m.find(e.advanced.updateOnSelectorChange);
                e.advanced.updateOnSelectorChange && 0 < b.length && b.each(function() {
                    a += this.offsetHeight + this.offsetWidth
                });
                return a
            }
            function l(a) {
                clearTimeout(m[0].autoUpdate);
                B.update.call(null, g[0], a)
            }
            var g = d(this),
            f = g.data("mCS"),
            e = f.opt,
            m = d("#mCSB_" + f.idx + "_container");
            a ? (clearTimeout(m[0].autoUpdate), G(m[0], "autoUpdate")) : c()
        },
        va = function(a, c, b) {
            return Math.round(a / c) * c - b
        },
        F = function(a) {
            a = a.data("mCS");
            d("#mCSB_" + a.idx + "_container,#mCSB_" + a.idx + "_container_wrapper,#mCSB_" + a.idx + "_dragger_vertical,#mCSB_" + a.idx + "_dragger_horizontal").each(function() {
                this._mTween || (this._mTween = {
                    top: {},
                    left: {}
                });
                for (var a = ["top", "left"], b = 0; b < a.length; b++) {
                    var d = a[b];
                    this._mTween[d].id && (window.requestAnimationFrame ? window.cancelAnimationFrame(this._mTween[d].id) : clearTimeout(this._mTween[d].id), this._mTween[d].id = null, this._mTween[d].stop = 1)
                }
            })
        },
        u = function(a, c, b) {
            function k(a) {
                return g && f.callbacks[a] && "function" === typeof f.callbacks[a]
            }
            function l() {
                var c = [h[0].offsetTop, h[0].offsetLeft],
                d = [t[0].offsetTop, t[0].offsetLeft],
                e = [h.outerHeight(!1), h.outerWidth(!1)],
                f = [m.height(), m.width()];
                a[0].mcs = {
                    content: h,
                    top: c[0],
                    left: c[1],
                    draggerTop: d[0],
                    draggerLeft: d[1],
                    topPct: Math.round(100 * Math.abs(c[0]) / (Math.abs(e[0]) - f[0])),
                    leftPct: Math.round(100 * Math.abs(c[1]) / (Math.abs(e[1]) - f[1])),
                    direction: b.dir
                }
            }
            var g = a.data("mCS"),
            f = g.opt;
            b = d.extend({
                trigger: "internal",
                dir: "y",
                scrollEasing: "mcsEaseOut",
                drag: !1,
                dur: f.scrollInertia,
                overwrite: "all",
                callbacks: !0,
                onStart: !0,
                onUpdate: !0,
                onComplete: !0
            },
            b);
            var e = [b.dur, b.drag ? 0 : b.dur],
            m = d("#mCSB_" + g.idx),
            h = d("#mCSB_" + g.idx + "_container"),
            n = h.parent(),
            p = f.callbacks.onTotalScrollOffset ? aa.call(a, f.callbacks.onTotalScrollOffset) : [0, 0],
            r = f.callbacks.onTotalScrollBackOffset ? aa.call(a, f.callbacks.onTotalScrollBackOffset) : [0, 0];
            g.trigger = b.trigger;
            if (0 !== n.scrollTop() || 0 !== n.scrollLeft()) d(".mCSB_" + g.idx + "_scrollbar").css("visibility", "visible"),
            n.scrollTop(0).scrollLeft(0);
            "_resetY" !== c || g.contentReset.y || (k("onOverflowYNone") && f.callbacks.onOverflowYNone.call(a[0]), g.contentReset.y = 1);
            "_resetX" !== c || g.contentReset.x || (k("onOverflowXNone") && f.callbacks.onOverflowXNone.call(a[0]), g.contentReset.x = 1);
            if ("_resetY" !== c && "_resetX" !== c) { ! g.contentReset.y && a[0].mcs || !g.overflowed[0] || (k("onOverflowY") && f.callbacks.onOverflowY.call(a[0]), g.contentReset.x = null); ! g.contentReset.x && a[0].mcs || !g.overflowed[1] || (k("onOverflowX") && f.callbacks.onOverflowX.call(a[0]), g.contentReset.x = null);
                f.snapAmount && (c = va(c, f.snapAmount instanceof Array ? "x" === b.dir ? f.snapAmount[1] : f.snapAmount[0] : f.snapAmount, f.snapOffset));
                switch (b.dir) {
                case "x":
                    var t = d("#mCSB_" + g.idx + "_dragger_horizontal"),
                    A = "left",
                    v = h[0].offsetLeft,
                    w = [m.width() - h.outerWidth(!1), t.parent().width() - t.width()],
                    x = [c, 0 === c ? 0 : c / g.scrollRatio.x],
                    y = p[1],
                    s = r[1],
                    u = 0 < y ? y / g.scrollRatio.x: 0,
                    Q = 0 < s ? s / g.scrollRatio.x: 0;
                    break;
                case "y":
                    t = d("#mCSB_" + g.idx + "_dragger_vertical"),
                    A = "top",
                    v = h[0].offsetTop,
                    w = [m.height() - h.outerHeight(!1), t.parent().height() - t.height()],
                    x = [c, 0 === c ? 0 : c / g.scrollRatio.y],
                    y = p[0],
                    s = r[0],
                    u = 0 < y ? y / g.scrollRatio.y: 0,
                    Q = 0 < s ? s / g.scrollRatio.y: 0
                }
                0 > x[1] || 0 === x[0] && 0 === x[1] ? x = [0, 0] : x[1] >= w[1] ? x = [w[0], w[1]] : x[0] = -x[0];
                a[0].mcs || (l(), k("onInit") && f.callbacks.onInit.call(a[0]));
                clearTimeout(h[0].onCompleteTimeout);
                ha(t[0], A, Math.round(x[1]), e[1], b.scrollEasing); ! g.tweenRunning && (0 === v && 0 <= x[0] || v === w[0] && x[0] <= w[0]) || ha(h[0], A, Math.round(x[0]), e[0], b.scrollEasing, b.overwrite, {
                    onStart: function() {
                        b.callbacks && b.onStart && !g.tweenRunning && (k("onScrollStart") && (l(), f.callbacks.onScrollStart.call(a[0])), g.tweenRunning = !0, X(t), g.cbOffsets = [f.callbacks.alwaysTriggerOffsets || v >= w[0] + y, f.callbacks.alwaysTriggerOffsets || v <= -s])
                    },
                    onUpdate: function() {
                        b.callbacks && b.onUpdate && k("whileScrolling") && (l(), f.callbacks.whileScrolling.call(a[0]))
                    },
                    onComplete: function() {
                        b.callbacks && b.onComplete && ("yx" === f.axis && clearTimeout(h[0].onCompleteTimeout), h[0].onCompleteTimeout = setTimeout(function() {
                            k("onScroll") && (l(), f.callbacks.onScroll.call(a[0]));
                            k("onTotalScroll") && x[1] >= w[1] - u && g.cbOffsets[0] && (l(), f.callbacks.onTotalScroll.call(a[0]));
                            k("onTotalScrollBack") && x[1] <= Q && g.cbOffsets[1] && (l(), f.callbacks.onTotalScrollBack.call(a[0]));
                            g.tweenRunning = !1;
                            h[0].idleTimer = 0;
                            X(t, "hide")
                        },
                        h[0].idleTimer || 0))
                    }
                })
            }
        },
        ha = function(a, c, b, d, l, g, f) {
            function e() {
                s.stop || (v || n.call(), v = R() - t, m(), v >= s.time && (s.time = v > s.time ? v + u - (v - s.time) : v + u - 1, s.time < v + 1 && (s.time = v + 1)), s.time < d ? s.id = y(e) : r.call())
            }
            function m() {
                0 < d ? (s.currVal = h(s.time, w, z, d, l), x[c] = Math.round(s.currVal) + "px") : x[c] = b + "px";
                p.call()
            }
            function h(a, b, c, d, e) {
                switch (e) {
                case "linear":
                case "mcsLinear":
                    return c * a / d + b;
                case "mcsLinearOut":
                    return a /= d,
                    a--,
                    c * Math.sqrt(1 - a * a) + b;
                case "easeInOutSmooth":
                    a /= d / 2;
                    if (1 > a) return c / 2 * a * a + b;
                    a--;
                    return - c / 2 * (a * (a - 2) - 1) + b;
                case "easeInOutStrong":
                    a /= d / 2;
                    if (1 > a) return c / 2 * Math.pow(2, 10 * (a - 1)) + b;
                    a--;
                    return c / 2 * ( - Math.pow(2, -10 * a) + 2) + b;
                case "easeInOut":
                case "mcsEaseInOut":
                    a /= d / 2;
                    if (1 > a) return c / 2 * a * a * a + b;
                    a -= 2;
                    return c / 2 * (a * a * a + 2) + b;
                case "easeOutSmooth":
                    return a /= d,
                    a--,
                    -c * (a * a * a * a - 1) + b;
                case "easeOutStrong":
                    return c * ( - Math.pow(2, -10 * a / d) + 1) + b;
                default:
                    return d = (a /= d) * a,
                    e = d * a,
                    b + c * (.499999999999997 * e * d + -2.5 * d * d + 5.5 * e + -6.5 * d + 4 * a)
                }
            }
            a._mTween || (a._mTween = {
                top: {},
                left: {}
            });
            f = f || {};
            var n = f.onStart ||
            function() {},
            p = f.onUpdate ||
            function() {},
            r = f.onComplete ||
            function() {},
            t = R(),
            u,
            v = 0,
            w = a.offsetTop,
            x = a.style,
            y,
            s = a._mTween[c];
            "left" === c && (w = a.offsetLeft);
            var z = b - w;
            s.stop = 0;
            "none" !== g && null != s.id && (window.requestAnimationFrame ? window.cancelAnimationFrame(s.id) : clearTimeout(s.id), s.id = null); (function() {
                u = 1E3 / 60;
                s.time = v + u;
                y = window.requestAnimationFrame ? window.requestAnimationFrame: function(a) {
                    m();
                    return setTimeout(a, .01)
                };
                s.id = y(e)
            })()
        },
        R = function() {
            return window.performance && window.performance.now ? window.performance.now() : window.performance && window.performance.webkitNow ? window.performance.webkitNow() : Date.now ? Date.now() : (new Date).getTime()
        },
        G = function(a, c) {
            try {
                delete a[c]
            } catch(b) {
                a[c] = null
            }
        },
        Y = function(a) {
            a = a.originalEvent.pointerType;
            return ! (a && "touch" !== a && 2 !== a)
        },
        V = function(a) {
            return ! isNaN(parseFloat(a)) && isFinite(a)
        },
        z = function(a) {
            var c = a.parents(".mCSB_container");
            return [a.offset().top - c.offset().top, a.offset().left - c.offset().left]
        },
        la = function() {
            var a = function() {
                var a = ["webkit", "moz", "ms", "o"];
                if ("hidden" in document) return "hidden";
                for (var b = 0; b < a.length; b++) if (a[b] + "Hidden" in document) return a[b] + "Hidden";
                return null
            } ();
            return a ? document[a] : !1
        };
        d.fn.mCustomScrollbar = function(a) {
            if (B[a]) return B[a].apply(this, Array.prototype.slice.call(arguments, 1));
            if ("object" !== typeof a && a) d.error("Method " + a + " does not exist");
            else return B.init.apply(this, arguments)
        };
        d.mCustomScrollbar = function(a) {
            if (B[a]) return B[a].apply(this, Array.prototype.slice.call(arguments, 1));
            if ("object" !== typeof a && a) d.error("Method " + a + " does not exist");
            else return B.init.apply(this, arguments)
        };
        d.mCustomScrollbar.defaults = S;
        window.mCustomScrollbar = !0;
        d(window).load(function() {
            d(".mCustomScrollbar").mCustomScrollbar();
            d.extend(d.expr[":"], {
                mcsInView: d.expr[":"].mcsInView ||
                function(a) {
                    a = d(a);
                    var c = a.parents(".mCSB_container"),
                    b;
                    if (c.length) return b = c.parent(),
                    c = [c[0].offsetTop, c[0].offsetLeft],
                    0 <= c[0] + z(a)[0] && c[0] + z(a)[0] < b.height() - a.outerHeight(!1) && 0 <= c[1] + z(a)[1] && c[1] + z(a)[1] < b.width() - a.outerWidth(!1)
                },
                mcsOverflow: d.expr[":"].mcsOverflow ||
                function(a) {
                    if (a = d(a).data("mCS")) return a.overflowed[0] || a.overflowed[1]
                }
            })
        })
    })
}); (function(d) {
    var l = {
        className: "autosizejs",
        append: "",
        callback: !1,
        resizeDelay: 10
    },
    q = "fontFamily fontSize fontWeight fontStyle letterSpacing textTransform wordSpacing textIndent".split(" "),
    h,
    c = d('\x3ctextarea tabindex\x3d"-1" style\x3d"position:absolute; top:-999px; left:0; right:auto; bottom:auto; border:0; padding: 0; -moz-box-sizing:content-box; -webkit-box-sizing:content-box; box-sizing:content-box; word-wrap:break-word; height:0 !important; min-height:0 !important; overflow:hidden; transition:none; -webkit-transition:none; -moz-transition:none;"/\x3e').data("autosize", !0)[0];
    c.style.lineHeight = "99px";
    "99px" === d(c).css("lineHeight") && q.push("lineHeight");
    c.style.lineHeight = "";
    d.fn.autosize = function(e) {
        if (!this.length) return this;
        e = d.extend({},
        l, e || {});
        c.parentNode !== document.body && d(document.body).append(c);
        return this.each(function() {
            function r() {
                var g, e = window.getComputedStyle ? window.getComputedStyle(b, null) : !1;
                e ? (g = b.getBoundingClientRect().width, d.each(["paddingLeft", "paddingRight", "borderLeftWidth", "borderRightWidth"],
                function(a, b) {
                    g -= parseInt(e[b], 10)
                }), c.style.width = g + "px") : c.style.width = Math.max(a.width(), 0) + "px"
            }
            function l() {
                var g = {};
                h = b;
                c.className = e.className;
                k = parseInt(a.css("maxHeight"), 10);
                d.each(q,
                function(b, c) {
                    g[c] = a.css(c)
                });
                d(c).css(g);
                r();
                if (window.chrome) {
                    var f = b.style.width;
                    b.style.width = "0px";
                    b.style.width = f
                }
            }
            function f() {
                var a, d;
                h !== b ? l() : r();
                c.value = b.value + e.append;
                c.style.overflowY = b.style.overflowY;
                d = parseInt(b.style.height, 10);
                c.scrollTop = 0;
                c.scrollTop = 9E4;
                a = c.scrollTop;
                k && a > k ? (b.style.overflowY = "scroll", a = k) : (b.style.overflowY = "hidden", a < m && (a = m));
                a += n;
                d !== a && (b.style.height = a + "px", u && e.callback.call(b, b))
            }
            function s() {
                clearTimeout(p);
                p = setTimeout(function() {
                    var b = a.width();
                    b !== t && (t = b, f())
                },
                parseInt(e.resizeDelay, 10))
            }
            var b = this,
            a = d(b),
            k,
            m,
            n = 0,
            u = d.isFunction(e.callback),
            v = {
                height: b.style.height,
                overflow: b.style.overflow,
                overflowY: b.style.overflowY,
                wordWrap: b.style.wordWrap,
                resize: b.style.resize
            },
            p,
            t = a.width();
            if (!a.data("autosize")) {
                a.data("autosize", !0);
                if ("border-box" === a.css("box-sizing") || "border-box" === a.css("-moz-box-sizing") || "border-box" === a.css("-webkit-box-sizing")) n = a.outerHeight() - a.height();
                m = Math.max(parseInt(a.css("minHeight"), 10) - n || 0, a.height());
                a.css({
                    overflow: "hidden",
                    overflowY: "hidden",
                    wordWrap: "break-word",
                    resize: "none" === a.css("resize") || "vertical" === a.css("resize") ? "none": "horizontal"
                });
                if ("onpropertychange" in b) if ("oninput" in b) a.on("input.autosize keyup.autosize", f);
                else a.on("propertychange.autosize",
                function() {
                    "value" === event.propertyName && f()
                });
                else a.on("input.autosize", f);
                if (!1 !== e.resizeDelay) d(window).on("resize.autosize", s);
                a.on("autosize.resize", f);
                a.on("autosize.resizeIncludeStyle",
                function() {
                    h = null;
                    f()
                });
                a.on("autosize.destroy",
                function() {
                    h = null;
                    clearTimeout(p);
                    d(window).off("resize", s);
                    a.off("autosize").off(".autosize").css(v).removeData("autosize");
                    d(c).remove()
                });
                f()
            }
        })
    }
})(window.jQuery || window.$); (function(b) {
    function B() {
        return b("\x3cdiv/\x3e")
    }
    var C = Math.abs,
    n = Math.max,
    p = Math.min,
    g = Math.round;
    b.imgAreaSelect = function(z, d) {
        function O(a) {
            return a + A.left - w.left
        }
        function P(a) {
            return a + A.top - w.top
        }
        function J(a) {
            return a - A.left + w.left
        }
        function K(a) {
            return a - A.top + w.top
        }
        function G(a) {
            var d = a || U;
            a = a || V;
            return {
                x1: g(c.x1 * d),
                y1: g(c.y1 * a),
                x2: g(c.x2 * d),
                y2: g(c.y2 * a),
                width: g(c.x2 * d) - g(c.x1 * d),
                height: g(c.y2 * a) - g(c.y1 * a)
            }
        }
        function ha(a, d, b, f, e) {
            var h = e || U;
            e = e || V;
            c = {
                x1: g(a / h || 0),
                y1: g(d / e || 0),
                x2: g(b / h || 0),
                y2: g(f / e || 0)
            };
            c.width = c.x2 - c.x1;
            c.height = c.y2 - c.y1
        }
        function M() {
            $ && x.width() && (A = {
                left: g(x.offset().left),
                top: g(x.offset().top)
            },
            y = x.innerWidth(), u = x.innerHeight(), A.top += x.outerHeight() - u >> 1, A.left += x.outerWidth() - y >> 1, W = g(d.minWidth / U) || 0, X = g(d.minHeight / V) || 0, ia = g(p(d.maxWidth / U || 16777216, y)), ja = g(p(d.maxHeight / V || 16777216, u)), "1.3.2" != b().jquery || "fixed" != aa || ka.getBoundingClientRect || (A.top += n(document.body.scrollTop, ka.scrollTop), A.left += n(document.body.scrollLeft, ka.scrollLeft)), w = /absolute|relative/.test(Q.css("position")) ? {
                left: g(Q.offset().left) - Q.scrollLeft(),
                top: g(Q.offset().top) - Q.scrollTop()
            }: "fixed" == aa ? {
                left: b(document).scrollLeft(),
                top: b(document).scrollTop()
            }: {
                left: 0,
                top: 0
            },
            q = O(0), r = P(0), (c.x2 > y || c.y2 > u) && ba())
        }
        function ca(a) {
            if (da) {
                l.css({
                    left: O(c.x1),
                    top: P(c.y1)
                }).add(R).width(E = c.width).height(L = c.height);
                R.add(t).add(s).css({
                    left: 0,
                    top: 0
                });
                t.width(n(E - t.outerWidth() + t.innerWidth(), 0)).height(n(L - t.outerHeight() + t.innerHeight(), 0));
                b(m[0]).css({
                    left: q,
                    top: r,
                    width: c.x1,
                    height: u
                });
                b(m[1]).css({
                    left: q + c.x1,
                    top: r,
                    width: E,
                    height: c.y1
                });
                b(m[2]).css({
                    left: q + c.x2,
                    top: r,
                    width: y - c.x2,
                    height: u
                });
                b(m[3]).css({
                    left: q + c.x1,
                    top: r + c.y2,
                    width: E,
                    height: u - c.y2
                });
                E -= s.outerWidth();
                L -= s.outerHeight();
                switch (s.length) {
                case 8:
                    b(s[4]).css({
                        left:
                        E >> 1
                    }),
                    b(s[5]).css({
                        left: E,
                        top: L >> 1
                    }),
                    b(s[6]).css({
                        left: E >> 1,
                        top: L
                    }),
                    b(s[7]).css({
                        top: L >> 1
                    });
                case 4:
                    s.slice(1, 3).css({
                        left: E
                    }),
                    s.slice(2, 4).css({
                        top: L
                    })
                }
                if (!1 !== a && (b.imgAreaSelect.onKeyPress != ta && b(document).unbind(b.imgAreaSelect.keyPress, b.imgAreaSelect.onKeyPress), d.keys)) b(document)[b.imgAreaSelect.keyPress](b.imgAreaSelect.onKeyPress = ta);
                S && 2 == t.outerWidth() - t.innerWidth() && (t.css("margin", 0), setTimeout(function() {
                    t.css("margin", "auto")
                },
                0))
            }
        }
        function la(a) {
            M();
            ca(a);
            f = O(c.x1);
            e = P(c.y1);
            h = O(c.x2);
            k = P(c.y2)
        }
        function ma(a, b) {
            d.fadeSpeed ? a.fadeOut(d.fadeSpeed, b) : a.hide()
        }
        function N(a) {
            var b = J(a.pageX - w.left) - c.x1;
            a = K(a.pageY - w.top) - c.y1;
            na || (M(), na = !0, l.one("mouseout",
            function() {
                na = !1
            }));
            v = "";
            d.resizable && (a <= d.resizeMargin ? v = "n": a >= c.height - d.resizeMargin && (v = "s"), b <= d.resizeMargin ? v += "w": b >= c.width - d.resizeMargin && (v += "e"));
            l.css("cursor", v ? v + "-resize": d.movable ? "move": "");
            ea && ea.toggle()
        }
        function ua(a) {
            b("body").css("cursor", ""); (d.autoHide || 0 == c.width * c.height) && ma(l.add(m),
            function() {
                b(this).hide()
            });
            b(document).unbind("mousemove", oa);
            l.mousemove(N);
            d.onSelectEnd(z, G())
        }
        function va(a) {
            if (1 != a.which) return ! 1;
            M();
            v ? (b("body").css("cursor", v + "-resize"), f = O(c[/w/.test(v) ? "x2": "x1"]), e = P(c[/n/.test(v) ? "y2": "y1"]), b(document).mousemove(oa).one("mouseup", ua), l.unbind("mousemove", N)) : d.movable ? (pa = q + c.x1 - (a.pageX - w.left), qa = r + c.y1 - (a.pageY - w.top), l.unbind("mousemove", N), b(document).mousemove(wa).one("mouseup",
            function() {
                d.onSelectEnd(z, G());
                b(document).unbind("mousemove", wa);
                l.mousemove(N)
            })) : x.mousedown(a);
            return ! 1
        }
        function Y(a) {
            H && (a ? (h = n(q, p(q + y, f + C(k - e) * H * (h > f || -1))), k = g(n(r, p(r + u, e + C(h - f) / H * (k > e || -1)))), h = g(h)) : (k = n(r, p(r + u, e + C(h - f) / H * (k > e || -1))), h = g(n(q, p(q + y, f + C(k - e) * H * (h > f || -1)))), k = g(k)))
        }
        function ba() {
            f = p(f, q + y);
            e = p(e, r + u);
            C(h - f) < W && (h = f - W * (h < f || -1), h < q ? f = q + W: h > q + y && (f = q + y - W));
            C(k - e) < X && (k = e - X * (k < e || -1), k < r ? e = r + X: k > r + u && (e = r + u - X));
            h = n(q, p(h, q + y));
            k = n(r, p(k, r + u));
            Y(C(h - f) < C(k - e) * H);
            C(h - f) > ia && (h = f - ia * (h < f || -1), Y());
            C(k - e) > ja && (k = e - ja * (k < e || -1), Y(!0));
            c = {
                x1: J(p(f, h)),
                x2: J(n(f, h)),
                y1: K(p(e, k)),
                y2: K(n(e, k)),
                width: C(h - f),
                height: C(k - e)
            };
            ca();
            d.onSelectChange(z, G())
        }
        function oa(a) {
            h = /w|e|^$/.test(v) || H ? a.pageX - w.left: O(c.x2);
            k = /n|s|^$/.test(v) || H ? a.pageY - w.top: P(c.y2);
            ba();
            return ! 1
        }
        function Z(a, g) {
            h = (f = a) + c.width;
            k = (e = g) + c.height;
            b.extend(c, {
                x1: J(f),
                y1: K(e),
                x2: J(h),
                y2: K(k)
            });
            ca();
            d.onSelectChange(z, G())
        }
        function wa(a) {
            f = n(q, p(pa + (a.pageX - w.left), q + y - c.width));
            e = n(r, p(qa + (a.pageY - w.top), r + u - c.height));
            Z(f, e);
            a.preventDefault();
            return ! 1
        }
        function ra() {
            b(document).unbind("mousemove", ra);
            M();
            h = f;
            k = e;
            ba();
            v = "";
            m.is(":visible") || l.add(m).hide().fadeIn(d.fadeSpeed || 0);
            da = !0;
            b(document).unbind("mouseup", fa).mousemove(oa).one("mouseup", ua);
            l.unbind("mousemove", N);
            d.onSelectStart(z, G())
        }
        function fa() {
            b(document).unbind("mousemove", ra).unbind("mouseup", fa);
            ma(l.add(m));
            ha(J(f), K(e), J(f), K(e));
            this instanceof b.imgAreaSelect || (d.onSelectChange(z, G()), d.onSelectEnd(z, G()))
        }
        function xa(a) {
            if (1 != a.which || m.is(":animated")) return ! 1;
            M();
            pa = f = a.pageX - w.left;
            qa = e = a.pageY - w.top;
            b(document).mousemove(ra).mouseup(fa);
            return ! 1
        }
        function ya() {
            la(!1)
        }
        function za() {
            $ = !0;
            sa(d = b.extend({
                classPrefix: "imgareaselect",
                movable: !0,
                parent: "body",
                resizable: !0,
                resizeMargin: 10,
                onInit: function() {},
                onSelectStart: function() {},
                onSelectChange: function() {},
                onSelectEnd: function() {}
            },
            d));
            l.add(m).css({
                visibility: ""
            });
            d.show && (da = !0, M(), ca(), l.add(m).hide().fadeIn(d.fadeSpeed || 0));
            setTimeout(function() {
                d.onInit(z, G())
            },
            0)
        }
        function ga(a, b) {
            for (var c in b) void 0 !== d[c] && a.css(b[c], d[c])
        }
        function sa(a) {
            a.parent && (Q = b(a.parent)).append(l.add(m));
            b.extend(d, a);
            M();
            if (null != a.handles) {
                s.remove();
                s = b([]);
                for (T = a.handles ? "corners" == a.handles ? 4 : 8 : 0; T--;) s = s.add(B());
                s.addClass(d.classPrefix + "-handle").css({
                    position: "absolute",
                    fontSize: 0,
                    zIndex: I + 1 || 1
                });
                0 <= !parseInt(s.css("width")) && s.width(5).height(5); (F = d.borderWidth) && s.css({
                    borderWidth: F,
                    borderStyle: "solid"
                });
                ga(s, {
                    borderColor1: "border-color",
                    borderColor2: "background-color",
                    borderOpacity: "opacity"
                })
            }
            U = d.imageWidth / y || 1;
            V = d.imageHeight / u || 1;
            null != a.x1 && (ha(a.x1, a.y1, a.x2, a.y2), a.show = !a.hide);
            a.keys && (d.keys = b.extend({
                shift: 1,
                ctrl: "resize"
            },
            a.keys));
            m.addClass(d.classPrefix + "-outer");
            R.addClass(d.classPrefix + "-selection");
            for (T = 0; 4 > T++;) b(t[T - 1]).addClass(d.classPrefix + "-border" + T);
            ga(R, {
                selectionColor: "background-color",
                selectionOpacity: "opacity"
            });
            ga(t, {
                borderOpacity: "opacity",
                borderWidth: "border-width"
            });
            ga(m, {
                outerColor: "background-color",
                outerOpacity: "opacity"
            }); (F = d.borderColor1) && b(t[0]).css({
                borderStyle: "solid",
                borderColor: F
            }); (F = d.borderColor2) && b(t[1]).css({
                borderStyle: "dashed",
                borderColor: F
            });
            l.append(R.add(t).add(ea)).append(s);
            S && ((F = (m.css("filter") || "").match(/opacity=(\d+)/)) && m.css("opacity", F[1] / 100), (F = (t.css("filter") || "").match(/opacity=(\d+)/)) && t.css("opacity", F[1] / 100));
            a.hide ? ma(l.add(m)) : a.show && $ && (da = !0, l.add(m).fadeIn(d.fadeSpeed || 0), la());
            H = (Aa = (d.aspectRatio || "").split(/:/))[0] / Aa[1];
            x.add(m).unbind("mousedown", xa);
            if (d.disable || !1 === d.enable) l.unbind("mousemove", N).unbind("mousedown", va),
            b(window).unbind("resize", ya);
            else {
                if (d.enable || !1 === d.disable)(d.resizable || d.movable) && l.mousemove(N).mousedown(va),
                b(window).resize(ya);
                d.persistent || x.add(m).mousedown(xa)
            }
            d.enable = d.disable = void 0
        }
        var x = b(z),
        $,
        l = B(),
        R = B(),
        t = B().add(B()).add(B()).add(B()),
        m = B().add(B()).add(B()).add(B()),
        s = b([]),
        ea,
        q,
        r,
        A = {
            left: 0,
            top: 0
        },
        y,
        u,
        Q,
        w = {
            left: 0,
            top: 0
        },
        I = 0,
        aa = "absolute",
        pa,
        qa,
        U,
        V,
        v,
        W,
        X,
        ia,
        ja,
        H,
        da,
        f,
        e,
        h,
        k,
        c = {
            x1: 0,
            y1: 0,
            x2: 0,
            y2: 0,
            width: 0,
            height: 0
        },
        ka = document.documentElement,
        D = navigator.userAgent,
        Aa,
        T,
        F,
        E,
        L,
        na,
        ta = function(a) {
            var b = d.keys,
            c, g = a.keyCode;
            c = isNaN(b.alt) || !a.altKey && !a.originalEvent.altKey ? !isNaN(b.ctrl) && a.ctrlKey ? b.ctrl: !isNaN(b.shift) && a.shiftKey ? b.shift: isNaN(b.arrows) ? 10 : b.arrows: b.alt;
            if ("resize" == b.arrows || "resize" == b.shift && a.shiftKey || "resize" == b.ctrl && a.ctrlKey || "resize" == b.alt && (a.altKey || a.originalEvent.altKey)) {
                switch (g) {
                case 37:
                    c = -c;
                case 39:
                    a = n(f, h);
                    f = p(f, h);
                    h = n(a + c, f);
                    Y();
                    break;
                case 38:
                    c = -c;
                case 40:
                    a = n(e, k);
                    e = p(e, k);
                    k = n(a + c, e);
                    Y(!0);
                    break;
                default:
                    return
                }
                ba()
            } else switch (f = p(f, h), e = p(e, k), g) {
            case 37:
                Z(n(f - c, q), e);
                break;
            case 38:
                Z(f, n(e - c, r));
                break;
            case 39:
                Z(f + p(c, y - J(h)), e);
                break;
            case 40:
                Z(f, e + p(c, u - K(k)));
                break;
            default:
                return
            }
            return ! 1
        };
        this.remove = function() {
            sa({
                disable: !0
            });
            l.add(m).remove()
        };
        this.getOptions = function() {
            return d
        };
        this.setOptions = sa;
        this.getSelection = G;
        this.setSelection = ha;
        this.cancelSelection = fa;
        this.update = la;
        for (var S = (/msie ([\w.]+)/i.exec(D) || [])[1], Ba = /opera/i.test(D), Ca = /webkit/i.test(D) && !/chrome/i.test(D), D = x; D.length;) I = n(I, isNaN(D.css("z-index")) ? I: D.css("z-index")),
        "fixed" == D.css("position") && (aa = "fixed"),
        D = D.parent(":not(body)");
        I = d.zIndex || I;
        S && x.attr("unselectable", "on");
        b.imgAreaSelect.keyPress = S || Ca ? "keydown": "keypress";
        Ba && (ea = B().css({
            width: "100%",
            height: "100%",
            position: "absolute",
            zIndex: I + 2 || 2
        }));
        l.add(m).css({
            visibility: "hidden",
            position: aa,
            overflow: "hidden",
            zIndex: I || "0"
        });
        l.css({
            zIndex: I + 2 || 2
        });
        R.add(t).css({
            position: "absolute",
            fontSize: 0
        });
        z.complete || "complete" == z.readyState || !x.is("img") ? za() : x.one("load", za); ! $ && S && 7 <= S && (z.src = z.src)
    };
    b.fn.imgAreaSelect = function(g) {
        g = g || {};
        this.each(function() {
            b(this).data("imgAreaSelect") ? g.remove ? (b(this).data("imgAreaSelect").remove(), b(this).removeData("imgAreaSelect")) : b(this).data("imgAreaSelect").setOptions(g) : g.remove || (void 0 === g.enable && void 0 === g.disable && (g.enable = !0), b(this).data("imgAreaSelect", new b.imgAreaSelect(this, g)))
        });
        return g.instance ? b(this).data("imgAreaSelect") : this
    }
})(jQuery); (function(r, q, v) {
    q.fn.artZoom = function(g) {
        g = q.extend({},
        q.fn.artZoom.defaults, g);
        var b, l = g.path,
        h = l + "/loading.gif",
        p = l + "/zoomin.cur",
        l = l + "/zoomout.cur"; (new Image).src = h;
        p = "url('" + p + "'), pointer";
        b = ['\x3cdiv class\x3d"ui-artZoom-toolbar" style\x3d"display:none"\x3e\x3cspan class\x3d"ui-artZoom-buttons" style\x3d"display:none"\x3e\x3ca data-go\x3d"left" class\x3d"ui-artZoom-left"\x3e\x3cspan\x3e\x3c/span\x3e', g.left, '\x3c/a\x3e\x3ca data-go\x3d"right" class\x3d"ui-artZoom-right"\x3e\x3cspan\x3e\x3c/span\x3e', g.right, '\x3c/a\x3e\x3ca data-go\x3d"source" class\x3d"ui-artZoom-source"\x3e\x3cspan\x3e\x3c/span\x3e', g.source, '\x3c/a\x3e\x3ca data-go\x3d"hide" class\x3d"ui-artZoom-hide"\x3e\x3cspan\x3e\x3c/span\x3e', g.hide, '\x3c/a\x3e\x3c/span\x3e\x3cspan class\x3d"ui-artZoom-loading"\x3e\x3cimg data-live\x3d"stop" src\x3d"', h, '" style\x3d"display:inline-block;*zoom:1;*display:inline;vertical-align:middle;width:16px;height:16px;" /\x3e \x3cspan\x3eLoading..\x3c/span\x3e\x3c/span\x3e\x3c/div\x3e\x3cdiv class\x3d"ui-artZoom-box" style\x3d"display:none"\x3e\x3cspan class\x3d"ui-artZoom-photo" data-go\x3d"hide" style\x3d"display:inline-block;*display:inline;*zoom:1;overflow:hidden;position:relative;cursor:', "url('" + l + "'), pointer", '"\x3e\x3cimg data-name\x3d"thumb" data-go\x3d"hide" data-live\x3d"stop" src\x3d"', h, '" /\x3e\x3c/span\x3e\x3c/div\x3e'].join("");
        this.on("click",
        function(d) {
            if ("IMG" !== this.nodeName && "stop" === this.getAttribute("data-live")) return ! 1;
            var f, a, n = this,
            c = q(n);
            d = c.parent();
            var k = n.src,
            m = c.attr("data-artZoom-show") || k,
            e = c.attr("data-artZoom-source") || m,
            h = g.maxWidth || ("A" === d[0].nodeName ? c.parent() : c).parent().width(),
            p = g.maxHeight || 99999,
            h = h - g.borderWidth,
            h = 400;
            "A" === d[0].nodeName && (m = d.attr("data-artZoom-show") || d.attr("href"), e = d.attr("data-artZoom-source") || d.attr("rel"));
            if (c.data("artZoom")) c.hide();
            else {
                var l = r.createElement("div"),
                s,
                t,
                u;
                f = q(l);
                l.className = "ui-artZoom ui-artZoom-noLoad";
                l.innerHTML = b; ("A" === d[0].nodeName ? c.parent() : c).before(l);
                c.data("artZoom", f);
                t = f.find(".ui-artZoom-box");
                s = f.find("[data-name\x3dthumb]");
                w(m,
                function() {
                    var a = this.width,
                    b = this.height,
                    e = Math.min(h, a),
                    b = e / a * b,
                    a = e;
                    s.attr("src", k).css(g.blur ? {
                        width: a + "px",
                        height: b + "px"
                    }: {
                        display: "none"
                    }).after(['\x3cimg class\x3d"ui-artZoom-show" title\x3d"', n.title, '" alt\x3d"', n.alt, '" src\x3d"', m, '" style\x3d"width:', a, "px;height:", b, 'px;position:absolute;left:0;top:0;background:transparent" /\x3e'].join(""));
                    u = f.find(".ui-artZoom-show");
                    s.attr("class", "ui-artZoom-show");
                    f.addClass("ui-artZoom-ready");
                    f.find(".ui-artZoom-buttons").show();
                    c.data("artZoom-ready", !0);
                    c.hide();
                    t.show()
                },
                function() {
                    s.removeAttr("class").hide();
                    u.css({
                        position: "static",
                        left: "auto",
                        top: "auto"
                    });
                    f.removeClass("ui-artZoom-noLoad");
                    f.find(".ui-artZoom-loading").hide();
                    c.data("artZoom-load", !0)
                },
                function() {
                    f.addClass("ui-artZoom-error");
                    v('jQuery.fn.artZoom: Load "' + m + '" Error!')
                })
            }
            f = c.data("artZoom");
            a = function(b) {
                b = this.getAttribute("data-go");
                var g = this.getAttribute("data-live"),
                d = c.data("artZoom-degree") || 0,
                l = f.find(".ui-artZoom-show")[0];
                if ("stop" === g) return ! 1;
                /img|canvas$/i.test(this.nodeName) && (b = "hide");
                switch (b) {
                case "left":
                    d -= 90;
                    d = -90 === d ? 270 : d;
                    break;
                case "right":
                    d += 90;
                    d = 360 === d ? 0 : d;
                    break;
                case "source":
                    window.open(e || m || k);
                    break;
                case "hide":
                    c.show(),
                    f.find(".ui-artZoom-toolbar").hide(),
                    f.hide(),
                    f.find("[data-go]").off("click", a)
                }
                "left" !== b && "right" !== b || !c.data("artZoom-load") || (x(l, d, h, p), c.data("artZoom-degree", d));
                return ! 1
            };
            f.show().find(".ui-artZoom-toolbar").slideDown(150);
            f.find("[data-go]").on("click", a);
            return ! 1
        });
        this.on("mouseover",
        function() {
            "ui-artZoom-show" !== this.className && (this.style.cursor = p)
        });
        this[0] && (this[0].style.cursor = p);
        return this
    };
    q.fn.artZoom.defaults = {
        path: "/static/css/img/artzoom",
        left: "\u5de6\u65cb\u8f6c",
        right: "\u53f3\u65cb\u8f6c",
        source: "\u770b\u539f\u56fe",
        hide: "\u00d7",
        blur: !0,
        preload: !0,
        maxWidth: null,
        maxHeight: null,
        borderWidth: 18
    };
    var x = q.imgRotate = function() {
        var g = !!r.createElement("canvas").getContext;
        return function(b, l, h, p) {
            var d, f, a, n = 1,
            c = b.naturalWidth,
            k = b.naturalHeight;
            a = b["{$canvas}"];
            if (!b["{$canvas}"]) {
                if (! ("naturalWidth" in b)) {
                    a = b.runtimeStyle;
                    var m = a.width,
                    e = a.height;
                    a.width = a.height = "auto";
                    b.naturalWidth = c = b.width;
                    b.naturalHeight = k = b.height;
                    a.width = m;
                    a.height = e
                }
                b["{$canvas}"] = a = r.createElement(g ? "canvas": "span");
                b.parentNode.insertBefore(a, b.nextSibling);
                b.style.display = "none";
                a.className = b.className;
                a.title = b.title;
                g || (a.img = r.createElement("img"), a.img.src = b.src, a.appendChild(a.img), a.style.cssText = "display:inline-block;*zoom:1;*display:inline;padding:0;margin:0;border:none 0;position:static;float:none;overflow:hidden;width:auto;height:auto")
            }
            m = function(a) {
                a && (c = [k, k = c][0]);
                c > h && (n = h / c, k *= n, c = h);
                k > p && (n = n * p / k, c *= p / k, k = p);
                g && (a ? k: c) / b.naturalWidth
            };
            switch (l) {
            case 0:
                f = d = 0;
                m();
                break;
            case 90:
                d = 0;
                f = -b.naturalHeight;
                m(!0);
                break;
            case 180:
                d = -b.naturalWidth;
                f = -b.naturalHeight;
                m();
                break;
            case 270:
                d = -b.naturalWidth,
                f = 0,
                m(!0)
            }
            g ? (a.setAttribute("width", c), a.setAttribute("height", k), a = a.getContext("2d"), a.rotate(l * Math.PI / 180), a.scale(n, n), a.drawImage(b, d, f)) : (a.style.width = c + "px", a.style.height = k + "px", a.img.style.filter = "progid:DXImageTransform.Microsoft.BasicImage(rotation\x3d" + l / 90 + ")", a.img.width = b.width * n, a.img.height = b.height * n)
        }
    } (),
    w = function() {
        var g = [],
        b = null,
        l = function() {
            for (var h = 0; h < g.length; h++) g[h].end ? g.splice(h--, 1) : g[h]();
            g.length || (clearInterval(b), b = null)
        };
        return function(h, p, d, f) {
            var a, n, c, k, m, e = new Image;
            e.src = h;
            e.complete ? (p.call(e), d && d.call(e)) : (n = e.width, c = e.height, e.onerror = function() {
                f && f.call(e);
                a.end = !0;
                e = e.onload = e.onerror = null
            },
            a = function() {
                k = e.width;
                m = e.height;
                if (k !== n || m !== c || 1024 < k * m) p.call(e),
                a.end = !0
            },
            a(), e.onload = function() { ! a.end && a();
                d && d.call(e);
                e = e.onload = e.onerror = null
            },
            a.end || (g.push(a), null === b && (b = setInterval(l, 40))))
        }
    } ()
})(document, jQuery,
function(r) {
    window.console && console.log(r)
});
jQuery.extend({
    highlighter: {
        __config__: {
            sourceHtmlDataKey: "__jquery.highlighter.data.sourceHTML__"
        }
    }
});
jQuery.fn.highlight = function(f, n) {
    var k = jQuery.extend({
        hClass: null,
        hColor: "#C03",
        separator: " ",
        wrapper: "em",
        useDefaultStyle: !0,
        needUnhighlight: !1
    },
    n),
    l = "string" == typeof k.hClass && 0 < k.hClass.length,
    r = $("\x3c" + k.wrapper + "/\x3e");
    l ? r.addClass(k.hClass) : k.useDefaultStyle && (r.css("color", k.hColor), "em" == k.wrapper && r.css("font-style", "normal"));
    var l = /^\s*$/,
    g = null;
    if ("string" == typeof f) {
        if (l.test(f)) return;
        g = f.split(k.separator)
    } else jQuery.isArray(f) && (g = f);
    for (var m = 0; m < g.length; m++) {
        var u = g[m];
        if (l.test(u)) g.splice(m, 1);
        else for (var s = g.length - 1; s > m; s--) u == g[s] && g.splice(s, 1)
    }
    var x = [{
        spChar: "\\",
        escapeChar: "\\\\"
    },
    {
        spChar: "$",
        escapeChar: "\\$"
    },
    {
        spChar: "(",
        escapeChar: "\\("
    },
    {
        spChar: ")",
        escapeChar: "\\)"
    },
    {
        spChar: "*",
        escapeChar: "\\*"
    },
    {
        spChar: "+",
        escapeChar: "\\+"
    },
    {
        spChar: ".",
        escapeChar: "\\."
    },
    {
        spChar: "[",
        escapeChar: "\\["
    },
    {
        spChar: "?",
        escapeChar: "\\?"
    },
    {
        spChar: "^",
        escapeChar: "\\^"
    },
    {
        spChar: "{",
        escapeChar: "\\{"
    },
    {
        spChar: "|",
        escapeChar: "\\|"
    }],
    v = /<\/?[a-z][a-z0-9]*[^<>]*>/ig,
    w = /&(?:[a-z]+?|#[0-9]+?|#x[0-9a-f]+?);/ig,
    t = $("\x3cdiv /\x3e");
    return this.each(function() {
        var f = $(this),
        h = f.data(jQuery.highlighter.__config__.sourceHtmlDataKey);
        h || (h = f.html(), k.needUnhighlight && f.data(jQuery.highlighter.__config__.sourceHtmlDataKey, h));
        for (var a = null,
        b = []; null != (a = v.exec(h));) a = {
            start: a.index,
            end: v.lastIndex,
            tag: a[0]
        },
        b.push(a);
        for (var p = []; null != (a = w.exec(h));) a = {
            start: a.index,
            end: w.lastIndex,
            tag: a[0]
        },
        p.push(a);
        for (var e = [], c = 0; c < g.length; c++) {
            var m = t.text(g[c]).html();
            jQuery.each(x,
            function(a, b) {
                m = m.replace(b.spChar, b.escapeChar)
            });
            for (var d = new RegExp(m, "ig"); null != (a = d.exec(h));) a = {
                start: a.index,
                end: d.lastIndex
            },
            e.push(a)
        }
        for (c = e.length - 1; 0 <= c; c--) for (a = e[c], d = 0; d < b.length; d++) {
            var n = b[d];
            if (a.start > n.start && a.end < n.end) {
                e.splice(c, 1);
                break
            }
        }
        for (c = e.length - 1; 0 <= c; c--) for (a = e[c], d = 0; d < p.length; d++) {
            b = p[d];
            if (a.start > b.start && a.end <= b.end || a.start >= b.start && a.end < b.end) {
                e.splice(c, 1);
                break
            }
            if (a.start > b.start && a.start < b.end && a.end > b.end || a.start < b.start && a.end > b.start && a.end < b.end) {
                e.splice(c, 1);
                break
            }
        }
        p = [];
        for (c = 0; c < e.length; c++) {
            a = e[c];
            for (d = e.length - 1; d > c; d--) b = e[d],
            a.start <= b.start && a.end >= b.start && a.end < b.end ? (a.end = b.end, e.splice(d, 1)) : b.start < a.start && b.end >= a.start && b.end <= a.end ? (a.start = b.start, e.splice(d, 1)) : a.start <= b.start && a.end >= b.end ? e.splice(d, 1) : a.start >= b.start && a.end <= b.end && (a.start = b.start, a.end = b.end, e.splice(d, 1));
            p.push(a)
        }
        p.sort(function(a, b) {
            return a.start - b.start
        });
        var l = [],
        q = 0;
        jQuery.each(p,
        function(a, b) {
            q < b.start && l.push(h.substring(q, b.start));
            t.empty().append(r.clone().html(h.substring(b.start, b.end)));
            l.push(t.html());
            q = b.end
        });
        q < h.length && l.push(h.substr(q));
        f.html(l.join(""))
    })
};
jQuery.fn.unhighlight = function() {
    return this.each(function() {
        var f = $(this),
        n = f.data(jQuery.highlighter.__config__.sourceHtmlDataKey);
        n && (f.html(n), f.removeData(jQuery.highlighter.__config__.sourceHtmlDataKey))
    })
}; (function(s, D, f, K) {
    var J = f("html"),
    p = f(s),
    q = f(D),
    b = f.fancybox = function() {
        b.open.apply(this, arguments)
    },
    I = navigator.userAgent.match(/msie/i),
    v = null,
    t = void 0 !== D.createTouch,
    y = function(a) {
        return a && a.hasOwnProperty && a instanceof f
    },
    r = function(a) {
        return a && "string" === f.type(a)
    },
    F = function(a) {
        return r(a) && 0 < a.indexOf("%")
    },
    m = function(a, d) {
        var e = parseInt(a, 10) || 0;
        d && F(a) && (e *= b.getViewport()[d] / 100);
        return Math.ceil(e)
    },
    w = function(a, b) {
        return m(a, b) + "px"
    };
    f.extend(b, {
        version: "2.1.5",
        defaults: {
            padding: 15,
            margin: 20,
            width: 800,
            height: 600,
            minWidth: 100,
            minHeight: 100,
            maxWidth: 9999,
            maxHeight: 9999,
            pixelRatio: 1,
            autoSize: !0,
            autoHeight: !1,
            autoWidth: !1,
            autoResize: !0,
            autoCenter: !t,
            fitToView: !0,
            aspectRatio: !1,
            topRatio: .5,
            leftRatio: .5,
            scrolling: "auto",
            wrapCSS: "",
            arrows: !0,
            closeBtn: !0,
            closeClick: !1,
            nextClick: !1,
            mouseWheel: !0,
            autoPlay: !1,
            playSpeed: 3E3,
            preload: 3,
            modal: !1,
            loop: !0,
            ajax: {
                dataType: "html",
                headers: {
                    "X-fancyBox": !0
                }
            },
            iframe: {
                scrolling: "auto",
                preload: !0
            },
            swf: {
                wmode: "transparent",
                allowfullscreen: "true",
                allowscriptaccess: "always"
            },
            keys: {
                next: {
                    13 : "left",
                    34 : "up",
                    39 : "left",
                    40 : "up"
                },
                prev: {
                    8 : "right",
                    33 : "down",
                    37 : "right",
                    38 : "down"
                },
                close: [27],
                play: [32],
                toggle: [70]
            },
            direction: {
                next: "left",
                prev: "right"
            },
            scrollOutside: !0,
            index: 0,
            type: null,
            href: null,
            content: null,
            title: null,
            tpl: {
                wrap: '\x3cdiv class\x3d"fancybox-wrap" tabIndex\x3d"-1"\x3e\x3cdiv class\x3d"fancybox-skin"\x3e\x3cdiv class\x3d"fancybox-outer"\x3e\x3cdiv class\x3d"fancybox-inner"\x3e\x3c/div\x3e\x3c/div\x3e\x3c/div\x3e\x3c/div\x3e',
                image: '\x3cimg class\x3d"fancybox-image" src\x3d"{href}" alt\x3d"" /\x3e',
                iframe: '\x3ciframe id\x3d"fancybox-frame{rnd}" name\x3d"fancybox-frame{rnd}" class\x3d"fancybox-iframe" frameborder\x3d"0" vspace\x3d"0" hspace\x3d"0" webkitAllowFullScreen mozallowfullscreen allowFullScreen' + (I ? ' allowtransparency\x3d"true"': "") + "\x3e\x3c/iframe\x3e",
                error: '\x3cp class\x3d"fancybox-error"\x3eThe requested content cannot be loaded.\x3cbr/\x3ePlease try again later.\x3c/p\x3e',
                closeBtn: '\x3ca title\x3d"Close" class\x3d"fancybox-item fancybox-close" href\x3d"javascript:;"\x3e\x3c/a\x3e',
                next: '\x3ca title\x3d"Next" class\x3d"fancybox-nav fancybox-next" href\x3d"javascript:;"\x3e\x3cspan\x3e\x3c/span\x3e\x3c/a\x3e',
                prev: '\x3ca title\x3d"Previous" class\x3d"fancybox-nav fancybox-prev" href\x3d"javascript:;"\x3e\x3cspan\x3e\x3c/span\x3e\x3c/a\x3e'
            },
            openEffect: "fade",
            openSpeed: 250,
            openEasing: "swing",
            openOpacity: !0,
            openMethod: "zoomIn",
            closeEffect: "fade",
            closeSpeed: 250,
            closeEasing: "swing",
            closeOpacity: !0,
            closeMethod: "zoomOut",
            nextEffect: "elastic",
            nextSpeed: 250,
            nextEasing: "swing",
            nextMethod: "changeIn",
            prevEffect: "elastic",
            prevSpeed: 250,
            prevEasing: "swing",
            prevMethod: "changeOut",
            helpers: {
                overlay: !0,
                title: !0
            },
            onCancel: f.noop,
            beforeLoad: f.noop,
            afterLoad: f.noop,
            beforeShow: f.noop,
            afterShow: f.noop,
            beforeChange: f.noop,
            beforeClose: f.noop,
            afterClose: f.noop
        },
        group: {},
        opts: {},
        previous: null,
        coming: null,
        current: null,
        isActive: !1,
        isOpen: !1,
        isOpened: !1,
        wrap: null,
        skin: null,
        outer: null,
        inner: null,
        player: {
            timer: null,
            isActive: !1
        },
        ajaxLoad: null,
        imgPreload: null,
        transitions: {},
        helpers: {},
        open: function(a, d) {
            if (a && (f.isPlainObject(d) || (d = {}), !1 !== b.close(!0))) return f.isArray(a) || (a = y(a) ? f(a).get() : [a]),
            f.each(a,
            function(e, c) {
                var l = {},
                g, k, h, n, m;
                "object" === f.type(c) && (c.nodeType && (c = f(c)), y(c) ? (l = {
                    href: c.data("fancybox-href") || c.attr("href"),
                    title: c.data("fancybox-title") || c.attr("title"),
                    type: c.data("fancybox-type") || c.attr("type"),
                    isDom: !0,
                    element: c
                },
                f.metadata && f.extend(!0, l, c.metadata())) : l = c);
                g = d.href || l.href || (r(c) ? c: null);
                k = void 0 !== d.title ? d.title: l.title || "";
                n = (h = d.content || l.content) ? "html": d.type || l.type; ! n && l.isDom && (n = c.data("fancybox-type"), n || (n = (n = c.prop("class").match(/fancybox\.(\w+)/)) ? n[1] : null));
                r(g) && (n || (b.isImage(g) ? n = "image": b.isSWF(g) ? n = "swf": "#" === g.charAt(0) ? n = "inline": r(c) && (n = "html", h = c)), "ajax" === n && (m = g.split(/\s+/, 2), g = m.shift(), m = m.shift()));
                h || ("inline" === n ? g ? h = f(r(g) ? g.replace(/.*(?=#[^\s]+$)/, "") : g) : l.isDom && (h = c) : "html" === n ? h = g: n || g || !l.isDom || (n = "inline", h = c));
                f.extend(l, {
                    href: g,
                    type: n,
                    content: h,
                    title: k,
                    selector: m
                });
                a[e] = l
            }),
            b.opts = f.extend(!0, {},
            b.defaults, d),
            void 0 !== d.keys && (b.opts.keys = d.keys ? f.extend({},
            b.defaults.keys, d.keys) : !1),
            b.group = a,
            b._start(b.opts.index)
        },
        cancel: function() {
            var a = b.coming;
            a && !1 !== b.trigger("onCancel") && (b.hideLoading(), b.ajaxLoad && b.ajaxLoad.abort(), b.ajaxLoad = null, b.imgPreload && (b.imgPreload.onload = b.imgPreload.onerror = null), a.wrap && a.wrap.stop(!0, !0).trigger("onReset").remove(), b.coming = null, b.current || b._afterZoomOut(a))
        },
        close: function(a) {
            b.cancel(); ! 1 !== b.trigger("beforeClose") && (b.unbindEvents(), b.isActive && (b.isOpen && !0 !== a ? (b.isOpen = b.isOpened = !1, b.isClosing = !0, f(".fancybox-item, .fancybox-nav").remove(), b.wrap.stop(!0, !0).removeClass("fancybox-opened"), b.transitions[b.current.closeMethod]()) : (f(".fancybox-wrap").stop(!0).trigger("onReset").remove(), b._afterZoomOut())))
        },
        play: function(a) {
            var d = function() {
                clearTimeout(b.player.timer)
            },
            e = function() {
                d();
                b.current && b.player.isActive && (b.player.timer = setTimeout(b.next, b.current.playSpeed))
            },
            c = function() {
                d();
                q.unbind(".player");
                b.player.isActive = !1;
                b.trigger("onPlayEnd")
            }; ! 0 === a || !b.player.isActive && !1 !== a ? b.current && (b.current.loop || b.current.index < b.group.length - 1) && (b.player.isActive = !0, q.bind({
                "onCancel.player beforeClose.player": c,
                "onUpdate.player": e,
                "beforeLoad.player": d
            }), e(), b.trigger("onPlayStart")) : c()
        },
        next: function(a) {
            var d = b.current;
            d && (r(a) || (a = d.direction.next), b.jumpto(d.index + 1, a, "next"))
        },
        prev: function(a) {
            var d = b.current;
            d && (r(a) || (a = d.direction.prev), b.jumpto(d.index - 1, a, "prev"))
        },
        jumpto: function(a, d, e) {
            var c = b.current;
            c && (a = m(a), b.direction = d || c.direction[a >= c.index ? "next": "prev"], b.router = e || "jumpto", c.loop && (0 > a && (a = c.group.length + a % c.group.length), a %= c.group.length), void 0 !== c.group[a] && (b.cancel(), b._start(a)))
        },
        reposition: function(a, d) {
            var e = b.current,
            c = e ? e.wrap: null,
            l;
            c && (l = b._getPosition(d), a && "scroll" === a.type ? (delete l.position, c.stop(!0, !0).animate(l, 200)) : (c.css(l), e.pos = f.extend({},
            e.dim, l)))
        },
        update: function(a) {
            var d = a && a.type,
            e = !d || "orientationchange" === d;
            e && (clearTimeout(v), v = null);
            b.isOpen && !v && (v = setTimeout(function() {
                var c = b.current;
                c && !b.isClosing && (b.wrap.removeClass("fancybox-tmp"), (e || "load" === d || "resize" === d && c.autoResize) && b._setDimension(), "scroll" === d && c.canShrink || b.reposition(a), b.trigger("onUpdate"), v = null)
            },
            e && !t ? 0 : 300))
        },
        toggle: function(a) {
            b.isOpen && (b.current.fitToView = "boolean" === f.type(a) ? a: !b.current.fitToView, t && (b.wrap.removeAttr("style").addClass("fancybox-tmp"), b.trigger("onUpdate")), b.update())
        },
        hideLoading: function() {
            q.unbind(".loading");
            f("#fancybox-loading").remove()
        },
        showLoading: function() {
            var a, d;
            b.hideLoading();
            a = f('\x3cdiv id\x3d"fancybox-loading"\x3e\x3cdiv\x3e\x3c/div\x3e\x3c/div\x3e').click(b.cancel).appendTo("body");
            q.bind("keydown.loading",
            function(a) {
                27 === (a.which || a.keyCode) && (a.preventDefault(), b.cancel())
            });
            b.defaults.fixed || (d = b.getViewport(), a.css({
                position: "absolute",
                top: .5 * d.h + d.y,
                left: .5 * d.w + d.x
            }))
        },
        getViewport: function() {
            var a = b.current && b.current.locked || !1,
            d = {
                x: p.scrollLeft(),
                y: p.scrollTop()
            };
            a ? (d.w = a[0].clientWidth, d.h = a[0].clientHeight) : (d.w = t && s.innerWidth ? s.innerWidth: p.width(), d.h = t && s.innerHeight ? s.innerHeight: p.height());
            return d
        },
        unbindEvents: function() {
            b.wrap && y(b.wrap) && b.wrap.unbind(".fb");
            q.unbind(".fb");
            p.unbind(".fb")
        },
        bindEvents: function() {
            var a = b.current,
            d;
            a && (p.bind("orientationchange.fb" + (t ? "": " resize.fb") + (a.autoCenter && !a.locked ? " scroll.fb": ""), b.update), (d = a.keys) && q.bind("keydown.fb",
            function(e) {
                var c = e.which || e.keyCode,
                l = e.target || e.srcElement;
                if (27 === c && b.coming) return ! 1;
                e.ctrlKey || e.altKey || e.shiftKey || e.metaKey || l && (l.type || f(l).is("[contenteditable]")) || f.each(d,
                function(d, l) {
                    if (1 < a.group.length && void 0 !== l[c]) return b[d](l[c]),
                    e.preventDefault(),
                    !1;
                    if ( - 1 < f.inArray(c, l)) return b[d](),
                    e.preventDefault(),
                    !1
                })
            }), f.fn.mousewheel && a.mouseWheel && b.wrap.bind("mousewheel.fb",
            function(d, c, l, g) {
                for (var k = f(d.target || null), h = !1; k.length && !(h || k.is(".fancybox-skin") || k.is(".fancybox-wrap"));) h = (h = k[0]) && !(h.style.overflow && "hidden" === h.style.overflow) && (h.clientWidth && h.scrollWidth > h.clientWidth || h.clientHeight && h.scrollHeight > h.clientHeight),
                k = f(k).parent();
                0 !== c && !h && 1 < b.group.length && !a.canShrink && (0 < g || 0 < l ? b.prev(0 < g ? "down": "left") : (0 > g || 0 > l) && b.next(0 > g ? "up": "right"), d.preventDefault())
            }))
        },
        trigger: function(a, d) {
            var e, c = d || b.coming || b.current;
            if (c) {
                f.isFunction(c[a]) && (e = c[a].apply(c, Array.prototype.slice.call(arguments, 1)));
                if (!1 === e) return ! 1;
                c.helpers && f.each(c.helpers,
                function(d, e) {
                    if (e && b.helpers[d] && f.isFunction(b.helpers[d][a])) b.helpers[d][a](f.extend(!0, {},
                    b.helpers[d].defaults, e), c)
                });
                q.trigger(a)
            }
        },
        isImage: function(a) {
            return r(a) && a.match(/(^data:image\/.*,)|(\.(jp(e|g|eg)|gif|png|bmp|webp|svg)((\?|#).*)?$)/i)
        },
        isSWF: function(a) {
            return r(a) && a.match(/\.(swf)((\?|#).*)?$/i)
        },
        _start: function(a) {
            var d = {},
            e, c;
            a = m(a);
            e = b.group[a] || null;
            if (!e) return ! 1;
            d = f.extend(!0, {},
            b.opts, e);
            e = d.margin;
            c = d.padding;
            "number" === f.type(e) && (d.margin = [e, e, e, e]);
            "number" === f.type(c) && (d.padding = [c, c, c, c]);
            d.modal && f.extend(!0, d, {
                closeBtn: !1,
                closeClick: !1,
                nextClick: !1,
                arrows: !1,
                mouseWheel: !1,
                keys: null,
                helpers: {
                    overlay: {
                        closeClick: !1
                    }
                }
            });
            d.autoSize && (d.autoWidth = d.autoHeight = !0);
            "auto" === d.width && (d.autoWidth = !0);
            "auto" === d.height && (d.autoHeight = !0);
            d.group = b.group;
            d.index = a;
            b.coming = d;
            if (!1 === b.trigger("beforeLoad")) b.coming = null;
            else {
                c = d.type;
                e = d.href;
                if (!c) return b.coming = null,
                b.current && b.router && "jumpto" !== b.router ? (b.current.index = a, b[b.router](b.direction)) : !1;
                b.isActive = !0;
                if ("image" === c || "swf" === c) d.autoHeight = d.autoWidth = !1,
                d.scrolling = "visible";
                "image" === c && (d.aspectRatio = !0);
                "iframe" === c && t && (d.scrolling = "scroll");
                d.wrap = f(d.tpl.wrap).addClass("fancybox-" + (t ? "mobile": "desktop") + " fancybox-type-" + c + " fancybox-tmp " + d.wrapCSS).appendTo(d.parent || "body");
                f.extend(d, {
                    skin: f(".fancybox-skin", d.wrap),
                    outer: f(".fancybox-outer", d.wrap),
                    inner: f(".fancybox-inner", d.wrap)
                });
                f.each(["Top", "Right", "Bottom", "Left"],
                function(a, b) {
                    d.skin.css("padding" + b, w(d.padding[a]))
                });
                b.trigger("onReady");
                if ("inline" === c || "html" === c) {
                    if (!d.content || !d.content.length) return b._error("content")
                } else if (!e) return b._error("href");
                "image" === c ? b._loadImage() : "ajax" === c ? b._loadAjax() : "iframe" === c ? b._loadIframe() : b._afterLoad()
            }
        },
        _error: function(a) {
            f.extend(b.coming, {
                type: "html",
                autoWidth: !0,
                autoHeight: !0,
                minWidth: 0,
                minHeight: 0,
                scrolling: "no",
                hasError: a,
                content: b.coming.tpl.error
            });
            b._afterLoad()
        },
        _loadImage: function() {
            var a = b.imgPreload = new Image;
            a.onload = function() {
                this.onload = this.onerror = null;
                b.coming.width = this.width / b.opts.pixelRatio;
                b.coming.height = this.height / b.opts.pixelRatio;
                b._afterLoad()
            };
            a.onerror = function() {
                this.onload = this.onerror = null;
                b._error("image")
            };
            a.src = b.coming.href; ! 0 !== a.complete && b.showLoading()
        },
        _loadAjax: function() {
            var a = b.coming;
            b.showLoading();
            b.ajaxLoad = f.ajax(f.extend({},
            a.ajax, {
                url: a.href,
                error: function(a, e) {
                    b.coming && "abort" !== e ? b._error("ajax", a) : b.hideLoading()
                },
                success: function(d, e) {
                    "success" === e && (a.content = d, b._afterLoad())
                }
            }))
        },
        _loadIframe: function() {
            var a = b.coming,
            d = f(a.tpl.iframe.replace(/\{rnd\}/g, (new Date).getTime())).attr("scrolling", t ? "auto": a.iframe.scrolling).attr("src", a.href);
            f(a.wrap).bind("onReset",
            function() {
                try {
                    f(this).find("iframe").hide().attr("src", "//about:blank").end().empty()
                } catch(a) {}
            });
            a.iframe.preload && (b.showLoading(), d.one("load",
            function() {
                f(this).data("ready", 1);
                t || f(this).bind("load.fb", b.update);
                f(this).parents(".fancybox-wrap").width("100%").removeClass("fancybox-tmp").show();
                b._afterLoad()
            }));
            a.content = d.appendTo(a.inner);
            a.iframe.preload || b._afterLoad()
        },
        _preloadImages: function() {
            var a = b.group,
            d = b.current,
            e = a.length,
            c = d.preload ? Math.min(d.preload, e - 1) : 0,
            f,
            g;
            for (g = 1; g <= c; g += 1) f = a[(d.index + g) % e],
            "image" === f.type && f.href && ((new Image).src = f.href)
        },
        _afterLoad: function() {
            var a = b.coming,
            d = b.current,
            e, c, l, g, k;
            b.hideLoading();
            if (a && !1 !== b.isActive) if (!1 === b.trigger("afterLoad", a, d)) a.wrap.stop(!0).trigger("onReset").remove(),
            b.coming = null;
            else {
                d && (b.trigger("beforeChange", d), d.wrap.stop(!0).removeClass("fancybox-opened").find(".fancybox-item, .fancybox-nav").remove());
                b.unbindEvents();
                e = a.content;
                c = a.type;
                l = a.scrolling;
                f.extend(b, {
                    wrap: a.wrap,
                    skin: a.skin,
                    outer: a.outer,
                    inner: a.inner,
                    current: a,
                    previous: d
                });
                g = a.href;
                switch (c) {
                case "inline":
                case "ajax":
                case "html":
                    a.selector ? e = f("\x3cdiv\x3e").html(e).find(a.selector) : y(e) && (e.data("fancybox-placeholder") || e.data("fancybox-placeholder", f('\x3cdiv class\x3d"fancybox-placeholder"\x3e\x3c/div\x3e').insertAfter(e).hide()), e = e.show().detach(), a.wrap.bind("onReset",
                    function() {
                        f(this).find(e).length && e.hide().replaceAll(e.data("fancybox-placeholder")).data("fancybox-placeholder", !1)
                    }));
                    break;
                case "image":
                    e = a.tpl.image.replace("{href}", g);
                    break;
                case "swf":
                    e = '\x3cobject id\x3d"fancybox-swf" classid\x3d"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width\x3d"100%" height\x3d"100%"\x3e\x3cparam name\x3d"movie" value\x3d"' + g + '"\x3e\x3c/param\x3e',
                    k = "",
                    f.each(a.swf,
                    function(a, b) {
                        e += '\x3cparam name\x3d"' + a + '" value\x3d"' + b + '"\x3e\x3c/param\x3e';
                        k += " " + a + '\x3d"' + b + '"'
                    }),
                    e += '\x3cembed src\x3d"' + g + '" type\x3d"application/x-shockwave-flash" width\x3d"100%" height\x3d"100%"' + k + "\x3e\x3c/embed\x3e\x3c/object\x3e"
                }
                y(e) && e.parent().is(a.inner) || a.inner.append(e);
                b.trigger("beforeShow");
                a.inner.css("overflow", "yes" === l ? "scroll": "no" === l ? "hidden": l);
                b._setDimension();
                b.reposition();
                b.isOpen = !1;
                b.coming = null;
                b.bindEvents();
                if (!b.isOpened) f(".fancybox-wrap").not(a.wrap).stop(!0).trigger("onReset").remove();
                else if (d.prevMethod) b.transitions[d.prevMethod]();
                b.transitions[b.isOpened ? a.nextMethod: a.openMethod]();
                b._preloadImages()
            }
        },
        _setDimension: function() {
            var a = b.getViewport(),
            d = 0,
            e = !1,
            c = !1,
            e = b.wrap,
            l = b.skin,
            g = b.inner,
            k = b.current,
            c = k.width,
            h = k.height,
            n = k.minWidth,
            u = k.minHeight,
            p = k.maxWidth,
            q = k.maxHeight,
            t = k.scrolling,
            r = k.scrollOutside ? k.scrollbarWidth: 0,
            x = k.margin,
            z = m(x[1] + x[3]),
            s = m(x[0] + x[2]),
            y,
            A,
            v,
            C,
            B,
            G,
            D,
            E,
            H;
            e.add(l).add(g).width("auto").height("auto").removeClass("fancybox-tmp");
            x = m(l.outerWidth(!0) - l.width());
            y = m(l.outerHeight(!0) - l.height());
            A = z + x;
            v = s + y;
            C = F(c) ? (a.w - A) * m(c) / 100 : c;
            B = F(h) ? (a.h - v) * m(h) / 100 : h;
            if ("iframe" === k.type) {
                if (H = k.content, k.autoHeight && 1 === H.data("ready")) try {
                    H[0].contentWindow.document.location && (g.width(C).height(9999), G = H.contents().find("body"), r && G.css("overflow-x", "hidden"), B = G.outerHeight(!0))
                } catch(I) {}
            } else if (k.autoWidth || k.autoHeight) g.addClass("fancybox-tmp"),
            k.autoWidth || g.width(C),
            k.autoHeight || g.height(B),
            k.autoWidth && (C = g.width()),
            k.autoHeight && (B = g.height()),
            g.removeClass("fancybox-tmp");
            c = m(C);
            h = m(B);
            E = C / B;
            n = m(F(n) ? m(n, "w") - A: n);
            p = m(F(p) ? m(p, "w") - A: p);
            u = m(F(u) ? m(u, "h") - v: u);
            q = m(F(q) ? m(q, "h") - v: q);
            G = p;
            D = q;
            k.fitToView && (p = Math.min(a.w - A, p), q = Math.min(a.h - v, q));
            A = a.w - z;
            s = a.h - s;
            k.aspectRatio ? (c > p && (c = p, h = m(c / E)), h > q && (h = q, c = m(h * E)), c < n && (c = n, h = m(c / E)), h < u && (h = u, c = m(h * E))) : (c = Math.max(n, Math.min(c, p)), k.autoHeight && "iframe" !== k.type && (g.width(c), h = g.height()), h = Math.max(u, Math.min(h, q)));
            if (k.fitToView) if (g.width(c).height(h), e.width(c + x), a = e.width(), z = e.height(), k.aspectRatio) for (; (a > A || z > s) && c > n && h > u && !(19 < d++);) h = Math.max(u, Math.min(q, h - 10)),
            c = m(h * E),
            c < n && (c = n, h = m(c / E)),
            c > p && (c = p, h = m(c / E)),
            g.width(c).height(h),
            e.width(c + x),
            a = e.width(),
            z = e.height();
            else c = Math.max(n, Math.min(c, c - (a - A))),
            h = Math.max(u, Math.min(h, h - (z - s)));
            r && "auto" === t && h < B && c + x + r < A && (c += r);
            g.width(c).height(h);
            e.width(c + x);
            a = e.width();
            z = e.height();
            e = (a > A || z > s) && c > n && h > u;
            c = k.aspectRatio ? c < G && h < D && c < C && h < B: (c < G || h < D) && (c < C || h < B);
            f.extend(k, {
                dim: {
                    width: w(a),
                    height: w(z)
                },
                origWidth: C,
                origHeight: B,
                canShrink: e,
                canExpand: c,
                wPadding: x,
                hPadding: y,
                wrapSpace: z - l.outerHeight(!0),
                skinSpace: l.height() - h
            }); ! H && k.autoHeight && h > u && h < q && !c && g.height("auto")
        },
        _getPosition: function(a) {
            var d = b.current,
            e = b.getViewport(),
            c = d.margin,
            f = b.wrap.width() + c[1] + c[3],
            g = b.wrap.height() + c[0] + c[2],
            c = {
                position: "absolute",
                top: c[0],
                left: c[3]
            };
            d.autoCenter && d.fixed && !a && g <= e.h && f <= e.w ? c.position = "fixed": d.locked || (c.top += e.y, c.left += e.x);
            c.top = w(Math.max(c.top, c.top + (e.h - g) * d.topRatio));
            c.left = w(Math.max(c.left, c.left + (e.w - f) * d.leftRatio));
            return c
        },
        _afterZoomIn: function() {
            var a = b.current;
            a && ((b.isOpen = b.isOpened = !0, b.wrap.css("overflow", "visible").addClass("fancybox-opened"), b.update(), (a.closeClick || a.nextClick && 1 < b.group.length) && b.inner.css("cursor", "pointer").bind("click.fb",
            function(d) {
                f(d.target).is("a") || f(d.target).parent().is("a") || (d.preventDefault(), b[a.closeClick ? "close": "next"]())
            }), a.closeBtn && f(a.tpl.closeBtn).appendTo(b.skin).bind("click.fb",
            function(a) {
                a.preventDefault();
                b.close()
            }), a.arrows && 1 < b.group.length && ((a.loop || 0 < a.index) && f(a.tpl.prev).appendTo(b.outer).bind("click.fb", b.prev), (a.loop || a.index < b.group.length - 1) && f(a.tpl.next).appendTo(b.outer).bind("click.fb", b.next)), b.trigger("afterShow"), a.loop || a.index !== a.group.length - 1) ? b.opts.autoPlay && !b.player.isActive && (b.opts.autoPlay = !1, b.play()) : b.play(!1))
        },
        _afterZoomOut: function(a) {
            a = a || b.current;
            f(".fancybox-wrap").trigger("onReset").remove();
            f.extend(b, {
                group: {},
                opts: {},
                router: !1,
                current: null,
                isActive: !1,
                isOpened: !1,
                isOpen: !1,
                isClosing: !1,
                wrap: null,
                skin: null,
                outer: null,
                inner: null
            });
            b.trigger("afterClose", a)
        }
    });
    b.transitions = {
        getOrigPosition: function() {
            var a = b.current,
            d = a.element,
            e = a.orig,
            c = {},
            f = 50,
            g = 50,
            k = a.hPadding,
            h = a.wPadding,
            n = b.getViewport(); ! e && a.isDom && d.is(":visible") && (e = d.find("img:first"), e.length || (e = d));
            y(e) ? (c = e.offset(), e.is("img") && (f = e.outerWidth(), g = e.outerHeight())) : (c.top = n.y + (n.h - g) * a.topRatio, c.left = n.x + (n.w - f) * a.leftRatio);
            if ("fixed" === b.wrap.css("position") || a.locked) c.top -= n.y,
            c.left -= n.x;
            return c = {
                top: w(c.top - k * a.topRatio),
                left: w(c.left - h * a.leftRatio),
                width: w(f + h),
                height: w(g + k)
            }
        },
        step: function(a, d) {
            var e, c, f = d.prop;
            c = b.current;
            var g = c.wrapSpace,
            k = c.skinSpace;
            if ("width" === f || "height" === f) e = d.end === d.start ? 1 : (a - d.start) / (d.end - d.start),
            b.isClosing && (e = 1 - e),
            c = "width" === f ? c.wPadding: c.hPadding,
            c = a - c,
            b.skin[f](m("width" === f ? c: c - g * e)),
            b.inner[f](m("width" === f ? c: c - g * e - k * e))
        },
        zoomIn: function() {
            var a = b.current,
            d = a.pos,
            e = a.openEffect,
            c = "elastic" === e,
            l = f.extend({
                opacity: 1
            },
            d);
            delete l.position;
            c ? (d = this.getOrigPosition(), a.openOpacity && (d.opacity = .1)) : "fade" === e && (d.opacity = .1);
            b.wrap.css(d).animate(l, {
                duration: "none" === e ? 0 : a.openSpeed,
                easing: a.openEasing,
                step: c ? this.step: null,
                complete: b._afterZoomIn
            })
        },
        zoomOut: function() {
            var a = b.current,
            d = a.closeEffect,
            e = "elastic" === d,
            c = {
                opacity: .1
            };
            e && (c = this.getOrigPosition(), a.closeOpacity && (c.opacity = .1));
            b.wrap.animate(c, {
                duration: "none" === d ? 0 : a.closeSpeed,
                easing: a.closeEasing,
                step: e ? this.step: null,
                complete: b._afterZoomOut
            })
        },
        changeIn: function() {
            var a = b.current,
            d = a.nextEffect,
            e = a.pos,
            c = {
                opacity: 1
            },
            f = b.direction,
            g;
            e.opacity = .1;
            "elastic" === d && (g = "down" === f || "up" === f ? "top": "left", "down" === f || "right" === f ? (e[g] = w(m(e[g]) - 200), c[g] = "+\x3d200px") : (e[g] = w(m(e[g]) + 200), c[g] = "-\x3d200px"));
            "none" === d ? b._afterZoomIn() : b.wrap.css(e).animate(c, {
                duration: a.nextSpeed,
                easing: a.nextEasing,
                complete: b._afterZoomIn
            })
        },
        changeOut: function() {
            var a = b.previous,
            d = a.prevEffect,
            e = {
                opacity: .1
            },
            c = b.direction;
            "elastic" === d && (e["down" === c || "up" === c ? "top": "left"] = ("up" === c || "left" === c ? "-": "+") + "\x3d200px");
            a.wrap.animate(e, {
                duration: "none" === d ? 0 : a.prevSpeed,
                easing: a.prevEasing,
                complete: function() {
                    f(this).trigger("onReset").remove()
                }
            })
        }
    };
    b.helpers.overlay = {
        defaults: {
            closeClick: !0,
            speedOut: 200,
            showEarly: !0,
            css: {},
            locked: !t,
            fixed: !0
        },
        overlay: null,
        fixed: !1,
        el: f("html"),
        create: function(a) {
            a = f.extend({},
            this.defaults, a);
            this.overlay && this.close();
            this.overlay = f('\x3cdiv class\x3d"fancybox-overlay"\x3e\x3c/div\x3e').appendTo(b.coming ? b.coming.parent: a.parent);
            this.fixed = !1;
            a.fixed && b.defaults.fixed && (this.overlay.addClass("fancybox-overlay-fixed"), this.fixed = !0)
        },
        open: function(a) {
            var d = this;
            a = f.extend({},
            this.defaults, a);
            this.overlay ? this.overlay.unbind(".overlay").width("auto").height("auto") : this.create(a);
            this.fixed || (p.bind("resize.overlay", f.proxy(this.update, this)), this.update());
            a.closeClick && this.overlay.bind("click.overlay",
            function(a) {
                if (f(a.target).hasClass("fancybox-overlay")) return b.isActive ? b.close() : d.close(),
                !1
            });
            this.overlay.css(a.css).show()
        },
        close: function() {
            var a, b;
            p.unbind("resize.overlay");
            this.el.hasClass("fancybox-lock") && (f(".fancybox-margin").removeClass("fancybox-margin"), a = p.scrollTop(), b = p.scrollLeft(), this.el.removeClass("fancybox-lock"), p.scrollTop(a).scrollLeft(b));
            f(".fancybox-overlay").remove().hide();
            f.extend(this, {
                overlay: null,
                fixed: !1
            })
        },
        update: function() {
            var a = "100%",
            b;
            this.overlay.width(a).height("100%");
            I ? (b = Math.max(D.documentElement.offsetWidth, D.body.offsetWidth), q.width() > b && (a = q.width())) : q.width() > p.width() && (a = q.width());
            this.overlay.width(a).height(q.height())
        },
        onReady: function(a, b) {
            var e = this.overlay;
            f(".fancybox-overlay").stop(!0, !0);
            e || this.create(a);
            a.locked && this.fixed && b.fixed && (e || (this.margin = q.height() > p.height() ? f("html").css("margin-right").replace("px", "") : !1), b.locked = this.overlay.append(b.wrap), b.fixed = !1); ! 0 === a.showEarly && this.beforeShow.apply(this, arguments)
        },
        beforeShow: function(a, b) {
            var e, c;
            b.locked && (!1 !== this.margin && (f("*").filter(function() {
                return "fixed" === f(this).css("position") && !f(this).hasClass("fancybox-overlay") && !f(this).hasClass("fancybox-wrap")
            }).addClass("fancybox-margin"), this.el.addClass("fancybox-margin")), e = p.scrollTop(), c = p.scrollLeft(), this.el.addClass("fancybox-lock"), p.scrollTop(e).scrollLeft(c));
            this.open(a)
        },
        onUpdate: function() {
            this.fixed || this.update()
        },
        afterClose: function(a) {
            this.overlay && !b.coming && this.overlay.fadeOut(a.speedOut, f.proxy(this.close, this))
        }
    };
    b.helpers.title = {
        defaults: {
            type: "float",
            position: "bottom"
        },
        beforeShow: function(a) {
            var d = b.current,
            e = d.title,
            c = a.type;
            f.isFunction(e) && (e = e.call(d.element, d));
            if (r(e) && "" !== f.trim(e)) {
                d = f('\x3cdiv class\x3d"fancybox-title fancybox-title-' + c + '-wrap"\x3e' + e + "\x3c/div\x3e");
                switch (c) {
                case "inside":
                    c = b.skin;
                    break;
                case "outside":
                    c = b.wrap;
                    break;
                case "over":
                    c = b.inner;
                    break;
                default:
                    c = b.skin,
                    d.appendTo("body"),
                    I && d.width(d.width()),
                    d.wrapInner('\x3cspan class\x3d"child"\x3e\x3c/span\x3e'),
                    b.current.margin[2] += Math.abs(m(d.css("margin-bottom")))
                }
                d["top" === a.position ? "prependTo": "appendTo"](c)
            }
        }
    };
    f.fn.fancybox = function(a) {
        var d, e = f(this),
        c = this.selector || "",
        l = function(g) {
            var k = f(this).blur(),
            h = d,
            l,
            m;
            g.ctrlKey || g.altKey || g.shiftKey || g.metaKey || k.is(".fancybox-wrap") || (l = a.groupAttr || "data-fancybox-group", m = k.attr(l), m || (l = "rel", m = k.get(0)[l]), m && "" !== m && "nofollow" !== m && (k = c.length ? f(c) : e, k = k.filter("[" + l + '\x3d"' + m + '"]'), h = k.index(this)), a.index = h, !1 !== b.open(k, a) && g.preventDefault())
        };
        a = a || {};
        d = a.index || 0;
        c && !1 !== a.live ? q.undelegate(c, "click.fb-start").delegate(c + ":not('.fancybox-item, .fancybox-nav')", "click.fb-start", l) : e.unbind("click.fb-start").bind("click.fb-start", l);
        this.filter("[data-fancybox-start\x3d1]").trigger("click");
        return this
    };
    q.ready(function() {
        var a, d;
        void 0 === f.scrollbarWidth && (f.scrollbarWidth = function() {
            var a = f('\x3cdiv style\x3d"width:50px;height:50px;overflow:auto"\x3e\x3cdiv/\x3e\x3c/div\x3e').appendTo("body"),
            b = a.children(),
            b = b.innerWidth() - b.height(99).innerWidth();
            a.remove();
            return b
        });
        void 0 === f.support.fixedPosition && (f.support.fixedPosition = function() {
            var a = f('\x3cdiv style\x3d"position:fixed;top:20px;"\x3e\x3c/div\x3e').appendTo("body"),
            b = 20 === a[0].offsetTop || 15 === a[0].offsetTop;
            a.remove();
            return b
        } ());
        f.extend(b.defaults, {
            scrollbarWidth: f.scrollbarWidth(),
            fixed: f.support.fixedPosition,
            parent: f("body")
        });
        a = f(s).width();
        J.addClass("fancybox-lock-test");
        d = f(s).width();
        J.removeClass("fancybox-lock-test");
        f("\x3cstyle type\x3d'text/css'\x3e.fancybox-margin{margin-right:" + (d - a) + "px;}\x3c/style\x3e").appendTo("head")
    })
})(window, document, jQuery);
/* Tooltipster v3.2.1 */
; (function(e, t, n) {
    function s(t, n) {
        this.bodyOverflowX;
        this.callbacks = {
            hide: [],
            show: []
        };
        this.checkInterval = null;
        this.Content;
        this.$el = e(t);
        this.$elProxy;
        this.elProxyPosition;
        this.enabled = true;
        this.options = e.extend({},
        i, n);
        this.mouseIsOverProxy = false;
        this.namespace = "tooltipster-" + Math.round(Math.random() * 1e5);
        this.Status = "hidden";
        this.timerHide = null;
        this.timerShow = null;
        this.$tooltip;
        this.options.iconTheme = this.options.iconTheme.replace(".", "");
        this.options.theme = this.options.theme.replace(".", "");
        this._init()
    }
    function o(t, n) {
        var r = true;
        e.each(t,
        function(e, i) {
            if (typeof n[e] === "undefined" || t[e] !== n[e]) {
                r = false;
                return false
            }
        });
        return r
    }
    function f() {
        return ! a && u
    }
    function l() {
        var e = n.body || n.documentElement,
        t = e.style,
        r = "transition";
        if (typeof t[r] == "string") {
            return true
        }
        v = ["Moz", "Webkit", "Khtml", "O", "ms"],
        r = r.charAt(0).toUpperCase() + r.substr(1);
        for (var i = 0; i < v.length; i++) {
            if (typeof t[v[i] + r] == "string") {
                return true
            }
        }
        return false
    }
    var r = "tooltipster",
    i = {
        animation: "fade",
        arrow: true,
        arrowColor: "",
        autoClose: true,
        content: null,
        contentAsHTML: false,
        contentCloning: true,
        delay: 200,
        fixedWidth: 0,
        maxWidth: 0,
        functionInit: function(e, t) {},
        functionBefore: function(e, t) {
            t()
        },
        functionReady: function(e, t) {},
        functionAfter: function(e) {},
        icon: "(?)",
        iconCloning: true,
        iconDesktop: false,
        iconTouch: false,
        iconTheme: "tooltipster-icon",
        interactive: false,
        interactiveTolerance: 350,
        multiple: false,
        offsetX: 0,
        offsetY: 0,
        onlyOne: false,
        position: "top",
        positionTracker: false,
        speed: 350,
        timer: 0,
        theme: "tooltipster-default",
        touchDevices: true,
        trigger: "hover",
        updateAnimation: true
    };
    s.prototype = {
        _init: function() {
            var t = this;
            if (n.querySelector) {
                if (t.options.content !== null) {
                    t._content_set(t.options.content)
                } else {
                    var r = t.$el.attr("title");
                    if (typeof r === "undefined") r = null;
                    t._content_set(r)
                }
                var i = t.options.functionInit.call(t.$el, t.$el, t.Content);
                if (typeof i !== "undefined") t._content_set(i);
                t.$el.removeAttr("title").addClass("tooltipstered");
                if (!u && t.options.iconDesktop || u && t.options.iconTouch) {
                    if (typeof t.options.icon === "string") {
                        t.$elProxy = e('<span class="' + t.options.iconTheme + '"></span>');
                        t.$elProxy.text(t.options.icon)
                    } else {
                        if (t.options.iconCloning) t.$elProxy = t.options.icon.clone(true);
                        else t.$elProxy = t.options.icon
                    }
                    t.$elProxy.insertAfter(t.$el)
                } else {
                    t.$elProxy = t.$el
                }
                if (t.options.trigger == "hover") {
                    t.$elProxy.on("mouseenter." + t.namespace,
                    function() {
                        if (!f() || t.options.touchDevices) {
                            t.mouseIsOverProxy = true;
                            t._show()
                        }
                    }).on("mouseleave." + t.namespace,
                    function() {
                        if (!f() || t.options.touchDevices) {
                            t.mouseIsOverProxy = false
                        }
                    });
                    if (u && t.options.touchDevices) {
                        t.$elProxy.on("touchstart." + t.namespace,
                        function() {
                            t._showNow()
                        })
                    }
                } else if (t.options.trigger == "click") {
                    t.$elProxy.on("click." + t.namespace,
                    function() {
                        if (!f() || t.options.touchDevices) {
                            t._show()
                        }
                    })
                }
            }
        },
        _show: function() {
            var e = this;
            if (e.Status != "shown" && e.Status != "appearing") {
                if (e.options.delay) {
                    e.timerShow = setTimeout(function() {
                        if (e.options.trigger == "click" || e.options.trigger == "hover" && e.mouseIsOverProxy) {
                            e._showNow()
                        }
                    },
                    e.options.delay)
                } else e._showNow()
            }
        },
        _showNow: function(n) {
            var r = this;
            r.options.functionBefore.call(r.$el, r.$el,
            function() {
                if (r.enabled && r.Content !== null) {
                    if (n) r.callbacks.show.push(n);
                    r.callbacks.hide = [];
                    clearTimeout(r.timerShow);
                    r.timerShow = null;
                    clearTimeout(r.timerHide);
                    r.timerHide = null;
                    if (r.options.onlyOne) {
                        e(".tooltipstered").not(r.$el).each(function(t, n) {
                            var r = e(n),
                            i = r.data("tooltipster-ns");
                            e.each(i,
                            function(e, t) {
                                var n = r.data(t),
                                i = n.status(),
                                s = n.option("autoClose");
                                if (i !== "hidden" && i !== "disappearing" && s) {
                                    n.hide()
                                }
                            })
                        })
                    }
                    var i = function() {
                        r.Status = "shown";
                        e.each(r.callbacks.show,
                        function(e, t) {
                            t.call(r.$el)
                        });
                        r.callbacks.show = []
                    };
                    if (r.Status !== "hidden") {
                        var s = 0;
                        if (r.Status === "disappearing") {
                            r.Status = "appearing";
                            if (l()) {
                                r.$tooltip.clearQueue().removeClass("tooltipster-dying").addClass("tooltipster-" + r.options.animation + "-show");
                                if (r.options.speed > 0) r.$tooltip.delay(r.options.speed);
                                r.$tooltip.queue(i)
                            } else {
                                r.$tooltip.stop().fadeIn(i)
                            }
                        } else if (r.Status === "shown") {
                            i()
                        }
                    } else {
                        r.Status = "appearing";
                        var s = r.options.speed;
                        r.bodyOverflowX = e("body").css("overflow-x");
                        e("body").css("overflow-x", "hidden");
                        var o = "tooltipster-" + r.options.animation,
                        a = "-webkit-transition-duration: " + r.options.speed + "ms; -webkit-animation-duration: " + r.options.speed + "ms; -moz-transition-duration: " + r.options.speed + "ms; -moz-animation-duration: " + r.options.speed + "ms; -o-transition-duration: " + r.options.speed + "ms; -o-animation-duration: " + r.options.speed + "ms; -ms-transition-duration: " + r.options.speed + "ms; -ms-animation-duration: " + r.options.speed + "ms; transition-duration: " + r.options.speed + "ms; animation-duration: " + r.options.speed + "ms;",
                        f = r.options.fixedWidth > 0 ? "width:" + Math.round(r.options.fixedWidth) + "px;": "",
                        c = r.options.maxWidth > 0 ? "max-width:" + Math.round(r.options.maxWidth) + "px;": "",
                        h = r.options.interactive ? "pointer-events: auto;": "";
                        r.$tooltip = e('<div class="tooltipster-base ' + r.options.theme + '" style="' + f + " " + c + " " + h + " " + a + '"><div class="tooltipster-content"></div></div>');
                        if (l()) r.$tooltip.addClass(o);
                        r._content_insert();
                        r.$tooltip.appendTo("body");
                        r.reposition();
                        r.options.functionReady.call(r.$el, r.$el, r.$tooltip);
                        if (l()) {
                            r.$tooltip.addClass(o + "-show");
                            if (r.options.speed > 0) r.$tooltip.delay(r.options.speed);
                            r.$tooltip.queue(i)
                        } else {
                            r.$tooltip.css("display", "none").fadeIn(r.options.speed, i)
                        }
                        r._interval_set();
                        e(t).on("scroll." + r.namespace + " resize." + r.namespace,
                        function() {
                            r.reposition()
                        });
                        if (r.options.autoClose) {
                            e("body").off("." + r.namespace);
                            if (r.options.trigger == "hover") {
                                if (u) {
                                    setTimeout(function() {
                                        e("body").on("touchstart." + r.namespace,
                                        function() {
                                            r.hide()
                                        })
                                    },
                                    0)
                                }
                                if (r.options.interactive) {
                                    if (u) {
                                        r.$tooltip.on("touchstart." + r.namespace,
                                        function(e) {
                                            e.stopPropagation()
                                        })
                                    }
                                    var p = null;
                                    r.$elProxy.add(r.$tooltip).on("mouseleave." + r.namespace + "-autoClose",
                                    function() {
                                        clearTimeout(p);
                                        p = setTimeout(function() {
                                            r.hide()
                                        },
                                        r.options.interactiveTolerance)
                                    }).on("mouseenter." + r.namespace + "-autoClose",
                                    function() {
                                        clearTimeout(p)
                                    })
                                } else {
                                    r.$elProxy.on("mouseleave." + r.namespace + "-autoClose",
                                    function() {
                                        r.hide()
                                    })
                                }
                            } else if (r.options.trigger == "click") {
                                setTimeout(function() {
                                    e("body").on("click." + r.namespace + " touchstart." + r.namespace,
                                    function() {
                                        r.hide()
                                    })
                                },
                                0);
                                if (r.options.interactive) {
                                    r.$tooltip.on("click." + r.namespace + " touchstart." + r.namespace,
                                    function(e) {
                                        e.stopPropagation()
                                    })
                                }
                            }
                        }
                    }
                    if (r.options.timer > 0) {
                        r.timerHide = setTimeout(function() {
                            r.timerHide = null;
                            r.hide()
                        },
                        r.options.timer + s)
                    }
                }
            })
        },
        _interval_set: function() {
            var t = this;
            t.checkInterval = setInterval(function() {
                if (e("body").find(t.$el).length === 0 || e("body").find(t.$elProxy).length === 0 || t.Status == "hidden" || e("body").find(t.$tooltip).length === 0) {
                    if (t.Status == "shown" || t.Status == "appearing") t.hide();
                    t._interval_cancel()
                } else {
                    if (t.options.positionTracker) {
                        var n = t._repositionInfo(t.$elProxy),
                        r = false;
                        if (o(n.dimension, t.elProxyPosition.dimension)) {
                            if (t.$elProxy.css("position") === "fixed") {
                                if (o(n.position, t.elProxyPosition.position)) r = true
                            } else {
                                if (o(n.offset, t.elProxyPosition.offset)) r = true
                            }
                        }
                        if (!r) {
                            t.reposition()
                        }
                    }
                }
            },
            200)
        },
        _interval_cancel: function() {
            clearInterval(this.checkInterval);
            this.checkInterval = null
        },
        _content_set: function(e) {
            if (typeof e === "object" && e !== null && this.options.contentCloning) {
                e = e.clone(true)
            }
            this.Content = e
        },
        _content_insert: function() {
            var e = this,
            t = this.$tooltip.find(".tooltipster-content");
            if (typeof e.Content === "string" && !e.options.contentAsHTML) {
                t.text(e.Content)
            } else {
                t.empty().append(e.Content)
            }
        },
        _update: function(e) {
            var t = this;
            t._content_set(e);
            if (t.Content !== null) {
                if (t.Status !== "hidden") {
                    t._content_insert();
                    t.reposition();
                    if (t.options.updateAnimation) {
                        if (l()) {
                            t.$tooltip.css({
                                width: "",
                                "-webkit-transition": "all " + t.options.speed + "ms, width 0ms, height 0ms, left 0ms, top 0ms",
                                "-moz-transition": "all " + t.options.speed + "ms, width 0ms, height 0ms, left 0ms, top 0ms",
                                "-o-transition": "all " + t.options.speed + "ms, width 0ms, height 0ms, left 0ms, top 0ms",
                                "-ms-transition": "all " + t.options.speed + "ms, width 0ms, height 0ms, left 0ms, top 0ms",
                                transition: "all " + t.options.speed + "ms, width 0ms, height 0ms, left 0ms, top 0ms"
                            }).addClass("tooltipster-content-changing");
                            setTimeout(function() {
                                if (t.Status != "hidden") {
                                    t.$tooltip.removeClass("tooltipster-content-changing");
                                    setTimeout(function() {
                                        if (t.Status !== "hidden") {
                                            t.$tooltip.css({
                                                "-webkit-transition": t.options.speed + "ms",
                                                "-moz-transition": t.options.speed + "ms",
                                                "-o-transition": t.options.speed + "ms",
                                                "-ms-transition": t.options.speed + "ms",
                                                transition: t.options.speed + "ms"
                                            })
                                        }
                                    },
                                    t.options.speed)
                                }
                            },
                            t.options.speed)
                        } else {
                            t.$tooltip.fadeTo(t.options.speed, .5,
                            function() {
                                if (t.Status != "hidden") {
                                    t.$tooltip.fadeTo(t.options.speed, 1)
                                }
                            })
                        }
                    }
                }
            } else {
                t.hide()
            }
        },
        _repositionInfo: function(e) {
            return {
                dimension: {
                    height: e.outerHeight(false),
                    width: e.outerWidth(false)
                },
                offset: e.offset(),
                position: {
                    left: parseInt(e.css("left")),
                    top: parseInt(e.css("top"))
                }
            }
        },
        hide: function(n) {
            var r = this;
            if (n) r.callbacks.hide.push(n);
            r.callbacks.show = [];
            clearTimeout(r.timerShow);
            r.timerShow = null;
            clearTimeout(r.timerHide);
            r.timerHide = null;
            var i = function() {
                e.each(r.callbacks.hide,
                function(e, t) {
                    t.call(r.$el)
                });
                r.callbacks.hide = []
            };
            if (r.Status == "shown" || r.Status == "appearing") {
                r.Status = "disappearing";
                var s = function() {
                    r.Status = "hidden";
                    if (typeof r.Content == "object" && r.Content !== null) {
                        r.Content.detach()
                    }
                    r.$tooltip.remove();
                    r.$tooltip = null;
                    e(t).off("." + r.namespace);
                    e("body").off("." + r.namespace).css("overflow-x", r.bodyOverflowX);
                    e("body").off("." + r.namespace);
                    r.$elProxy.off("." + r.namespace + "-autoClose");
                    r.options.functionAfter.call(r.$el, r.$el);
                    i()
                };
                if (l()) {
                    r.$tooltip.clearQueue().removeClass("tooltipster-" + r.options.animation + "-show").addClass("tooltipster-dying");
                    if (r.options.speed > 0) r.$tooltip.delay(r.options.speed);
                    r.$tooltip.queue(s)
                } else {
                    r.$tooltip.stop().fadeOut(r.options.speed, s)
                }
            } else if (r.Status == "hidden") {
                i()
            }
            return r
        },
        show: function(e) {
            this._showNow(e);
            return this
        },
        update: function(e) {
            return this.content(e)
        },
        content: function(e) {
            if (typeof e === "undefined") {
                return this.Content
            } else {
                this._update(e);
                return this
            }
        },
        reposition: function() {
            var n = this;
            if (e("body").find(n.$tooltip).length !== 0) {
                n.$tooltip.css("width", "");
                n.elProxyPosition = n._repositionInfo(n.$elProxy);
                var r = null,
                i = e(t).width(),
                s = n.elProxyPosition,
                o = n.$tooltip.outerWidth(false),
                u = n.$tooltip.innerWidth() + 1,
                a = n.$tooltip.outerHeight(false);
                if (n.$elProxy.is("area")) {
                    var f = n.$elProxy.attr("shape"),
                    l = n.$elProxy.parent().attr("name"),
                    c = e('img[usemap="#' + l + '"]'),
                    h = c.offset().left,
                    p = c.offset().top,
                    d = n.$elProxy.attr("coords") !== undefined ? n.$elProxy.attr("coords").split(",") : undefined;
                    if (f == "circle") {
                        var v = parseInt(d[0]),
                        m = parseInt(d[1]),
                        g = parseInt(d[2]);
                        s.dimension.height = g * 2;
                        s.dimension.width = g * 2;
                        s.offset.top = p + m - g;
                        s.offset.left = h + v - g
                    } else if (f == "rect") {
                        var v = parseInt(d[0]),
                        m = parseInt(d[1]),
                        y = parseInt(d[2]),
                        b = parseInt(d[3]);
                        s.dimension.height = b - m;
                        s.dimension.width = y - v;
                        s.offset.top = p + m;
                        s.offset.left = h + v
                    } else if (f == "poly") {
                        var w = [],
                        E = [],
                        S = 0,
                        x = 0,
                        T = 0,
                        N = 0,
                        C = "even";
                        for (var k = 0; k < d.length; k++) {
                            var L = parseInt(d[k]);
                            if (C == "even") {
                                if (L > T) {
                                    T = L;
                                    if (k === 0) {
                                        S = T
                                    }
                                }
                                if (L < S) {
                                    S = L
                                }
                                C = "odd"
                            } else {
                                if (L > N) {
                                    N = L;
                                    if (k == 1) {
                                        x = N
                                    }
                                }
                                if (L < x) {
                                    x = L
                                }
                                C = "even"
                            }
                        }
                        s.dimension.height = N - x;
                        s.dimension.width = T - S;
                        s.offset.top = p + x;
                        s.offset.left = h + S
                    } else {
                        s.dimension.height = c.outerHeight(false);
                        s.dimension.width = c.outerWidth(false);
                        s.offset.top = p;
                        s.offset.left = h
                    }
                }
                if (n.options.fixedWidth === 0) {
                    n.$tooltip.css({
                        width: Math.round(u) + "px",
                        "padding-left": "0px",
                        "padding-right": "0px"
                    })
                }
                var A = 0,
                O = 0,
                M = 0,
                _ = parseInt(n.options.offsetY),
                D = parseInt(n.options.offsetX),
                P = n.options.position;
                function H() {
                    var n = e(t).scrollLeft();
                    if (A - n < 0) {
                        r = A - n;
                        A = n
                    }
                    if (A + o - n > i) {
                        r = A - (i + n - o);
                        A = i + n - o
                    }
                }
                function B(n, r) {
                    if (s.offset.top - e(t).scrollTop() - a - _ - 12 < 0 && r.indexOf("top") > -1) {
                        P = n
                    }
                    if (s.offset.top + s.dimension.height + a + 12 + _ > e(t).scrollTop() + e(t).height() && r.indexOf("bottom") > -1) {
                        P = n;
                        M = s.offset.top - a - _ - 12
                    }
                }
                if (P == "top") {
                    var j = s.offset.left + o - (s.offset.left + s.dimension.width);
                    A = s.offset.left + D - j / 2;
                    M = s.offset.top - a - _ - 12;
                    H();
                    B("bottom", "top")
                }
                if (P == "top-left") {
                    A = s.offset.left + D;
                    M = s.offset.top - a - _ - 12;
                    H();
                    B("bottom-left", "top-left")
                }
                if (P == "top-right") {
                    A = s.offset.left + s.dimension.width + D - o;
                    M = s.offset.top - a - _ - 12;
                    H();
                    B("bottom-right", "top-right")
                }
                if (P == "bottom") {
                    var j = s.offset.left + o - (s.offset.left + s.dimension.width);
                    A = s.offset.left - j / 2 + D;
                    M = s.offset.top + s.dimension.height + _ + 12;
                    H();
                    B("top", "bottom")
                }
                if (P == "bottom-left") {
                    A = s.offset.left + D;
                    M = s.offset.top + s.dimension.height + _ + 12;
                    H();
                    B("top-left", "bottom-left")
                }
                if (P == "bottom-right") {
                    A = s.offset.left + s.dimension.width + D - o;
                    M = s.offset.top + s.dimension.height + _ + 12;
                    H();
                    B("top-right", "bottom-right")
                }
                if (P == "left") {
                    A = s.offset.left - D - o - 12;
                    O = s.offset.left + D + s.dimension.width + 12;
                    var F = s.offset.top + a - (s.offset.top + s.dimension.height);
                    M = s.offset.top - F / 2 - _;
                    if (A < 0 && O + o > i) {
                        var I = parseFloat(n.$tooltip.css("border-width")) * 2,
                        q = o + A - I;
                        n.$tooltip.css("width", q + "px");
                        a = n.$tooltip.outerHeight(false);
                        A = s.offset.left - D - q - 12 - I;
                        F = s.offset.top + a - (s.offset.top + s.dimension.height);
                        M = s.offset.top - F / 2 - _
                    } else if (A < 0) {
                        A = s.offset.left + D + s.dimension.width + 12;
                        r = "left"
                    }
                }
                if (P == "right") {
                    A = s.offset.left + D + s.dimension.width + 12;
                    O = s.offset.left - D - o - 12;
                    var F = s.offset.top + a - (s.offset.top + s.dimension.height);
                    M = s.offset.top - F / 2 - _;
                    if (A + o > i && O < 0) {
                        var I = parseFloat(n.$tooltip.css("border-width")) * 2,
                        q = i - A - I;
                        n.$tooltip.css("width", q + "px");
                        a = n.$tooltip.outerHeight(false);
                        F = s.offset.top + a - (s.offset.top + s.dimension.height);
                        M = s.offset.top - F / 2 - _
                    } else if (A + o > i) {
                        A = s.offset.left - D - o - 12;
                        r = "right"
                    }
                }
                if (n.options.arrow) {
                    var R = "tooltipster-arrow-" + P;
                    if (n.options.arrowColor.length < 1) {
                        var U = n.$tooltip.css("background-color")
                    } else {
                        var U = n.options.arrowColor
                    }
                    if (!r) {
                        r = ""
                    } else if (r == "left") {
                        R = "tooltipster-arrow-right";
                        r = ""
                    } else if (r == "right") {
                        R = "tooltipster-arrow-left";
                        r = ""
                    } else {
                        r = "left:" + Math.round(r) + "px;"
                    }
                    if (P == "top" || P == "top-left" || P == "top-right") {
                        var z = parseFloat(n.$tooltip.css("border-bottom-width")),
                        W = n.$tooltip.css("border-bottom-color")
                    } else if (P == "bottom" || P == "bottom-left" || P == "bottom-right") {
                        var z = parseFloat(n.$tooltip.css("border-top-width")),
                        W = n.$tooltip.css("border-top-color")
                    } else if (P == "left") {
                        var z = parseFloat(n.$tooltip.css("border-right-width")),
                        W = n.$tooltip.css("border-right-color")
                    } else if (P == "right") {
                        var z = parseFloat(n.$tooltip.css("border-left-width")),
                        W = n.$tooltip.css("border-left-color")
                    } else {
                        var z = parseFloat(n.$tooltip.css("border-bottom-width")),
                        W = n.$tooltip.css("border-bottom-color")
                    }
                    if (z > 1) {
                        z++
                    }
                    var X = "";
                    if (z !== 0) {
                        var V = "",
                        J = "border-color: " + W + ";";
                        if (R.indexOf("bottom") !== -1) {
                            V = "margin-top: -" + Math.round(z) + "px;"
                        } else if (R.indexOf("top") !== -1) {
                            V = "margin-bottom: -" + Math.round(z) + "px;"
                        } else if (R.indexOf("left") !== -1) {
                            V = "margin-right: -" + Math.round(z) + "px;"
                        } else if (R.indexOf("right") !== -1) {
                            V = "margin-left: -" + Math.round(z) + "px;"
                        }
                        X = '<span class="tooltipster-arrow-border" style="' + V + " " + J + ';"></span>'
                    }
                    n.$tooltip.find(".tooltipster-arrow").remove();
                    var K = '<div class="' + R + ' tooltipster-arrow" style="' + r + '">' + X + '<span style="border-color:' + U + ';"></span></div>';
                    n.$tooltip.append(K)
                }
                n.$tooltip.css({
                    top: Math.round(M) + "px",
                    left: Math.round(A) + "px"
                })
            }
            return n
        },
        enable: function() {
            this.enabled = true;
            return this
        },
        disable: function() {
            this.hide();
            this.enabled = false;
            return this
        },
        destroy: function() {
            var t = this;
            t.hide();
            if (t.$el[0] !== t.$elProxy[0]) t.$elProxy.remove();
            t.$el.removeData(t.namespace).off("." + t.namespace);
            var n = t.$el.data("tooltipster-ns");
            if (n.length === 1) {
                var r = typeof t.Content === "string" ? t.Content: e("<div></div>").append(t.Content).html();
                t.$el.removeClass("tooltipstered").attr("title", r).removeData(t.namespace).removeData("tooltipster-ns").off("." + t.namespace)
            } else {
                n = e.grep(n,
                function(e, n) {
                    return e !== t.namespace
                });
                t.$el.data("tooltipster-ns", n)
            }
            return t
        },
        elementIcon: function() {
            return this.$el[0] !== this.$elProxy[0] ? this.$elProxy[0] : undefined
        },
        elementTooltip: function() {
            return this.$tooltip ? this.$tooltip[0] : undefined
        },
        option: function(e) {
            return this.options[e]
        },
        status: function(e) {
            return this.Status
        }
    };
    e.fn[r] = function() {
        var t = arguments;
        if (this.length === 0) {
            if (typeof t[0] === "string") {
                var n = true;
                switch (t[0]) {
                case "setDefaults":
                    e.extend(i, t[1]);
                    break;
                default:
                    n = false;
                    break
                }
                if (n) return true;
                else return this
            } else {
                return this
            }
        } else {
            if (typeof t[0] === "string") {
                var r = "#*$~&";
                this.each(function() {
                    var n = e(this).data("tooltipster-ns"),
                    i = n ? e(this).data(n[0]) : null;
                    if (i) {
                        if (typeof i[t[0]] === "function") {
                            var s = i[t[0]](t[1])
                        } else {
                            throw new Error('Unknown method .tooltipster("' + t[0] + '")')
                        }
                        if (s !== i) {
                            r = s;
                            return false
                        }
                    } else {
                        throw new Error("You called Tooltipster's \"" + t[0] + '" method on an uninitialized element')
                    }
                });
                return r !== "#*$~&" ? r: this
            } else {
                var o = [],
                u = t[0] && typeof t[0].multiple !== "undefined",
                a = u && t[0].multiple || !u && i.multiple;
                this.each(function() {
                    var n = false,
                    r = e(this).data("tooltipster-ns"),
                    i = null;
                    if (!r) {
                        n = true
                    } else {
                        if (a) n = true;
                        else console.log('Tooltipster: one or more tooltips are already attached to this element: ignoring. Use the "multiple" option to attach more tooltips.')
                    }
                    if (n) {
                        i = new s(this, t[0]);
                        if (!r) r = [];
                        r.push(i.namespace);
                        e(this).data("tooltipster-ns", r);
                        e(this).data(i.namespace, i)
                    }
                    o.push(i)
                });
                if (a) return o;
                else return this
            }
        }
    };
    var u = !!("ontouchstart" in t);
    var a = false;
    e("body").one("mousemove",
    function() {
        a = true
    })
})(jQuery, window, document);
/*! DataTables 1.10.4
 * 漏2008-2014 SpryMedia Ltd - datatables.net/license
 */
(function(Da, P, l) {
    var O = function(g) {
        function V(a) {
            var b, c, e = {};
            g.each(a,
            function(d) {
                if ((b = d.match(/^([^A-Z]+?)([A-Z])/)) && -1 !== "a aa ai ao as b fn i m o s ".indexOf(b[1] + " ")) c = d.replace(b[0], b[2].toLowerCase()),
                e[c] = d,
                "o" === b[1] && V(a[d])
            });
            a._hungarianMap = e
        }
        function G(a, b, c) {
            a._hungarianMap || V(a);
            var e;
            g.each(b,
            function(d) {
                e = a._hungarianMap[d];
                if (e !== l && (c || b[e] === l))"o" === e.charAt(0) ? (b[e] || (b[e] = {}), g.extend(!0, b[e], b[d]), G(a[e], b[e], c)) : b[e] = b[d]
            })
        }
        function O(a) {
            var b = p.defaults.oLanguage,
            c = a.sZeroRecords; ! a.sEmptyTable && (c && "No data available in table" === b.sEmptyTable) && D(a, a, "sZeroRecords", "sEmptyTable"); ! a.sLoadingRecords && (c && "Loading..." === b.sLoadingRecords) && D(a, a, "sZeroRecords", "sLoadingRecords");
            a.sInfoThousands && (a.sThousands = a.sInfoThousands); (a = a.sDecimal) && cb(a)
        }
        function db(a) {
            z(a, "ordering", "bSort");
            z(a, "orderMulti", "bSortMulti");
            z(a, "orderClasses", "bSortClasses");
            z(a, "orderCellsTop", "bSortCellsTop");
            z(a, "order", "aaSorting");
            z(a, "orderFixed", "aaSortingFixed");
            z(a, "paging", "bPaginate");
            z(a, "pagingType", "sPaginationType");
            z(a, "pageLength", "iDisplayLength");
            z(a, "searching", "bFilter");
            if (a = a.aoSearchCols) for (var b = 0,
            c = a.length; b < c; b++) a[b] && G(p.models.oSearch, a[b])
        }
        function eb(a) {
            z(a, "orderable", "bSortable");
            z(a, "orderData", "aDataSort");
            z(a, "orderSequence", "asSorting");
            z(a, "orderDataType", "sortDataType")
        }
        function fb(a) {
            var a = a.oBrowser,
            b = g("<div/>").css({
                position: "absolute",
                top: 0,
                left: 0,
                height: 1,
                width: 1,
                overflow: "hidden"
            }).append(g("<div/>").css({
                position: "absolute",
                top: 1,
                left: 1,
                width: 100,
                overflow: "scroll"
            }).append(g('<div class="test"/>').css({
                width: "100%",
                height: 10
            }))).appendTo("body"),
            c = b.find(".test");
            a.bScrollOversize = 100 === c[0].offsetWidth;
            a.bScrollbarLeft = 1 !== c.offset().left;
            b.remove()
        }
        function gb(a, b, c, e, d, f) {
            var h, i = !1;
            c !== l && (h = c, i = !0);
            for (; e !== d;) a.hasOwnProperty(e) && (h = i ? b(h, a[e], e, a) : a[e], i = !0, e += f);
            return h
        }
        function Ea(a, b) {
            var c = p.defaults.column,
            e = a.aoColumns.length,
            c = g.extend({},
            p.models.oColumn, c, {
                nTh: b ? b: P.createElement("th"),
                sTitle: c.sTitle ? c.sTitle: b ? b.innerHTML: "",
                aDataSort: c.aDataSort ? c.aDataSort: [e],
                mData: c.mData ? c.mData: e,
                idx: e
            });
            a.aoColumns.push(c);
            c = a.aoPreSearchCols;
            c[e] = g.extend({},
            p.models.oSearch, c[e]);
            ja(a, e, null)
        }
        function ja(a, b, c) {
            var b = a.aoColumns[b],
            e = a.oClasses,
            d = g(b.nTh);
            if (!b.sWidthOrig) {
                b.sWidthOrig = d.attr("width") || null;
                var f = (d.attr("style") || "").match(/width:\s*(\d+[pxem%]+)/);
                f && (b.sWidthOrig = f[1])
            }
            c !== l && null !== c && (eb(c), G(p.defaults.column, c), c.mDataProp !== l && !c.mData && (c.mData = c.mDataProp), c.sType && (b._sManualType = c.sType), c.className && !c.sClass && (c.sClass = c.className), g.extend(b, c), D(b, c, "sWidth", "sWidthOrig"), "number" === typeof c.iDataSort && (b.aDataSort = [c.iDataSort]), D(b, c, "aDataSort"));
            var h = b.mData,
            i = W(h),
            j = b.mRender ? W(b.mRender) : null,
            c = function(a) {
                return "string" === typeof a && -1 !== a.indexOf("@")
            };
            b._bAttrSrc = g.isPlainObject(h) && (c(h.sort) || c(h.type) || c(h.filter));
            b.fnGetData = function(a, b, c) {
                var e = i(a, b, l, c);
                return j && b ? j(e, b, a, c) : e
            };
            b.fnSetData = function(a, b, c) {
                return Q(h)(a, b, c)
            };
            "number" !== typeof h && (a._rowReadObject = !0);
            a.oFeatures.bSort || (b.bSortable = !1, d.addClass(e.sSortableNone));
            a = -1 !== g.inArray("asc", b.asSorting);
            c = -1 !== g.inArray("desc", b.asSorting); ! b.bSortable || !a && !c ? (b.sSortingClass = e.sSortableNone, b.sSortingClassJUI = "") : a && !c ? (b.sSortingClass = e.sSortableAsc, b.sSortingClassJUI = e.sSortJUIAscAllowed) : !a && c ? (b.sSortingClass = e.sSortableDesc, b.sSortingClassJUI = e.sSortJUIDescAllowed) : (b.sSortingClass = e.sSortable, b.sSortingClassJUI = e.sSortJUI)
        }
        function X(a) {
            if (!1 !== a.oFeatures.bAutoWidth) {
                var b = a.aoColumns;
                Fa(a);
                for (var c = 0,
                e = b.length; c < e; c++) b[c].nTh.style.width = b[c].sWidth
            }
            b = a.oScroll; ("" !== b.sY || "" !== b.sX) && Y(a);
            u(a, null, "column-sizing", [a])
        }
        function ka(a, b) {
            var c = Z(a, "bVisible");
            return "number" === typeof c[b] ? c[b] : null
        }
        function $(a, b) {
            var c = Z(a, "bVisible"),
            c = g.inArray(b, c);
            return - 1 !== c ? c: null
        }
        function aa(a) {
            return Z(a, "bVisible").length
        }
        function Z(a, b) {
            var c = [];
            g.map(a.aoColumns,
            function(a, d) {
                a[b] && c.push(d)
            });
            return c
        }
        function Ga(a) {
            var b = a.aoColumns,
            c = a.aoData,
            e = p.ext.type.detect,
            d, f, h, i, j, g, m, o, k;
            d = 0;
            for (f = b.length; d < f; d++) if (m = b[d], k = [], !m.sType && m._sManualType) m.sType = m._sManualType;
            else if (!m.sType) {
                h = 0;
                for (i = e.length; h < i; h++) {
                    j = 0;
                    for (g = c.length; j < g; j++) {
                        k[j] === l && (k[j] = v(a, j, d, "type"));
                        o = e[h](k[j], a);
                        if (!o && h !== e.length - 1) break;
                        if ("html" === o) break
                    }
                    if (o) {
                        m.sType = o;
                        break
                    }
                }
                m.sType || (m.sType = "string")
            }
        }
        function hb(a, b, c, e) {
            var d, f, h, i, j, n, m = a.aoColumns;
            if (b) for (d = b.length - 1; 0 <= d; d--) {
                n = b[d];
                var o = n.targets !== l ? n.targets: n.aTargets;
                g.isArray(o) || (o = [o]);
                f = 0;
                for (h = o.length; f < h; f++) if ("number" === typeof o[f] && 0 <= o[f]) {
                    for (; m.length <= o[f];) Ea(a);
                    e(o[f], n)
                } else if ("number" === typeof o[f] && 0 > o[f]) e(m.length + o[f], n);
                else if ("string" === typeof o[f]) {
                    i = 0;
                    for (j = m.length; i < j; i++)("_all" == o[f] || g(m[i].nTh).hasClass(o[f])) && e(i, n)
                }
            }
            if (c) {
                d = 0;
                for (a = c.length; d < a; d++) e(d, c[d])
            }
        }
        function I(a, b, c, e) {
            var d = a.aoData.length,
            f = g.extend(!0, {},
            p.models.oRow, {
                src: c ? "dom": "data"
            });
            f._aData = b;
            a.aoData.push(f);
            for (var b = a.aoColumns,
            f = 0,
            h = b.length; f < h; f++) c && Ha(a, d, f, v(a, d, f)),
            b[f].sType = null;
            a.aiDisplayMaster.push(d); (c || !a.oFeatures.bDeferRender) && Ia(a, d, c, e);
            return d
        }
        function la(a, b) {
            var c;
            b instanceof g || (b = g(b));
            return b.map(function(b, d) {
                c = ma(a, d);
                return I(a, c.data, d, c.cells)
            })
        }
        function v(a, b, c, e) {
            var d = a.iDraw,
            f = a.aoColumns[c],
            h = a.aoData[b]._aData,
            i = f.sDefaultContent,
            c = f.fnGetData(h, e, {
                settings: a,
                row: b,
                col: c
            });
            if (c === l) return a.iDrawError != d && null === i && (R(a, 0, "Requested unknown parameter " + ("function" == typeof f.mData ? "{function}": "'" + f.mData + "'") + " for row " + b, 4), a.iDrawError = d),
            i;
            if ((c === h || null === c) && null !== i) c = i;
            else if ("function" === typeof c) return c.call(h);
            return null === c && "display" == e ? "": c
        }
        function Ha(a, b, c, e) {
            a.aoColumns[c].fnSetData(a.aoData[b]._aData, e, {
                settings: a,
                row: b,
                col: c
            })
        }
        function Ja(a) {
            return g.map(a.match(/(\\.|[^\.])+/g),
            function(a) {
                return a.replace(/\\./g, ".")
            })
        }
        function W(a) {
            if (g.isPlainObject(a)) {
                var b = {};
                g.each(a,
                function(a, c) {
                    c && (b[a] = W(c))
                });
                return function(a, c, f, h) {
                    var i = b[c] || b._;
                    return i !== l ? i(a, c, f, h) : a
                }
            }
            if (null === a) return function(a) {
                return a
            };
            if ("function" === typeof a) return function(b, c, f, h) {
                return a(b, c, f, h)
            };
            if ("string" === typeof a && ( - 1 !== a.indexOf(".") || -1 !== a.indexOf("[") || -1 !== a.indexOf("("))) {
                var c = function(a, b, f) {
                    var h, i;
                    if ("" !== f) {
                        i = Ja(f);
                        for (var j = 0,
                        g = i.length; j < g; j++) {
                            f = i[j].match(ba);
                            h = i[j].match(S);
                            if (f) {
                                i[j] = i[j].replace(ba, "");
                                "" !== i[j] && (a = a[i[j]]);
                                h = [];
                                i.splice(0, j + 1);
                                i = i.join(".");
                                j = 0;
                                for (g = a.length; j < g; j++) h.push(c(a[j], b, i));
                                a = f[0].substring(1, f[0].length - 1);
                                a = "" === a ? h: h.join(a);
                                break
                            } else if (h) {
                                i[j] = i[j].replace(S, "");
                                a = a[i[j]]();
                                continue
                            }
                            if (null === a || a[i[j]] === l) return l;
                            a = a[i[j]]
                        }
                    }
                    return a
                };
                return function(b, d) {
                    return c(b, d, a)
                }
            }
            return function(b) {
                return b[a]
            }
        }
        function Q(a) {
            if (g.isPlainObject(a)) return Q(a._);
            if (null === a) return function() {};
            if ("function" === typeof a) return function(b, e, d) {
                a(b, "set", e, d)
            };
            if ("string" === typeof a && ( - 1 !== a.indexOf(".") || -1 !== a.indexOf("[") || -1 !== a.indexOf("("))) {
                var b = function(a, e, d) {
                    var d = Ja(d),
                    f;
                    f = d[d.length - 1];
                    for (var h, i, j = 0,
                    g = d.length - 1; j < g; j++) {
                        h = d[j].match(ba);
                        i = d[j].match(S);
                        if (h) {
                            d[j] = d[j].replace(ba, "");
                            a[d[j]] = [];
                            f = d.slice();
                            f.splice(0, j + 1);
                            h = f.join(".");
                            i = 0;
                            for (g = e.length; i < g; i++) f = {},
                            b(f, e[i], h),
                            a[d[j]].push(f);
                            return
                        }
                        i && (d[j] = d[j].replace(S, ""), a = a[d[j]](e));
                        if (null === a[d[j]] || a[d[j]] === l) a[d[j]] = {};
                        a = a[d[j]]
                    }
                    if (f.match(S)) a[f.replace(S, "")](e);
                    else a[f.replace(ba, "")] = e
                };
                return function(c, e) {
                    return b(c, e, a)
                }
            }
            return function(b, e) {
                b[a] = e
            }
        }
        function Ka(a) {
            return C(a.aoData, "_aData")
        }
        function na(a) {
            a.aoData.length = 0;
            a.aiDisplayMaster.length = 0;
            a.aiDisplay.length = 0
        }
        function oa(a, b, c) {
            for (var e = -1,
            d = 0,
            f = a.length; d < f; d++) a[d] == b ? e = d: a[d] > b && a[d]--; - 1 != e && c === l && a.splice(e, 1)
        }
        function ca(a, b, c, e) {
            var d = a.aoData[b],
            f,
            h = function(c, f) {
                for (; c.childNodes.length;) c.removeChild(c.firstChild);
                c.innerHTML = v(a, b, f, "display")
            };
            if ("dom" === c || (!c || "auto" === c) && "dom" === d.src) d._aData = ma(a, d, e, e === l ? l: d._aData).data;
            else {
                var i = d.anCells;
                if (i) if (e !== l) h(i[e], e);
                else {
                    c = 0;
                    for (f = i.length; c < f; c++) h(i[c], c)
                }
            }
            d._aSortData = null;
            d._aFilterData = null;
            h = a.aoColumns;
            if (e !== l) h[e].sType = null;
            else {
                c = 0;
                for (f = h.length; c < f; c++) h[c].sType = null;
                La(d)
            }
        }
        function ma(a, b, c, e) {
            var d = [],
            f = b.firstChild,
            h,
            i = 0,
            j,
            n = a.aoColumns,
            m = a._rowReadObject,
            e = e || m ? {}: [],
            o = function(a, b) {
                if ("string" === typeof a) {
                    var c = a.indexOf("@"); - 1 !== c && (c = a.substring(c + 1), Q(a)(e, b.getAttribute(c)))
                }
            },
            a = function(a) {
                if (c === l || c === i) h = n[i],
                j = g.trim(a.innerHTML),
                h && h._bAttrSrc ? (Q(h.mData._)(e, j), o(h.mData.sort, a), o(h.mData.type, a), o(h.mData.filter, a)) : m ? (h._setter || (h._setter = Q(h.mData)), h._setter(e, j)) : e[i] = j;
                i++
            };
            if (f) for (; f;) {
                b = f.nodeName.toUpperCase();
                if ("TD" == b || "TH" == b) a(f),
                d.push(f);
                f = f.nextSibling
            } else {
                d = b.anCells;
                f = 0;
                for (b = d.length; f < b; f++) a(d[f])
            }
            return {
                data: e,
                cells: d
            }
        }
        function Ia(a, b, c, e) {
            var d = a.aoData[b],
            f = d._aData,
            h = [],
            i,
            j,
            g,
            m,
            o;
            if (null === d.nTr) {
                i = c || P.createElement("tr");
                d.nTr = i;
                d.anCells = h;
                i._DT_RowIndex = b;
                La(d);
                m = 0;
                for (o = a.aoColumns.length; m < o; m++) {
                    g = a.aoColumns[m];
                    j = c ? e[m] : P.createElement(g.sCellType);
                    h.push(j);
                    if (!c || g.mRender || g.mData !== m) j.innerHTML = v(a, b, m, "display");
                    g.sClass && (j.className += " " + g.sClass);
                    g.bVisible && !c ? i.appendChild(j) : !g.bVisible && c && j.parentNode.removeChild(j);
                    g.fnCreatedCell && g.fnCreatedCell.call(a.oInstance, j, v(a, b, m), f, b, m)
                }
                u(a, "aoRowCreatedCallback", null, [i, f, b])
            }
            d.nTr.setAttribute("role", "row")
        }
        function La(a) {
            var b = a.nTr,
            c = a._aData;
            if (b) {
                c.DT_RowId && (b.id = c.DT_RowId);
                if (c.DT_RowClass) {
                    var e = c.DT_RowClass.split(" ");
                    a.__rowc = a.__rowc ? Ma(a.__rowc.concat(e)) : e;
                    g(b).removeClass(a.__rowc.join(" ")).addClass(c.DT_RowClass)
                }
                c.DT_RowData && g(b).data(c.DT_RowData)
            }
        }
        function ib(a) {
            var b, c, e, d, f, h = a.nTHead,
            i = a.nTFoot,
            j = 0 === g("th, td", h).length,
            n = a.oClasses,
            m = a.aoColumns;
            j && (d = g("<tr/>").appendTo(h));
            b = 0;
            for (c = m.length; b < c; b++) f = m[b],
            e = g(f.nTh).addClass(f.sClass),
            j && e.appendTo(d),
            a.oFeatures.bSort && (e.addClass(f.sSortingClass), !1 !== f.bSortable && (e.attr("tabindex", a.iTabIndex).attr("aria-controls", a.sTableId), Na(a, f.nTh, b))),
            f.sTitle != e.html() && e.html(f.sTitle),
            Oa(a, "header")(a, e, f, n);
            j && da(a.aoHeader, h);
            g(h).find(">tr").attr("role", "row");
            g(h).find(">tr>th, >tr>td").addClass(n.sHeaderTH);
            g(i).find(">tr>th, >tr>td").addClass(n.sFooterTH);
            if (null !== i) {
                a = a.aoFooter[0];
                b = 0;
                for (c = a.length; b < c; b++) f = m[b],
                f.nTf = a[b].cell,
                f.sClass && g(f.nTf).addClass(f.sClass)
            }
        }
        function ea(a, b, c) {
            var e, d, f, h = [],
            i = [],
            j = a.aoColumns.length,
            n;
            if (b) {
                c === l && (c = !1);
                e = 0;
                for (d = b.length; e < d; e++) {
                    h[e] = b[e].slice();
                    h[e].nTr = b[e].nTr;
                    for (f = j - 1; 0 <= f; f--) ! a.aoColumns[f].bVisible && !c && h[e].splice(f, 1);
                    i.push([])
                }
                e = 0;
                for (d = h.length; e < d; e++) {
                    if (a = h[e].nTr) for (; f = a.firstChild;) a.removeChild(f);
                    f = 0;
                    for (b = h[e].length; f < b; f++) if (n = j = 1, i[e][f] === l) {
                        a.appendChild(h[e][f].cell);
                        for (i[e][f] = 1; h[e + j] !== l && h[e][f].cell == h[e + j][f].cell;) i[e + j][f] = 1,
                        j++;
                        for (; h[e][f + n] !== l && h[e][f].cell == h[e][f + n].cell;) {
                            for (c = 0; c < j; c++) i[e + c][f + n] = 1;
                            n++
                        }
                        g(h[e][f].cell).attr("rowspan", j).attr("colspan", n)
                    }
                }
            }
        }
        function L(a) {
            var b = u(a, "aoPreDrawCallback", "preDraw", [a]);
            if ( - 1 !== g.inArray(!1, b)) B(a, !1);
            else {
                var b = [],
                c = 0,
                e = a.asStripeClasses,
                d = e.length,
                f = a.oLanguage,
                h = a.iInitDisplayStart,
                i = "ssp" == A(a),
                j = a.aiDisplay;
                a.bDrawing = !0;
                h !== l && -1 !== h && (a._iDisplayStart = i ? h: h >= a.fnRecordsDisplay() ? 0 : h, a.iInitDisplayStart = -1);
                var h = a._iDisplayStart,
                n = a.fnDisplayEnd();
                if (a.bDeferLoading) a.bDeferLoading = !1,
                a.iDraw++,
                B(a, !1);
                else if (i) {
                    if (!a.bDestroying && !jb(a)) return
                } else a.iDraw++;
                if (0 !== j.length) {
                    f = i ? a.aoData.length: n;
                    for (i = i ? 0 : h; i < f; i++) {
                        var m = j[i],
                        o = a.aoData[m];
                        null === o.nTr && Ia(a, m);
                        m = o.nTr;
                        if (0 !== d) {
                            var k = e[c % d];
                            o._sRowStripe != k && (g(m).removeClass(o._sRowStripe).addClass(k), o._sRowStripe = k)
                        }
                        u(a, "aoRowCallback", null, [m, o._aData, c, i]);
                        b.push(m);
                        c++
                    }
                } else c = f.sZeroRecords,
                1 == a.iDraw && "ajax" == A(a) ? c = f.sLoadingRecords: f.sEmptyTable && 0 === a.fnRecordsTotal() && (c = f.sEmptyTable),
                b[0] = g("<tr/>", {
                    "class": d ? e[0] : ""
                }).append(g("<td />", {
                    valign: "top",
                    colSpan: aa(a),
                    "class": a.oClasses.sRowEmpty
                }).html(c))[0];
                u(a, "aoHeaderCallback", "header", [g(a.nTHead).children("tr")[0], Ka(a), h, n, j]);
                u(a, "aoFooterCallback", "footer", [g(a.nTFoot).children("tr")[0], Ka(a), h, n, j]);
                e = g(a.nTBody);
                e.children().detach();
                e.append(g(b));
                u(a, "aoDrawCallback", "draw", [a]);
                a.bSorted = !1;
                a.bFiltered = !1;
                a.bDrawing = !1
            }
        }
        function M(a, b) {
            var c = a.oFeatures,
            e = c.bFilter;
            c.bSort && kb(a);
            e ? fa(a, a.oPreviousSearch) : a.aiDisplay = a.aiDisplayMaster.slice(); ! 0 !== b && (a._iDisplayStart = 0);
            a._drawHold = b;
            L(a);
            a._drawHold = !1
        }
        function lb(a) {
            var b = a.oClasses,
            c = g(a.nTable),
            c = g("<div/>").insertBefore(c),
            e = a.oFeatures,
            d = g("<div/>", {
                id: a.sTableId + "_wrapper",
                "class": b.sWrapper + (a.nTFoot ? "": " " + b.sNoFooter)
            });
            a.nHolding = c[0];
            a.nTableWrapper = d[0];
            a.nTableReinsertBefore = a.nTable.nextSibling;
            for (var f = a.sDom.split(""), h, i, j, n, m, o, k = 0; k < f.length; k++) {
                h = null;
                i = f[k];
                if ("<" == i) {
                    j = g("<div/>")[0];
                    n = f[k + 1];
                    if ("'" == n || '"' == n) {
                        m = "";
                        for (o = 2; f[k + o] != n;) m += f[k + o],
                        o++;
                        "H" == m ? m = b.sJUIHeader: "F" == m && (m = b.sJUIFooter); - 1 != m.indexOf(".") ? (n = m.split("."), j.id = n[0].substr(1, n[0].length - 1), j.className = n[1]) : "#" == m.charAt(0) ? j.id = m.substr(1, m.length - 1) : j.className = m;
                        k += o
                    }
                    d.append(j);
                    d = g(j)
                } else if (">" == i) d = d.parent();
                else if ("l" == i && e.bPaginate && e.bLengthChange) h = mb(a);
                else if ("f" == i && e.bFilter) h = nb(a);
                else if ("r" == i && e.bProcessing) h = ob(a);
                else if ("t" == i) h = pb(a);
                else if ("i" == i && e.bInfo) h = qb(a);
                else if ("p" == i && e.bPaginate) h = rb(a);
                else if (0 !== p.ext.feature.length) {
                    j = p.ext.feature;
                    o = 0;
                    for (n = j.length; o < n; o++) if (i == j[o].cFeature) {
                        h = j[o].fnInit(a);
                        break
                    }
                }
                h && (j = a.aanFeatures, j[i] || (j[i] = []), j[i].push(h), d.append(h))
            }
            c.replaceWith(d)
        }
        function da(a, b) {
            var c = g(b).children("tr"),
            e,
            d,
            f,
            h,
            i,
            j,
            n,
            m,
            o,
            k;
            a.splice(0, a.length);
            f = 0;
            for (j = c.length; f < j; f++) a.push([]);
            f = 0;
            for (j = c.length; f < j; f++) {
                e = c[f];
                for (d = e.firstChild; d;) {
                    if ("TD" == d.nodeName.toUpperCase() || "TH" == d.nodeName.toUpperCase()) {
                        m = 1 * d.getAttribute("colspan");
                        o = 1 * d.getAttribute("rowspan");
                        m = !m || 0 === m || 1 === m ? 1 : m;
                        o = !o || 0 === o || 1 === o ? 1 : o;
                        h = 0;
                        for (i = a[f]; i[h];) h++;
                        n = h;
                        k = 1 === m ? !0 : !1;
                        for (i = 0; i < m; i++) for (h = 0; h < o; h++) a[f + h][n + i] = {
                            cell: d,
                            unique: k
                        },
                        a[f + h].nTr = e
                    }
                    d = d.nextSibling
                }
            }
        }
        function pa(a, b, c) {
            var e = [];
            c || (c = a.aoHeader, b && (c = [], da(c, b)));
            for (var b = 0,
            d = c.length; b < d; b++) for (var f = 0,
            h = c[b].length; f < h; f++) if (c[b][f].unique && (!e[f] || !a.bSortCellsTop)) e[f] = c[b][f].cell;
            return e
        }
        function qa(a, b, c) {
            u(a, "aoServerParams", "serverParams", [b]);
            if (b && g.isArray(b)) {
                var e = {},
                d = /(.*?)\[\]$/;
                g.each(b,
                function(a, b) {
                    var c = b.name.match(d);
                    c ? (c = c[0], e[c] || (e[c] = []), e[c].push(b.value)) : e[b.name] = b.value
                });
                b = e
            }
            var f, h = a.ajax,
            i = a.oInstance;
            if (g.isPlainObject(h) && h.data) {
                f = h.data;
                var j = g.isFunction(f) ? f(b) : f,
                b = g.isFunction(f) && j ? j: g.extend(!0, b, j);
                delete h.data
            }
            j = {
                data: b,
                success: function(b) {
                    var f = b.error || b.sError;
                    f && a.oApi._fnLog(a, 0, f);
                    a.json = b;
                    u(a, null, "xhr", [a, b]);
                    c(b)
                },
                dataType: "json",
                cache: !1,
                type: a.sServerMethod,
                error: function(b, c) {
                    var f = a.oApi._fnLog;
                    "parsererror" == c ? f(a, 0, "Invalid JSON response", 1) : 4 === b.readyState && f(a, 0, "Ajax error", 7);
                    B(a, !1)
                }
            };
            a.oAjaxData = b;
            u(a, null, "preXhr", [a, b]);
            a.fnServerData ? a.fnServerData.call(i, a.sAjaxSource, g.map(b,
            function(a, b) {
                return {
                    name: b,
                    value: a
                }
            }), c, a) : a.sAjaxSource || "string" === typeof h ? a.jqXHR = g.ajax(g.extend(j, {
                url: h || a.sAjaxSource
            })) : g.isFunction(h) ? a.jqXHR = h.call(i, b, c, a) : (a.jqXHR = g.ajax(g.extend(j, h)), h.data = f)
        }
        function jb(a) {
            return a.bAjaxDataGet ? (a.iDraw++, B(a, !0), qa(a, sb(a),
            function(b) {
                tb(a, b)
            }), !1) : !0
        }
        function sb(a) {
            var b = a.aoColumns,
            c = b.length,
            e = a.oFeatures,
            d = a.oPreviousSearch,
            f = a.aoPreSearchCols,
            h, i = [],
            j,
            n,
            m,
            o = T(a);
            h = a._iDisplayStart;
            j = !1 !== e.bPaginate ? a._iDisplayLength: -1;
            var k = function(a, b) {
                i.push({
                    name: a,
                    value: b
                })
            };
            k("sEcho", a.iDraw);
            k("iColumns", c);
            k("sColumns", C(b, "sName").join(","));
            k("iDisplayStart", h);
            k("iDisplayLength", j);
            var l = {
                draw: a.iDraw,
                columns: [],
                order: [],
                start: h,
                length: j,
                search: {
                    value: d.sSearch,
                    regex: d.bRegex
                }
            };
            for (h = 0; h < c; h++) n = b[h],
            m = f[h],
            j = "function" == typeof n.mData ? "function": n.mData,
            l.columns.push({
                data: j,
                name: n.sName,
                searchable: n.bSearchable,
                orderable: n.bSortable,
                search: {
                    value: m.sSearch,
                    regex: m.bRegex
                }
            }),
            k("mDataProp_" + h, j),
            e.bFilter && (k("sSearch_" + h, m.sSearch), k("bRegex_" + h, m.bRegex), k("bSearchable_" + h, n.bSearchable)),
            e.bSort && k("bSortable_" + h, n.bSortable);
            e.bFilter && (k("sSearch", d.sSearch), k("bRegex", d.bRegex));
            e.bSort && (g.each(o,
            function(a, b) {
                l.order.push({
                    column: b.col,
                    dir: b.dir
                });
                k("iSortCol_" + a, b.col);
                k("sSortDir_" + a, b.dir)
            }), k("iSortingCols", o.length));
            b = p.ext.legacy.ajax;
            return null === b ? a.sAjaxSource ? i: l: b ? i: l
        }
        function tb(a, b) {
            var c = b.sEcho !== l ? b.sEcho: b.draw,
            e = b.iTotalRecords !== l ? b.iTotalRecords: b.recordsTotal,
            d = b.iTotalDisplayRecords !== l ? b.iTotalDisplayRecords: b.recordsFiltered;
            if (c) {
                if (1 * c < a.iDraw) return;
                a.iDraw = 1 * c
            }
            na(a);
            a._iRecordsTotal = parseInt(e, 10);
            a._iRecordsDisplay = parseInt(d, 10);
            c = ra(a, b);
            e = 0;
            for (d = c.length; e < d; e++) I(a, c[e]);
            a.aiDisplay = a.aiDisplayMaster.slice();
            a.bAjaxDataGet = !1;
            L(a);
            a._bInitComplete || sa(a, b);
            a.bAjaxDataGet = !0;
            B(a, !1)
        }
        function ra(a, b) {
            var c = g.isPlainObject(a.ajax) && a.ajax.dataSrc !== l ? a.ajax.dataSrc: a.sAjaxDataProp;
            return "data" === c ? b.aaData || b[c] : "" !== c ? W(c)(b) : b
        }
        function nb(a) {
            var b = a.oClasses,
            c = a.sTableId,
            e = a.oLanguage,
            d = a.oPreviousSearch,
            f = a.aanFeatures,
            h = '<input type="search" class="' + b.sFilterInput + '"/>',
            i = e.sSearch,
            i = i.match(/_INPUT_/) ? i.replace("_INPUT_", h) : i + h,
            b = g("<div/>", {
                id: !f.f ? c + "_filter": null,
                "class": b.sFilter
            }).append(g("<label/>").append(i)),
            f = function() {
                var b = !this.value ? "": this.value;
                b != d.sSearch && (fa(a, {
                    sSearch: b,
                    bRegex: d.bRegex,
                    bSmart: d.bSmart,
                    bCaseInsensitive: d.bCaseInsensitive
                }), a._iDisplayStart = 0, L(a))
            },
            h = null !== a.searchDelay ? a.searchDelay: "ssp" === A(a) ? 400 : 0,
            j = g("input", b).val(d.sSearch).attr("placeholder", e.sSearchPlaceholder).bind("keyup.DT search.DT input.DT paste.DT cut.DT", h ? ta(f, h) : f).bind("keypress.DT",
            function(a) {
                if (13 == a.keyCode) return ! 1
            }).attr("aria-controls", c);
            g(a.nTable).on("search.dt.DT",
            function(b, c) {
                if (a === c) try {
                    j[0] !== P.activeElement && j.val(d.sSearch)
                } catch(f) {}
            });
            return b[0]
        }
        function fa(a, b, c) {
            var e = a.oPreviousSearch,
            d = a.aoPreSearchCols,
            f = function(a) {
                e.sSearch = a.sSearch;
                e.bRegex = a.bRegex;
                e.bSmart = a.bSmart;
                e.bCaseInsensitive = a.bCaseInsensitive
            };
            Ga(a);
            if ("ssp" != A(a)) {
                ub(a, b.sSearch, c, b.bEscapeRegex !== l ? !b.bEscapeRegex: b.bRegex, b.bSmart, b.bCaseInsensitive);
                f(b);
                for (b = 0; b < d.length; b++) vb(a, d[b].sSearch, b, d[b].bEscapeRegex !== l ? !d[b].bEscapeRegex: d[b].bRegex, d[b].bSmart, d[b].bCaseInsensitive);
                wb(a)
            } else f(b);
            a.bFiltered = !0;
            u(a, null, "search", [a])
        }
        function wb(a) {
            for (var b = p.ext.search,
            c = a.aiDisplay,
            e, d, f = 0,
            h = b.length; f < h; f++) {
                for (var i = [], j = 0, g = c.length; j < g; j++) d = c[j],
                e = a.aoData[d],
                b[f](a, e._aFilterData, d, e._aData, j) && i.push(d);
                c.length = 0;
                c.push.apply(c, i)
            }
        }
        function vb(a, b, c, e, d, f) {
            if ("" !== b) for (var h = a.aiDisplay,
            e = Pa(b, e, d, f), d = h.length - 1; 0 <= d; d--) b = a.aoData[h[d]]._aFilterData[c],
            e.test(b) || h.splice(d, 1)
        }
        function ub(a, b, c, e, d, f) {
            var e = Pa(b, e, d, f),
            d = a.oPreviousSearch.sSearch,
            f = a.aiDisplayMaster,
            h;
            0 !== p.ext.search.length && (c = !0);
            h = xb(a);
            if (0 >= b.length) a.aiDisplay = f.slice();
            else {
                if (h || c || d.length > b.length || 0 !== b.indexOf(d) || a.bSorted) a.aiDisplay = f.slice();
                b = a.aiDisplay;
                for (c = b.length - 1; 0 <= c; c--) e.test(a.aoData[b[c]]._sFilterRow) || b.splice(c, 1)
            }
        }
        function Pa(a, b, c, e) {
            a = b ? a: ua(a);
            c && (a = "^(?=.*?" + g.map(a.match(/"[^"]+"|[^ ]+/g) || "",
            function(a) {
                if ('"' === a.charAt(0)) var b = a.match(/^"(.*)"$/),
                a = b ? b[1] : a;
                return a.replace('"', "")
            }).join(")(?=.*?") + ").*$");
            return RegExp(a, e ? "i": "")
        }
        function ua(a) {
            return a.replace(Xb, "\\$1")
        }
        function xb(a) {
            var b = a.aoColumns,
            c, e, d, f, h, i, g, n, m = p.ext.type.search;
            c = !1;
            e = 0;
            for (f = a.aoData.length; e < f; e++) if (n = a.aoData[e], !n._aFilterData) {
                i = [];
                d = 0;
                for (h = b.length; d < h; d++) c = b[d],
                c.bSearchable ? (g = v(a, e, d, "filter"), m[c.sType] && (g = m[c.sType](g)), null === g && (g = ""), "string" !== typeof g && g.toString && (g = g.toString())) : g = "",
                g.indexOf && -1 !== g.indexOf("&") && (va.innerHTML = g, g = Yb ? va.textContent: va.innerText),
                g.replace && (g = g.replace(/[\r\n]/g, "")),
                i.push(g);
                n._aFilterData = i;
                n._sFilterRow = i.join("  ");
                c = !0
            }
            return c
        }
        function yb(a) {
            return {
                search: a.sSearch,
                smart: a.bSmart,
                regex: a.bRegex,
                caseInsensitive: a.bCaseInsensitive
            }
        }
        function zb(a) {
            return {
                sSearch: a.search,
                bSmart: a.smart,
                bRegex: a.regex,
                bCaseInsensitive: a.caseInsensitive
            }
        }
        function qb(a) {
            var b = a.sTableId,
            c = a.aanFeatures.i,
            e = g("<div/>", {
                "class": a.oClasses.sInfo,
                id: !c ? b + "_info": null
            });
            c || (a.aoDrawCallback.push({
                fn: Ab,
                sName: "information"
            }), e.attr("role", "status").attr("aria-live", "polite"), g(a.nTable).attr("aria-describedby", b + "_info"));
            return e[0]
        }
        function Ab(a) {
            var b = a.aanFeatures.i;
            if (0 !== b.length) {
                var c = a.oLanguage,
                e = a._iDisplayStart + 1,
                d = a.fnDisplayEnd(),
                f = a.fnRecordsTotal(),
                h = a.fnRecordsDisplay(),
                i = h ? c.sInfo: c.sInfoEmpty;
                h !== f && (i += " " + c.sInfoFiltered);
                i += c.sInfoPostFix;
                i = Bb(a, i);
                c = c.fnInfoCallback;
                null !== c && (i = c.call(a.oInstance, a, e, d, f, h, i));
                g(b).html(i)
            }
        }
        function Bb(a, b) {
            var c = a.fnFormatNumber,
            e = a._iDisplayStart + 1,
            d = a._iDisplayLength,
            f = a.fnRecordsDisplay(),
            h = -1 === d;
            return b.replace(/_START_/g, c.call(a, e)).replace(/_END_/g, c.call(a, a.fnDisplayEnd())).replace(/_MAX_/g, c.call(a, a.fnRecordsTotal())).replace(/_TOTAL_/g, c.call(a, f)).replace(/_PAGE_/g, c.call(a, h ? 1 : Math.ceil(e / d))).replace(/_PAGES_/g, c.call(a, h ? 1 : Math.ceil(f / d)))
        }
        function ga(a) {
            var b, c, e = a.iInitDisplayStart,
            d = a.aoColumns,
            f;
            c = a.oFeatures;
            if (a.bInitialised) {
                lb(a);
                ib(a);
                ea(a, a.aoHeader);
                ea(a, a.aoFooter);
                B(a, !0);
                c.bAutoWidth && Fa(a);
                b = 0;
                for (c = d.length; b < c; b++) f = d[b],
                f.sWidth && (f.nTh.style.width = s(f.sWidth));
                M(a);
                d = A(a);
                "ssp" != d && ("ajax" == d ? qa(a, [],
                function(c) {
                    var f = ra(a, c);
                    for (b = 0; b < f.length; b++) I(a, f[b]);
                    a.iInitDisplayStart = e;
                    M(a);
                    B(a, !1);
                    sa(a, c)
                },
                a) : (B(a, !1), sa(a)))
            } else setTimeout(function() {
                ga(a)
            },
            200)
        }
        function sa(a, b) {
            a._bInitComplete = !0;
            b && X(a);
            u(a, "aoInitComplete", "init", [a, b])
        }
        function Qa(a, b) {
            var c = parseInt(b, 10);
            a._iDisplayLength = c;
            Ra(a);
            u(a, null, "length", [a, c])
        }
        function mb(a) {
            for (var b = a.oClasses,
            c = a.sTableId,
            e = a.aLengthMenu,
            d = g.isArray(e[0]), f = d ? e[0] : e, e = d ? e[1] : e, d = g("<select/>", {
                name: c + "_length",
                "aria-controls": c,
                "class": b.sLengthSelect
            }), h = 0, i = f.length; h < i; h++) d[0][h] = new Option(e[h], f[h]);
            var j = g("<div><label/></div>").addClass(b.sLength);
            a.aanFeatures.l || (j[0].id = c + "_length");
            j.children().append(a.oLanguage.sLengthMenu.replace("_MENU_", d[0].outerHTML));
            g("select", j).val(a._iDisplayLength).bind("change.DT",
            function() {
                Qa(a, g(this).val());
                L(a)
            });
            g(a.nTable).bind("length.dt.DT",
            function(b, c, f) {
                a === c && g("select", j).val(f)
            });
            return j[0]
        }
        function rb(a) {
            var b = a.sPaginationType,
            c = p.ext.pager[b],
            e = "function" === typeof c,
            d = function(a) {
                L(a)
            },
            b = g("<div/>").addClass(a.oClasses.sPaging + b)[0],
            f = a.aanFeatures;
            e || c.fnInit(a, b, d);
            f.p || (b.id = a.sTableId + "_paginate", a.aoDrawCallback.push({
                fn: function(a) {
                    if (e) {
                        var b = a._iDisplayStart,
                        g = a._iDisplayLength,
                        n = a.fnRecordsDisplay(),
                        m = -1 === g,
                        b = m ? 0 : Math.ceil(b / g),
                        g = m ? 1 : Math.ceil(n / g),
                        n = c(b, g),
                        o,
                        m = 0;
                        for (o = f.p.length; m < o; m++) Oa(a, "pageButton")(a, f.p[m], m, n, b, g)
                    } else c.fnUpdate(a, d)
                },
                sName: "pagination"
            }));
            return b
        }
        function Sa(a, b, c) {
            var e = a._iDisplayStart,
            d = a._iDisplayLength,
            f = a.fnRecordsDisplay();
            0 === f || -1 === d ? e = 0 : "number" === typeof b ? (e = b * d, e > f && (e = 0)) : "first" == b ? e = 0 : "previous" == b ? (e = 0 <= d ? e - d: 0, 0 > e && (e = 0)) : "next" == b ? e + d < f && (e += d) : "last" == b ? e = Math.floor((f - 1) / d) * d: R(a, 0, "Unknown paging action: " + b, 5);
            b = a._iDisplayStart !== e;
            a._iDisplayStart = e;
            b && (u(a, null, "page", [a]), c && L(a));
            return b
        }
        function ob(a) {
            return g("<div/>", {
                id: !a.aanFeatures.r ? a.sTableId + "_processing": null,
                "class": a.oClasses.sProcessing
            }).html(a.oLanguage.sProcessing).insertBefore(a.nTable)[0]
        }
        function B(a, b) {
            a.oFeatures.bProcessing && g(a.aanFeatures.r).css("display", b ? "block": "none");
            u(a, null, "processing", [a, b])
        }
        function pb(a) {
            var b = g(a.nTable);
            b.attr("role", "grid");
            var c = a.oScroll;
            if ("" === c.sX && "" === c.sY) return a.nTable;
            var e = c.sX,
            d = c.sY,
            f = a.oClasses,
            h = b.children("caption"),
            i = h.length ? h[0]._captionSide: null,
            j = g(b[0].cloneNode(!1)),
            n = g(b[0].cloneNode(!1)),
            m = b.children("tfoot");
            c.sX && "100%" === b.attr("width") && b.removeAttr("width");
            m.length || (m = null);
            c = g("<div/>", {
                "class": f.sScrollWrapper
            }).append(g("<div/>", {
                "class": f.sScrollHead
            }).css({
                overflow: "hidden",
                position: "relative",
                border: 0,
                width: e ? !e ? null: s(e) : "100%"
            }).append(g("<div/>", {
                "class": f.sScrollHeadInner
            }).css({
                "box-sizing": "content-box",
                width: c.sXInner || "100%"
            }).append(j.removeAttr("id").css("margin-left", 0).append("top" === i ? h: null).append(b.children("thead"))))).append(g("<div/>", {
                "class": f.sScrollBody
            }).css({
                overflow: "auto",
                height: !d ? null: s(d),
                width: !e ? null: s(e)
            }).append(b));
            m && c.append(g("<div/>", {
                "class": f.sScrollFoot
            }).css({
                overflow: "hidden",
                border: 0,
                width: e ? !e ? null: s(e) : "100%"
            }).append(g("<div/>", {
                "class": f.sScrollFootInner
            }).append(n.removeAttr("id").css("margin-left", 0).append("bottom" === i ? h: null).append(b.children("tfoot")))));
            var b = c.children(),
            o = b[0],
            f = b[1],
            k = m ? b[2] : null;
            e && g(f).scroll(function() {
                var a = this.scrollLeft;
                o.scrollLeft = a;
                m && (k.scrollLeft = a)
            });
            a.nScrollHead = o;
            a.nScrollBody = f;
            a.nScrollFoot = k;
            a.aoDrawCallback.push({
                fn: Y,
                sName: "scrolling"
            });
            return c[0]
        }
        function Y(a) {
            var b = a.oScroll,
            c = b.sX,
            e = b.sXInner,
            d = b.sY,
            f = b.iBarWidth,
            h = g(a.nScrollHead),
            i = h[0].style,
            j = h.children("div"),
            n = j[0].style,
            m = j.children("table"),
            j = a.nScrollBody,
            o = g(j),
            k = j.style,
            l = g(a.nScrollFoot).children("div"),
            p = l.children("table"),
            r = g(a.nTHead),
            q = g(a.nTable),
            t = q[0],
            N = t.style,
            J = a.nTFoot ? g(a.nTFoot) : null,
            u = a.oBrowser,
            w = u.bScrollOversize,
            y,
            v,
            x,
            K,
            z,
            A = [],
            B = [],
            C = [],
            D,
            E = function(a) {
                a = a.style;
                a.paddingTop = "0";
                a.paddingBottom = "0";
                a.borderTopWidth = "0";
                a.borderBottomWidth = "0";
                a.height = 0
            };
            q.children("thead, tfoot").remove();
            z = r.clone().prependTo(q);
            y = r.find("tr");
            x = z.find("tr");
            z.find("th, td").removeAttr("tabindex");
            J && (K = J.clone().prependTo(q), v = J.find("tr"), K = K.find("tr"));
            c || (k.width = "100%", h[0].style.width = "100%");
            g.each(pa(a, z),
            function(b, c) {
                D = ka(a, b);
                c.style.width = a.aoColumns[D].sWidth
            });
            J && F(function(a) {
                a.style.width = ""
            },
            K);
            b.bCollapse && "" !== d && (k.height = o[0].offsetHeight + r[0].offsetHeight + "px");
            h = q.outerWidth();
            if ("" === c) {
                if (N.width = "100%", w && (q.find("tbody").height() > j.offsetHeight || "scroll" == o.css("overflow-y"))) N.width = s(q.outerWidth() - f)
            } else "" !== e ? N.width = s(e) : h == o.width() && o.height() < q.height() ? (N.width = s(h - f), q.outerWidth() > h - f && (N.width = s(h))) : N.width = s(h);
            h = q.outerWidth();
            F(E, x);
            F(function(a) {
                C.push(a.innerHTML);
                A.push(s(g(a).css("width")))
            },
            x);
            F(function(a, b) {
                a.style.width = A[b]
            },
            y);
            g(x).height(0);
            J && (F(E, K), F(function(a) {
                B.push(s(g(a).css("width")))
            },
            K), F(function(a, b) {
                a.style.width = B[b]
            },
            v), g(K).height(0));
            F(function(a, b) {
                a.innerHTML = '<div class="dataTables_sizing" style="height:0;overflow:hidden;">' + C[b] + "</div>";
                a.style.width = A[b]
            },
            x);
            J && F(function(a, b) {
                a.innerHTML = "";
                a.style.width = B[b]
            },
            K);
            if (q.outerWidth() < h) {
                v = j.scrollHeight > j.offsetHeight || "scroll" == o.css("overflow-y") ? h + f: h;
                if (w && (j.scrollHeight > j.offsetHeight || "scroll" == o.css("overflow-y"))) N.width = s(v - f); ("" === c || "" !== e) && R(a, 1, "Possible column misalignment", 6)
            } else v = "100%";
            k.width = s(v);
            i.width = s(v);
            J && (a.nScrollFoot.style.width = s(v)); ! d && w && (k.height = s(t.offsetHeight + f));
            d && b.bCollapse && (k.height = s(d), b = c && t.offsetWidth > j.offsetWidth ? f: 0, t.offsetHeight < j.offsetHeight && (k.height = s(t.offsetHeight + b)));
            b = q.outerWidth();
            m[0].style.width = s(b);
            n.width = s(b);
            m = q.height() > j.clientHeight || "scroll" == o.css("overflow-y");
            u = "padding" + (u.bScrollbarLeft ? "Left": "Right");
            n[u] = m ? f + "px": "0px";
            J && (p[0].style.width = s(b), l[0].style.width = s(b), l[0].style[u] = m ? f + "px": "0px");
            o.scroll();
            if ((a.bSorted || a.bFiltered) && !a._drawHold) j.scrollTop = 0
        }
        function F(a, b, c) {
            for (var e = 0,
            d = 0,
            f = b.length,
            h, g; d < f;) {
                h = b[d].firstChild;
                for (g = c ? c[d].firstChild: null; h;) 1 === h.nodeType && (c ? a(h, g, e) : a(h, e), e++),
                h = h.nextSibling,
                g = c ? g.nextSibling: null;
                d++
            }
        }
        function Fa(a) {
            var b = a.nTable,
            c = a.aoColumns,
            e = a.oScroll,
            d = e.sY,
            f = e.sX,
            h = e.sXInner,
            i = c.length,
            e = Z(a, "bVisible"),
            j = g("th", a.nTHead),
            n = b.getAttribute("width"),
            m = b.parentNode,
            o = !1,
            k,
            l;
            for (k = 0; k < e.length; k++) l = c[e[k]],
            null !== l.sWidth && (l.sWidth = Cb(l.sWidthOrig, m), o = !0);
            if (!o && !f && !d && i == aa(a) && i == j.length) for (k = 0; k < i; k++) c[k].sWidth = s(j.eq(k).width());
            else {
                i = g(b).clone().empty().css("visibility", "hidden").removeAttr("id").append(g(a.nTHead).clone(!1)).append(g(a.nTFoot).clone(!1)).append(g("<tbody><tr/></tbody>"));
                i.find("tfoot th, tfoot td").css("width", "");
                var p = i.find("tbody tr"),
                j = pa(a, i.find("thead")[0]);
                for (k = 0; k < e.length; k++) l = c[e[k]],
                j[k].style.width = null !== l.sWidthOrig && "" !== l.sWidthOrig ? s(l.sWidthOrig) : "";
                if (a.aoData.length) for (k = 0; k < e.length; k++) o = e[k],
                l = c[o],
                g(Db(a, o)).clone(!1).append(l.sContentPadding).appendTo(p);
                i.appendTo(m);
                f && h ? i.width(h) : f ? (i.css("width", "auto"), i.width() < m.offsetWidth && i.width(m.offsetWidth)) : d ? i.width(m.offsetWidth) : n && i.width(n);
                Eb(a, i[0]);
                if (f) {
                    for (k = h = 0; k < e.length; k++) l = c[e[k]],
                    d = g(j[k]).outerWidth(),
                    h += null === l.sWidthOrig ? d: parseInt(l.sWidth, 10) + d - g(j[k]).width();
                    i.width(s(h));
                    b.style.width = s(h)
                }
                for (k = 0; k < e.length; k++) if (l = c[e[k]], d = g(j[k]).width()) l.sWidth = s(d);
                b.style.width = s(i.css("width"));
                i.remove()
            }
            n && (b.style.width = s(n));
            if ((n || f) && !a._reszEvt) g(Da).bind("resize.DT-" + a.sInstance, ta(function() {
                X(a)
            })),
            a._reszEvt = !0
        }
        function ta(a, b) {
            var c = b !== l ? b: 200,
            e,
            d;
            return function() {
                var b = this,
                h = +new Date,
                g = arguments;
                e && h < e + c ? (clearTimeout(d), d = setTimeout(function() {
                    e = l;
                    a.apply(b, g)
                },
                c)) : e ? (e = h, a.apply(b, g)) : e = h
            }
        }
        function Cb(a, b) {
            if (!a) return 0;
            var c = g("<div/>").css("width", s(a)).appendTo(b || P.body),
            e = c[0].offsetWidth;
            c.remove();
            return e
        }
        function Eb(a, b) {
            var c = a.oScroll;
            if (c.sX || c.sY) c = !c.sX ? c.iBarWidth: 0,
            b.style.width = s(g(b).outerWidth() - c)
        }
        function Db(a, b) {
            var c = Fb(a, b);
            if (0 > c) return null;
            var e = a.aoData[c];
            return ! e.nTr ? g("<td/>").html(v(a, c, b, "display"))[0] : e.anCells[b]
        }
        function Fb(a, b) {
            for (var c, e = -1,
            d = -1,
            f = 0,
            h = a.aoData.length; f < h; f++) c = v(a, f, b, "display") + "",
            c = c.replace(Zb, ""),
            c.length > e && (e = c.length, d = f);
            return d
        }
        function s(a) {
            return null === a ? "0px": "number" == typeof a ? 0 > a ? "0px": a + "px": a.match(/\d$/) ? a + "px": a
        }
        function Gb() {
            if (!p.__scrollbarWidth) {
                var a = g("<p/>").css({
                    width: "100%",
                    height: 200,
                    padding: 0
                })[0],
                b = g("<div/>").css({
                    position: "absolute",
                    top: 0,
                    left: 0,
                    width: 200,
                    height: 150,
                    padding: 0,
                    overflow: "hidden",
                    visibility: "hidden"
                }).append(a).appendTo("body"),
                c = a.offsetWidth;
                b.css("overflow", "scroll");
                a = a.offsetWidth;
                c === a && (a = b[0].clientWidth);
                b.remove();
                p.__scrollbarWidth = c - a
            }
            return p.__scrollbarWidth
        }
        function T(a) {
            var b, c, e = [],
            d = a.aoColumns,
            f,
            h,
            i,
            j;
            b = a.aaSortingFixed;
            c = g.isPlainObject(b);
            var n = [];
            f = function(a) {
                a.length && !g.isArray(a[0]) ? n.push(a) : n.push.apply(n, a)
            };
            g.isArray(b) && f(b);
            c && b.pre && f(b.pre);
            f(a.aaSorting);
            c && b.post && f(b.post);
            for (a = 0; a < n.length; a++) {
                j = n[a][0];
                f = d[j].aDataSort;
                b = 0;
                for (c = f.length; b < c; b++) h = f[b],
                i = d[h].sType || "string",
                n[a]._idx === l && (n[a]._idx = g.inArray(n[a][1], d[h].asSorting)),
                e.push({
                    src: j,
                    col: h,
                    dir: n[a][1],
                    index: n[a]._idx,
                    type: i,
                    formatter: p.ext.type.order[i + "-pre"]
                })
            }
            return e
        }
        function kb(a) {
            var b, c, e = [],
            d = p.ext.type.order,
            f = a.aoData,
            h = 0,
            g,
            j = a.aiDisplayMaster,
            n;
            Ga(a);
            n = T(a);
            b = 0;
            for (c = n.length; b < c; b++) g = n[b],
            g.formatter && h++,
            Hb(a, g.col);
            if ("ssp" != A(a) && 0 !== n.length) {
                b = 0;
                for (c = j.length; b < c; b++) e[j[b]] = b;
                h === n.length ? j.sort(function(a, b) {
                    var c, d, h, g, i = n.length,
                    j = f[a]._aSortData,
                    l = f[b]._aSortData;
                    for (h = 0; h < i; h++) if (g = n[h], c = j[g.col], d = l[g.col], c = c < d ? -1 : c > d ? 1 : 0, 0 !== c) return "asc" === g.dir ? c: -c;
                    c = e[a];
                    d = e[b];
                    return c < d ? -1 : c > d ? 1 : 0
                }) : j.sort(function(a, b) {
                    var c, h, g, i, j = n.length,
                    l = f[a]._aSortData,
                    p = f[b]._aSortData;
                    for (g = 0; g < j; g++) if (i = n[g], c = l[i.col], h = p[i.col], i = d[i.type + "-" + i.dir] || d["string-" + i.dir], c = i(c, h), 0 !== c) return c;
                    c = e[a];
                    h = e[b];
                    return c < h ? -1 : c > h ? 1 : 0
                })
            }
            a.bSorted = !0
        }
        function Ib(a) {
            for (var b, c, e = a.aoColumns,
            d = T(a), a = a.oLanguage.oAria, f = 0, h = e.length; f < h; f++) {
                c = e[f];
                var g = c.asSorting;
                b = c.sTitle.replace(/<.*?>/g, "");
                var j = c.nTh;
                j.removeAttribute("aria-sort");
                c.bSortable && (0 < d.length && d[0].col == f ? (j.setAttribute("aria-sort", "asc" == d[0].dir ? "ascending": "descending"), c = g[d[0].index + 1] || g[0]) : c = g[0], b += "asc" === c ? a.sSortAscending: a.sSortDescending);
                j.setAttribute("aria-label", b)
            }
        }
        function Ta(a, b, c, e) {
            var d = a.aaSorting,
            f = a.aoColumns[b].asSorting,
            h = function(a, b) {
                var c = a._idx;
                c === l && (c = g.inArray(a[1], f));
                return c + 1 < f.length ? c + 1 : b ? null: 0
            };
            "number" === typeof d[0] && (d = a.aaSorting = [d]);
            c && a.oFeatures.bSortMulti ? (c = g.inArray(b, C(d, "0")), -1 !== c ? (b = h(d[c], !0), null === b ? d.splice(c, 1) : (d[c][1] = f[b], d[c]._idx = b)) : (d.push([b, f[0], 0]), d[d.length - 1]._idx = 0)) : d.length && d[0][0] == b ? (b = h(d[0]), d.length = 1, d[0][1] = f[b], d[0]._idx = b) : (d.length = 0, d.push([b, f[0]]), d[0]._idx = 0);
            M(a);
            "function" == typeof e && e(a)
        }
        function Na(a, b, c, e) {
            var d = a.aoColumns[c];
            Ua(b, {},
            function(b) { ! 1 !== d.bSortable && (a.oFeatures.bProcessing ? (B(a, !0), setTimeout(function() {
                    Ta(a, c, b.shiftKey, e);
                    "ssp" !== A(a) && B(a, !1)
                },
                0)) : Ta(a, c, b.shiftKey, e))
            })
        }
        function wa(a) {
            var b = a.aLastSort,
            c = a.oClasses.sSortColumn,
            e = T(a),
            d = a.oFeatures,
            f,
            h;
            if (d.bSort && d.bSortClasses) {
                d = 0;
                for (f = b.length; d < f; d++) h = b[d].src,
                g(C(a.aoData, "anCells", h)).removeClass(c + (2 > d ? d + 1 : 3));
                d = 0;
                for (f = e.length; d < f; d++) h = e[d].src,
                g(C(a.aoData, "anCells", h)).addClass(c + (2 > d ? d + 1 : 3))
            }
            a.aLastSort = e
        }
        function Hb(a, b) {
            var c = a.aoColumns[b],
            e = p.ext.order[c.sSortDataType],
            d;
            e && (d = e.call(a.oInstance, a, b, $(a, b)));
            for (var f, h = p.ext.type.order[c.sType + "-pre"], g = 0, j = a.aoData.length; g < j; g++) if (c = a.aoData[g], c._aSortData || (c._aSortData = []), !c._aSortData[b] || e) f = e ? d[g] : v(a, g, b, "sort"),
            c._aSortData[b] = h ? h(f) : f
        }
        function xa(a) {
            if (a.oFeatures.bStateSave && !a.bDestroying) {
                var b = {
                    time: +new Date,
                    start: a._iDisplayStart,
                    length: a._iDisplayLength,
                    order: g.extend(!0, [], a.aaSorting),
                    search: yb(a.oPreviousSearch),
                    columns: g.map(a.aoColumns,
                    function(b, e) {
                        return {
                            visible: b.bVisible,
                            search: yb(a.aoPreSearchCols[e])
                        }
                    })
                };
                u(a, "aoStateSaveParams", "stateSaveParams", [a, b]);
                a.oSavedState = b;
                a.fnStateSaveCallback.call(a.oInstance, a, b)
            }
        }
        function Jb(a) {
            var b, c, e = a.aoColumns;
            if (a.oFeatures.bStateSave) {
                var d = a.fnStateLoadCallback.call(a.oInstance, a);
                if (d && d.time && (b = u(a, "aoStateLoadParams", "stateLoadParams", [a, d]), -1 === g.inArray(!1, b) && (b = a.iStateDuration, !(0 < b && d.time < +new Date - 1E3 * b) && e.length === d.columns.length))) {
                    a.oLoadedState = g.extend(!0, {},
                    d);
                    a._iDisplayStart = d.start;
                    a.iInitDisplayStart = d.start;
                    a._iDisplayLength = d.length;
                    a.aaSorting = [];
                    g.each(d.order,
                    function(b, c) {
                        a.aaSorting.push(c[0] >= e.length ? [0, c[1]] : c)
                    });
                    g.extend(a.oPreviousSearch, zb(d.search));
                    b = 0;
                    for (c = d.columns.length; b < c; b++) {
                        var f = d.columns[b];
                        e[b].bVisible = f.visible;
                        g.extend(a.aoPreSearchCols[b], zb(f.search))
                    }
                    u(a, "aoStateLoaded", "stateLoaded", [a, d])
                }
            }
        }
        function ya(a) {
            var b = p.settings,
            a = g.inArray(a, C(b, "nTable"));
            return - 1 !== a ? b[a] : null
        }
        function R(a, b, c, e) {
            c = "DataTables warning: " + (null !== a ? "table id=" + a.sTableId + " - ": "") + c;
            e && (c += ". For more information about this error, please see http://datatables.net/tn/" + e);
            if (b) Da.console && console.log && console.log(c);
            else if (a = p.ext, "alert" == (a.sErrMode || a.errMode)) alert(c);
            else throw Error(c);
        }
        function D(a, b, c, e) {
            g.isArray(c) ? g.each(c,
            function(c, f) {
                g.isArray(f) ? D(a, b, f[0], f[1]) : D(a, b, f)
            }) : (e === l && (e = c), b[c] !== l && (a[e] = b[c]))
        }
        function Kb(a, b, c) {
            var e, d;
            for (d in b) b.hasOwnProperty(d) && (e = b[d], g.isPlainObject(e) ? (g.isPlainObject(a[d]) || (a[d] = {}), g.extend(!0, a[d], e)) : a[d] = c && "data" !== d && "aaData" !== d && g.isArray(e) ? e.slice() : e);
            return a
        }
        function Ua(a, b, c) {
            g(a).bind("click.DT", b,
            function(b) {
                a.blur();
                c(b)
            }).bind("keypress.DT", b,
            function(a) {
                13 === a.which && (a.preventDefault(), c(a))
            }).bind("selectstart.DT",
            function() {
                return ! 1
            })
        }
        function x(a, b, c, e) {
            c && a[b].push({
                fn: c,
                sName: e
            })
        }
        function u(a, b, c, e) {
            var d = [];
            b && (d = g.map(a[b].slice().reverse(),
            function(b) {
                return b.fn.apply(a.oInstance, e)
            }));
            null !== c && g(a.nTable).trigger(c + ".dt", e);
            return d
        }
        function Ra(a) {
            var b = a._iDisplayStart,
            c = a.fnDisplayEnd(),
            e = a._iDisplayLength;
            b >= c && (b = c - e);
            b -= b % e;
            if ( - 1 === e || 0 > b) b = 0;
            a._iDisplayStart = b
        }
        function Oa(a, b) {
            var c = a.renderer,
            e = p.ext.renderer[b];
            return g.isPlainObject(c) && c[b] ? e[c[b]] || e._: "string" === typeof c ? e[c] || e._: e._
        }
        function A(a) {
            return a.oFeatures.bServerSide ? "ssp": a.ajax || a.sAjaxSource ? "ajax": "dom"
        }
        function Va(a, b) {
            var c = [],
            c = Lb.numbers_length,
            e = Math.floor(c / 2);
            b <= c ? c = U(0, b) : a <= e ? (c = U(0, c - 2), c.push("ellipsis"), c.push(b - 1)) : (a >= b - 1 - e ? c = U(b - (c - 2), b) : (c = U(a - 1, a + 2), c.push("ellipsis"), c.push(b - 1)), c.splice(0, 0, "ellipsis"), c.splice(0, 0, 0));
            c.DT_el = "span";
            return c
        }
        function cb(a) {
            g.each({
                num: function(b) {
                    return za(b, a)
                },
                "num-fmt": function(b) {
                    return za(b, a, Wa)
                },
                "html-num": function(b) {
                    return za(b, a, Aa)
                },
                "html-num-fmt": function(b) {
                    return za(b, a, Aa, Wa)
                }
            },
            function(b, c) {
                w.type.order[b + a + "-pre"] = c;
                b.match(/^html\-/) && (w.type.search[b + a] = w.type.search.html)
            })
        }
        function Mb(a) {
            return function() {
                var b = [ya(this[p.ext.iApiIndex])].concat(Array.prototype.slice.call(arguments));
                return p.ext.internal[a].apply(this, b)
            }
        }
        var p, w, q, r, t, Xa = {},
        Nb = /[\r\n]/g,
        Aa = /<.*?>/g,
        $b = /^[\w\+\-]/,
        ac = /[\w\+\-]$/,
        Xb = RegExp("(\\/|\\.|\\*|\\+|\\?|\\||\\(|\\)|\\[|\\]|\\{|\\}|\\\\|\\$|\\^|\\-)", "g"),
        Wa = /[',$\u00a3\u20ac\u00a5%\u2009\u202F]/g,
        H = function(a) {
            return ! a || !0 === a || "-" === a ? !0 : !1
        },
        Ob = function(a) {
            var b = parseInt(a, 10);
            return ! isNaN(b) && isFinite(a) ? b: null
        },
        Pb = function(a, b) {
            Xa[b] || (Xa[b] = RegExp(ua(b), "g"));
            return "string" === typeof a && "." !== b ? a.replace(/\./g, "").replace(Xa[b], ".") : a
        },
        Ya = function(a, b, c) {
            var e = "string" === typeof a;
            b && e && (a = Pb(a, b));
            c && e && (a = a.replace(Wa, ""));
            return H(a) || !isNaN(parseFloat(a)) && isFinite(a)
        },
        Qb = function(a, b, c) {
            return H(a) ? !0 : !(H(a) || "string" === typeof a) ? null: Ya(a.replace(Aa, ""), b, c) ? !0 : null
        },
        C = function(a, b, c) {
            var e = [],
            d = 0,
            f = a.length;
            if (c !== l) for (; d < f; d++) a[d] && a[d][b] && e.push(a[d][b][c]);
            else for (; d < f; d++) a[d] && e.push(a[d][b]);
            return e
        },
        ha = function(a, b, c, e) {
            var d = [],
            f = 0,
            h = b.length;
            if (e !== l) for (; f < h; f++) a[b[f]][c] && d.push(a[b[f]][c][e]);
            else for (; f < h; f++) d.push(a[b[f]][c]);
            return d
        },
        U = function(a, b) {
            var c = [],
            e;
            b === l ? (b = 0, e = a) : (e = b, b = a);
            for (var d = b; d < e; d++) c.push(d);
            return c
        },
        Rb = function(a) {
            for (var b = [], c = 0, e = a.length; c < e; c++) a[c] && b.push(a[c]);
            return b
        },
        Ma = function(a) {
            var b = [],
            c,
            e,
            d = a.length,
            f,
            h = 0;
            e = 0;
            a: for (; e < d; e++) {
                c = a[e];
                for (f = 0; f < h; f++) if (b[f] === c) continue a;
                b.push(c);
                h++
            }
            return b
        },
        z = function(a, b, c) {
            a[b] !== l && (a[c] = a[b])
        },
        ba = /\[.*?\]$/,
        S = /\(\)$/,
        va = g("<div>")[0],
        Yb = va.textContent !== l,
        Zb = /<.*?>/g;
        p = function(a) {
            this.$ = function(a, b) {
                return this.api(!0).$(a, b)
            };
            this._ = function(a, b) {
                return this.api(!0).rows(a, b).data()
            };
            this.api = function(a) {
                return a ? new q(ya(this[w.iApiIndex])) : new q(this)
            };
            this.fnAddData = function(a, b) {
                var c = this.api(!0),
                e = g.isArray(a) && (g.isArray(a[0]) || g.isPlainObject(a[0])) ? c.rows.add(a) : c.row.add(a); (b === l || b) && c.draw();
                return e.flatten().toArray()
            };
            this.fnAdjustColumnSizing = function(a) {
                var b = this.api(!0).columns.adjust(),
                c = b.settings()[0],
                e = c.oScroll;
                a === l || a ? b.draw(!1) : ("" !== e.sX || "" !== e.sY) && Y(c)
            };
            this.fnClearTable = function(a) {
                var b = this.api(!0).clear(); (a === l || a) && b.draw()
            };
            this.fnClose = function(a) {
                this.api(!0).row(a).child.hide()
            };
            this.fnDeleteRow = function(a, b, c) {
                var e = this.api(!0),
                a = e.rows(a),
                d = a.settings()[0],
                g = d.aoData[a[0][0]];
                a.remove();
                b && b.call(this, d, g); (c === l || c) && e.draw();
                return g
            };
            this.fnDestroy = function(a) {
                this.api(!0).destroy(a)
            };
            this.fnDraw = function(a) {
                this.api(!0).draw(!a)
            };
            this.fnFilter = function(a, b, c, e, d, g) {
                d = this.api(!0);
                null === b || b === l ? d.search(a, c, e, g) : d.column(b).search(a, c, e, g);
                d.draw()
            };
            this.fnGetData = function(a, b) {
                var c = this.api(!0);
                if (a !== l) {
                    var e = a.nodeName ? a.nodeName.toLowerCase() : "";
                    return b !== l || "td" == e || "th" == e ? c.cell(a, b).data() : c.row(a).data() || null
                }
                return c.data().toArray()
            };
            this.fnGetNodes = function(a) {
                var b = this.api(!0);
                return a !== l ? b.row(a).node() : b.rows().nodes().flatten().toArray()
            };
            this.fnGetPosition = function(a) {
                var b = this.api(!0),
                c = a.nodeName.toUpperCase();
                return "TR" == c ? b.row(a).index() : "TD" == c || "TH" == c ? (a = b.cell(a).index(), [a.row, a.columnVisible, a.column]) : null
            };
            this.fnIsOpen = function(a) {
                return this.api(!0).row(a).child.isShown()
            };
            this.fnOpen = function(a, b, c) {
                return this.api(!0).row(a).child(b, c).show().child()[0]
            };
            this.fnPageChange = function(a, b) {
                var c = this.api(!0).page(a); (b === l || b) && c.draw(!1)
            };
            this.fnSetColumnVis = function(a, b, c) {
                a = this.api(!0).column(a).visible(b); (c === l || c) && a.columns.adjust().draw()
            };
            this.fnSettings = function() {
                return ya(this[w.iApiIndex])
            };
            this.fnSort = function(a) {
                this.api(!0).order(a).draw()
            };
            this.fnSortListener = function(a, b, c) {
                this.api(!0).order.listener(a, b, c)
            };
            this.fnUpdate = function(a, b, c, e, d) {
                var g = this.api(!0);
                c === l || null === c ? g.row(b).data(a) : g.cell(b, c).data(a); (d === l || d) && g.columns.adjust(); (e === l || e) && g.draw();
                return 0
            };
            this.fnVersionCheck = w.fnVersionCheck;
            var b = this,
            c = a === l,
            e = this.length;
            c && (a = {});
            this.oApi = this.internal = w.internal;
            for (var d in p.ext.internal) d && (this[d] = Mb(d));
            this.each(function() {
                var d = {},
                d = 1 < e ? Kb(d, a, !0) : a,
                h = 0,
                i,
                j = this.getAttribute("id"),
                n = !1,
                m = p.defaults;
                if ("table" != this.nodeName.toLowerCase()) R(null, 0, "Non-table node initialisation (" + this.nodeName + ")", 2);
                else {
                    db(m);
                    eb(m.column);
                    G(m, m, !0);
                    G(m.column, m.column, !0);
                    G(m, d);
                    var o = p.settings,
                    h = 0;
                    for (i = o.length; h < i; h++) {
                        if (o[h].nTable == this) {
                            i = d.bRetrieve !== l ? d.bRetrieve: m.bRetrieve;
                            if (c || i) return o[h].oInstance;
                            if (d.bDestroy !== l ? d.bDestroy: m.bDestroy) {
                                o[h].oInstance.fnDestroy();
                                break
                            } else {
                                R(o[h], 0, "Cannot reinitialise DataTable", 3);
                                return
                            }
                        }
                        if (o[h].sTableId == this.id) {
                            o.splice(h, 1);
                            break
                        }
                    }
                    if (null === j || "" === j) this.id = j = "DataTables_Table_" + p.ext._unique++;
                    var k = g.extend(!0, {},
                    p.models.oSettings, {
                        nTable: this,
                        oApi: b.internal,
                        oInit: d,
                        sDestroyWidth: g(this)[0].style.width,
                        sInstance: j,
                        sTableId: j
                    });
                    o.push(k);
                    k.oInstance = 1 === b.length ? b: g(this).dataTable();
                    db(d);
                    d.oLanguage && O(d.oLanguage);
                    d.aLengthMenu && !d.iDisplayLength && (d.iDisplayLength = g.isArray(d.aLengthMenu[0]) ? d.aLengthMenu[0][0] : d.aLengthMenu[0]);
                    d = Kb(g.extend(!0, {},
                    m), d);
                    D(k.oFeatures, d, "bPaginate bLengthChange bFilter bSort bSortMulti bInfo bProcessing bAutoWidth bSortClasses bServerSide bDeferRender".split(" "));
                    D(k, d, ["asStripeClasses", "ajax", "fnServerData", "fnFormatNumber", "sServerMethod", "aaSorting", "aaSortingFixed", "aLengthMenu", "sPaginationType", "sAjaxSource", "sAjaxDataProp", "iStateDuration", "sDom", "bSortCellsTop", "iTabIndex", "fnStateLoadCallback", "fnStateSaveCallback", "renderer", "searchDelay", ["iCookieDuration", "iStateDuration"], ["oSearch", "oPreviousSearch"], ["aoSearchCols", "aoPreSearchCols"], ["iDisplayLength", "_iDisplayLength"], ["bJQueryUI", "bJUI"]]);
                    D(k.oScroll, d, [["sScrollX", "sX"], ["sScrollXInner", "sXInner"], ["sScrollY", "sY"], ["bScrollCollapse", "bCollapse"]]);
                    D(k.oLanguage, d, "fnInfoCallback");
                    x(k, "aoDrawCallback", d.fnDrawCallback, "user");
                    x(k, "aoServerParams", d.fnServerParams, "user");
                    x(k, "aoStateSaveParams", d.fnStateSaveParams, "user");
                    x(k, "aoStateLoadParams", d.fnStateLoadParams, "user");
                    x(k, "aoStateLoaded", d.fnStateLoaded, "user");
                    x(k, "aoRowCallback", d.fnRowCallback, "user");
                    x(k, "aoRowCreatedCallback", d.fnCreatedRow, "user");
                    x(k, "aoHeaderCallback", d.fnHeaderCallback, "user");
                    x(k, "aoFooterCallback", d.fnFooterCallback, "user");
                    x(k, "aoInitComplete", d.fnInitComplete, "user");
                    x(k, "aoPreDrawCallback", d.fnPreDrawCallback, "user");
                    j = k.oClasses;
                    d.bJQueryUI ? (g.extend(j, p.ext.oJUIClasses, d.oClasses), d.sDom === m.sDom && "lfrtip" === m.sDom && (k.sDom = '<"H"lfr>t<"F"ip>'), k.renderer) ? g.isPlainObject(k.renderer) && !k.renderer.header && (k.renderer.header = "jqueryui") : k.renderer = "jqueryui": g.extend(j, p.ext.classes, d.oClasses);
                    g(this).addClass(j.sTable);
                    if ("" !== k.oScroll.sX || "" !== k.oScroll.sY) k.oScroll.iBarWidth = Gb(); ! 0 === k.oScroll.sX && (k.oScroll.sX = "100%");
                    k.iInitDisplayStart === l && (k.iInitDisplayStart = d.iDisplayStart, k._iDisplayStart = d.iDisplayStart);
                    null !== d.iDeferLoading && (k.bDeferLoading = !0, h = g.isArray(d.iDeferLoading), k._iRecordsDisplay = h ? d.iDeferLoading[0] : d.iDeferLoading, k._iRecordsTotal = h ? d.iDeferLoading[1] : d.iDeferLoading);
                    var r = k.oLanguage;
                    g.extend(!0, r, d.oLanguage);
                    "" !== r.sUrl && (g.ajax({
                        dataType: "json",
                        url: r.sUrl,
                        success: function(a) {
                            O(a);
                            G(m.oLanguage, a);
                            g.extend(true, r, a);
                            ga(k)
                        },
                        error: function() {
                            ga(k)
                        }
                    }), n = !0);
                    null === d.asStripeClasses && (k.asStripeClasses = [j.sStripeOdd, j.sStripeEven]);
                    var h = k.asStripeClasses,
                    q = g("tbody tr:eq(0)", this); - 1 !== g.inArray(!0, g.map(h,
                    function(a) {
                        return q.hasClass(a)
                    })) && (g("tbody tr", this).removeClass(h.join(" ")), k.asDestroyStripes = h.slice());
                    var o = [],
                    s,
                    h = this.getElementsByTagName("thead");
                    0 !== h.length && (da(k.aoHeader, h[0]), o = pa(k));
                    if (null === d.aoColumns) {
                        s = [];
                        h = 0;
                        for (i = o.length; h < i; h++) s.push(null)
                    } else s = d.aoColumns;
                    h = 0;
                    for (i = s.length; h < i; h++) Ea(k, o ? o[h] : null);
                    hb(k, d.aoColumnDefs, s,
                    function(a, b) {
                        ja(k, a, b)
                    });
                    if (q.length) {
                        var t = function(a, b) {
                            return a.getAttribute("data-" + b) ? b: null
                        };
                        g.each(ma(k, q[0]).cells,
                        function(a, b) {
                            var c = k.aoColumns[a];
                            if (c.mData === a) {
                                var e = t(b, "sort") || t(b, "order"),
                                d = t(b, "filter") || t(b, "search");
                                if (e !== null || d !== null) {
                                    c.mData = {
                                        _: a + ".display",
                                        sort: e !== null ? a + ".@data-" + e: l,
                                        type: e !== null ? a + ".@data-" + e: l,
                                        filter: d !== null ? a + ".@data-" + d: l
                                    };
                                    ja(k, a)
                                }
                            }
                        })
                    }
                    var v = k.oFeatures;
                    d.bStateSave && (v.bStateSave = !0, Jb(k, d), x(k, "aoDrawCallback", xa, "state_save"));
                    if (d.aaSorting === l) {
                        o = k.aaSorting;
                        h = 0;
                        for (i = o.length; h < i; h++) o[h][1] = k.aoColumns[h].asSorting[0]
                    }
                    wa(k);
                    v.bSort && x(k, "aoDrawCallback",
                    function() {
                        if (k.bSorted) {
                            var a = T(k),
                            b = {};
                            g.each(a,
                            function(a, c) {
                                b[c.src] = c.dir
                            });
                            u(k, null, "order", [k, a, b]);
                            Ib(k)
                        }
                    });
                    x(k, "aoDrawCallback",
                    function() { (k.bSorted || A(k) === "ssp" || v.bDeferRender) && wa(k)
                    },
                    "sc");
                    fb(k);
                    h = g(this).children("caption").each(function() {
                        this._captionSide = g(this).css("caption-side")
                    });
                    i = g(this).children("thead");
                    0 === i.length && (i = g("<thead/>").appendTo(this));
                    k.nTHead = i[0];
                    i = g(this).children("tbody");
                    0 === i.length && (i = g("<tbody/>").appendTo(this));
                    k.nTBody = i[0];
                    i = g(this).children("tfoot");
                    if (0 === i.length && 0 < h.length && ("" !== k.oScroll.sX || "" !== k.oScroll.sY)) i = g("<tfoot/>").appendTo(this);
                    0 === i.length || 0 === i.children().length ? g(this).addClass(j.sNoFooter) : 0 < i.length && (k.nTFoot = i[0], da(k.aoFooter, k.nTFoot));
                    if (d.aaData) for (h = 0; h < d.aaData.length; h++) I(k, d.aaData[h]);
                    else(k.bDeferLoading || "dom" == A(k)) && la(k, g(k.nTBody).children("tr"));
                    k.aiDisplay = k.aiDisplayMaster.slice();
                    k.bInitialised = !0; ! 1 === n && ga(k)
                }
            });
            b = null;
            return this
        };
        var Sb = [],
        y = Array.prototype,
        bc = function(a) {
            var b, c, e = p.settings,
            d = g.map(e,
            function(a) {
                return a.nTable
            });
            if (a) {
                if (a.nTable && a.oApi) return [a];
                if (a.nodeName && "table" === a.nodeName.toLowerCase()) return b = g.inArray(a, d),
                -1 !== b ? [e[b]] : null;
                if (a && "function" === typeof a.settings) return a.settings().toArray();
                "string" === typeof a ? c = g(a) : a instanceof g && (c = a)
            } else return [];
            if (c) return c.map(function() {
                b = g.inArray(this, d);
                return - 1 !== b ? e[b] : null
            }).toArray()
        };
        q = function(a, b) {
            if (!this instanceof q) throw "DT API must be constructed as a new object";
            var c = [],
            e = function(a) { (a = bc(a)) && c.push.apply(c, a)
            };
            if (g.isArray(a)) for (var d = 0,
            f = a.length; d < f; d++) e(a[d]);
            else e(a);
            this.context = Ma(c);
            b && this.push.apply(this, b.toArray ? b.toArray() : b);
            this.selector = {
                rows: null,
                cols: null,
                opts: null
            };
            q.extend(this, this, Sb)
        };
        p.Api = q;
        q.prototype = {
            concat: y.concat,
            context: [],
            each: function(a) {
                for (var b = 0,
                c = this.length; b < c; b++) a.call(this, this[b], b, this);
                return this
            },
            eq: function(a) {
                var b = this.context;
                return b.length > a ? new q(b[a], this[a]) : null
            },
            filter: function(a) {
                var b = [];
                if (y.filter) b = y.filter.call(this, a, this);
                else for (var c = 0,
                e = this.length; c < e; c++) a.call(this, this[c], c, this) && b.push(this[c]);
                return new q(this.context, b)
            },
            flatten: function() {
                var a = [];
                return new q(this.context, a.concat.apply(a, this.toArray()))
            },
            join: y.join,
            indexOf: y.indexOf ||
            function(a, b) {
                for (var c = b || 0,
                e = this.length; c < e; c++) if (this[c] === a) return c;
                return - 1
            },
            iterator: function(a, b, c, e) {
                var d = [],
                f,
                h,
                g,
                j,
                n,
                m = this.context,
                o,
                k,
                p = this.selector;
                "string" === typeof a && (e = c, c = b, b = a, a = !1);
                h = 0;
                for (g = m.length; h < g; h++) {
                    var r = new q(m[h]);
                    if ("table" === b) f = c.call(r, m[h], h),
                    f !== l && d.push(f);
                    else if ("columns" === b || "rows" === b) f = c.call(r, m[h], this[h], h),
                    f !== l && d.push(f);
                    else if ("column" === b || "column-rows" === b || "row" === b || "cell" === b) {
                        k = this[h];
                        "column-rows" === b && (o = Ba(m[h], p.opts));
                        j = 0;
                        for (n = k.length; j < n; j++) f = k[j],
                        f = "cell" === b ? c.call(r, m[h], f.row, f.column, h, j) : c.call(r, m[h], f, h, j, o),
                        f !== l && d.push(f)
                    }
                }
                return d.length || e ? (a = new q(m, a ? d.concat.apply([], d) : d), b = a.selector, b.rows = p.rows, b.cols = p.cols, b.opts = p.opts, a) : this
            },
            lastIndexOf: y.lastIndexOf ||
            function(a, b) {
                return this.indexOf.apply(this.toArray.reverse(), arguments)
            },
            length: 0,
            map: function(a) {
                var b = [];
                if (y.map) b = y.map.call(this, a, this);
                else for (var c = 0,
                e = this.length; c < e; c++) b.push(a.call(this, this[c], c));
                return new q(this.context, b)
            },
            pluck: function(a) {
                return this.map(function(b) {
                    return b[a]
                })
            },
            pop: y.pop,
            push: y.push,
            reduce: y.reduce ||
            function(a, b) {
                return gb(this, a, b, 0, this.length, 1)
            },
            reduceRight: y.reduceRight ||
            function(a, b) {
                return gb(this, a, b, this.length - 1, -1, -1)
            },
            reverse: y.reverse,
            selector: null,
            shift: y.shift,
            sort: y.sort,
            splice: y.splice,
            toArray: function() {
                return y.slice.call(this)
            },
            to$: function() {
                return g(this)
            },
            toJQuery: function() {
                return g(this)
            },
            unique: function() {
                return new q(this.context, Ma(this))
            },
            unshift: y.unshift
        };
        q.extend = function(a, b, c) {
            if (b && (b instanceof q || b.__dt_wrapper)) {
                var e, d, f, h = function(a, b, c) {
                    return function() {
                        var e = b.apply(a, arguments);
                        q.extend(e, e, c.methodExt);
                        return e
                    }
                };
                e = 0;
                for (d = c.length; e < d; e++) f = c[e],
                b[f.name] = "function" === typeof f.val ? h(a, f.val, f) : g.isPlainObject(f.val) ? {}: f.val,
                b[f.name].__dt_wrapper = !0,
                q.extend(a, b[f.name], f.propExt)
            }
        };
        q.register = r = function(a, b) {
            if (g.isArray(a)) for (var c = 0,
            e = a.length; c < e; c++) q.register(a[c], b);
            else for (var d = a.split("."), f = Sb, h, i, c = 0, e = d.length; c < e; c++) {
                h = (i = -1 !== d[c].indexOf("()")) ? d[c].replace("()", "") : d[c];
                var j;
                a: {
                    j = 0;
                    for (var n = f.length; j < n; j++) if (f[j].name === h) {
                        j = f[j];
                        break a
                    }
                    j = null
                }
                j || (j = {
                    name: h,
                    val: {},
                    methodExt: [],
                    propExt: []
                },
                f.push(j));
                c === e - 1 ? j.val = b: f = i ? j.methodExt: j.propExt
            }
        };
        q.registerPlural = t = function(a, b, c) {
            q.register(a, c);
            q.register(b,
            function() {
                var a = c.apply(this, arguments);
                return a === this ? this: a instanceof q ? a.length ? g.isArray(a[0]) ? new q(a.context, a[0]) : a[0] : l: a
            })
        };
        r("tables()",
        function(a) {
            var b;
            if (a) {
                b = q;
                var c = this.context;
                if ("number" === typeof a) a = [c[a]];
                else var e = g.map(c,
                function(a) {
                    return a.nTable
                }),
                a = g(e).filter(a).map(function() {
                    var a = g.inArray(this, e);
                    return c[a]
                }).toArray();
                b = new b(a)
            } else b = this;
            return b
        });
        r("table()",
        function(a) {
            var a = this.tables(a),
            b = a.context;
            return b.length ? new q(b[0]) : a
        });
        t("tables().nodes()", "table().node()",
        function() {
            return this.iterator("table",
            function(a) {
                return a.nTable
            },
            1)
        });
        t("tables().body()", "table().body()",
        function() {
            return this.iterator("table",
            function(a) {
                return a.nTBody
            },
            1)
        });
        t("tables().header()", "table().header()",
        function() {
            return this.iterator("table",
            function(a) {
                return a.nTHead
            },
            1)
        });
        t("tables().footer()", "table().footer()",
        function() {
            return this.iterator("table",
            function(a) {
                return a.nTFoot
            },
            1)
        });
        t("tables().containers()", "table().container()",
        function() {
            return this.iterator("table",
            function(a) {
                return a.nTableWrapper
            },
            1)
        });
        r("draw()",
        function(a) {
            return this.iterator("table",
            function(b) {
                M(b, !1 === a)
            })
        });
        r("page()",
        function(a) {
            return a === l ? this.page.info().page: this.iterator("table",
            function(b) {
                Sa(b, a)
            })
        });
        r("page.info()",
        function() {
            if (0 === this.context.length) return l;
            var a = this.context[0],
            b = a._iDisplayStart,
            c = a._iDisplayLength,
            e = a.fnRecordsDisplay(),
            d = -1 === c;
            return {
                page: d ? 0 : Math.floor(b / c),
                pages: d ? 1 : Math.ceil(e / c),
                start: b,
                end: a.fnDisplayEnd(),
                length: c,
                recordsTotal: a.fnRecordsTotal(),
                recordsDisplay: e
            }
        });
        r("page.len()",
        function(a) {
            return a === l ? 0 !== this.context.length ? this.context[0]._iDisplayLength: l: this.iterator("table",
            function(b) {
                Qa(b, a)
            })
        });
        var Tb = function(a, b, c) {
            "ssp" == A(a) ? M(a, b) : (B(a, !0), qa(a, [],
            function(c) {
                na(a);
                for (var c = ra(a, c), e = 0, h = c.length; e < h; e++) I(a, c[e]);
                M(a, b);
                B(a, !1)
            }));
            if (c) {
                var e = new q(a);
                e.one("draw",
                function() {
                    c(e.ajax.json())
                })
            }
        };
        r("ajax.json()",
        function() {
            var a = this.context;
            if (0 < a.length) return a[0].json
        });
        r("ajax.params()",
        function() {
            var a = this.context;
            if (0 < a.length) return a[0].oAjaxData
        });
        r("ajax.reload()",
        function(a, b) {
            return this.iterator("table",
            function(c) {
                Tb(c, !1 === b, a)
            })
        });
        r("ajax.url()",
        function(a) {
            var b = this.context;
            if (a === l) {
                if (0 === b.length) return l;
                b = b[0];
                return b.ajax ? g.isPlainObject(b.ajax) ? b.ajax.url: b.ajax: b.sAjaxSource
            }
            return this.iterator("table",
            function(b) {
                g.isPlainObject(b.ajax) ? b.ajax.url = a: b.ajax = a
            })
        });
        r("ajax.url().load()",
        function(a, b) {
            return this.iterator("table",
            function(c) {
                Tb(c, !1 === b, a)
            })
        });
        var Za = function(a, b) {
            var c = [],
            e,
            d,
            f,
            h,
            i,
            j;
            e = typeof a;
            if (!a || "string" === e || "function" === e || a.length === l) a = [a];
            f = 0;
            for (h = a.length; f < h; f++) {
                d = a[f] && a[f].split ? a[f].split(",") : [a[f]];
                i = 0;
                for (j = d.length; i < j; i++)(e = b("string" === typeof d[i] ? g.trim(d[i]) : d[i])) && e.length && c.push.apply(c, e)
            }
            return c
        },
        $a = function(a) {
            a || (a = {});
            a.filter && !a.search && (a.search = a.filter);
            return {
                search: a.search || "none",
                order: a.order || "current",
                page: a.page || "all"
            }
        },
        ab = function(a) {
            for (var b = 0,
            c = a.length; b < c; b++) if (0 < a[b].length) return a[0] = a[b],
            a.length = 1,
            a.context = [a.context[b]],
            a;
            a.length = 0;
            return a
        },
        Ba = function(a, b) {
            var c, e, d, f = [],
            h = a.aiDisplay;
            c = a.aiDisplayMaster;
            var i = b.search;
            e = b.order;
            d = b.page;
            if ("ssp" == A(a)) return "removed" === i ? [] : U(0, c.length);
            if ("current" == d) {
                c = a._iDisplayStart;
                for (e = a.fnDisplayEnd(); c < e; c++) f.push(h[c])
            } else if ("current" == e || "applied" == e) f = "none" == i ? c.slice() : "applied" == i ? h.slice() : g.map(c,
            function(a) {
                return - 1 === g.inArray(a, h) ? a: null
            });
            else if ("index" == e || "original" == e) {
                c = 0;
                for (e = a.aoData.length; c < e; c++)"none" == i ? f.push(c) : (d = g.inArray(c, h), ( - 1 === d && "removed" == i || 0 <= d && "applied" == i) && f.push(c))
            }
            return f
        };
        r("rows()",
        function(a, b) {
            a === l ? a = "": g.isPlainObject(a) && (b = a, a = "");
            var b = $a(b),
            c = this.iterator("table",
            function(c) {
                var d = b;
                return Za(a,
                function(a) {
                    var b = Ob(a);
                    if (b !== null && !d) return [b];
                    var i = Ba(c, d);
                    if (b !== null && g.inArray(b, i) !== -1) return [b];
                    if (!a) return i;
                    if (typeof a === "function") return g.map(i,
                    function(b) {
                        var d = c.aoData[b];
                        return a(b, d._aData, d.nTr) ? b: null
                    });
                    b = Rb(ha(c.aoData, i, "nTr"));
                    return a.nodeName && g.inArray(a, b) !== -1 ? [a._DT_RowIndex] : g(b).filter(a).map(function() {
                        return this._DT_RowIndex
                    }).toArray()
                })
            },
            1);
            c.selector.rows = a;
            c.selector.opts = b;
            return c
        });
        r("rows().nodes()",
        function() {
            return this.iterator("row",
            function(a, b) {
                return a.aoData[b].nTr || l
            },
            1)
        });
        r("rows().data()",
        function() {
            return this.iterator(!0, "rows",
            function(a, b) {
                return ha(a.aoData, b, "_aData")
            },
            1)
        });
        t("rows().cache()", "row().cache()",
        function(a) {
            return this.iterator("row",
            function(b, c) {
                var e = b.aoData[c];
                return "search" === a ? e._aFilterData: e._aSortData
            },
            1)
        });
        t("rows().invalidate()", "row().invalidate()",
        function(a) {
            return this.iterator("row",
            function(b, c) {
                ca(b, c, a)
            })
        });
        t("rows().indexes()", "row().index()",
        function() {
            return this.iterator("row",
            function(a, b) {
                return b
            },
            1)
        });
        t("rows().remove()", "row().remove()",
        function() {
            var a = this;
            return this.iterator("row",
            function(b, c, e) {
                var d = b.aoData;
                d.splice(c, 1);
                for (var f = 0,
                h = d.length; f < h; f++) null !== d[f].nTr && (d[f].nTr._DT_RowIndex = f);
                g.inArray(c, b.aiDisplay);
                oa(b.aiDisplayMaster, c);
                oa(b.aiDisplay, c);
                oa(a[e], c, !1);
                Ra(b)
            })
        });
        r("rows.add()",
        function(a) {
            var b = this.iterator("table",
            function(b) {
                var c, f, h, g = [];
                f = 0;
                for (h = a.length; f < h; f++) c = a[f],
                c.nodeName && "TR" === c.nodeName.toUpperCase() ? g.push(la(b, c)[0]) : g.push(I(b, c));
                return g
            },
            1),
            c = this.rows( - 1);
            c.pop();
            c.push.apply(c, b.toArray());
            return c
        });
        r("row()",
        function(a, b) {
            return ab(this.rows(a, b))
        });
        r("row().data()",
        function(a) {
            var b = this.context;
            if (a === l) return b.length && this.length ? b[0].aoData[this[0]]._aData: l;
            b[0].aoData[this[0]]._aData = a;
            ca(b[0], this[0], "data");
            return this
        });
        r("row().node()",
        function() {
            var a = this.context;
            return a.length && this.length ? a[0].aoData[this[0]].nTr || null: null
        });
        r("row.add()",
        function(a) {
            a instanceof g && a.length && (a = a[0]);
            var b = this.iterator("table",
            function(b) {
                return a.nodeName && "TR" === a.nodeName.toUpperCase() ? la(b, a)[0] : I(b, a)
            });
            return this.row(b[0])
        });
        var bb = function(a, b) {
            var c = a.context;
            c.length && (c = c[0].aoData[b !== l ? b: a[0]], c._details && (c._details.remove(), c._detailsShow = l, c._details = l))
        },
        Ub = function(a, b) {
            var c = a.context;
            if (c.length && a.length) {
                var e = c[0].aoData[a[0]];
                if (e._details) { (e._detailsShow = b) ? e._details.insertAfter(e.nTr) : e._details.detach();
                    var d = c[0],
                    f = new q(d),
                    h = d.aoData;
                    f.off("draw.dt.DT_details column-visibility.dt.DT_details destroy.dt.DT_details");
                    0 < C(h, "_details").length && (f.on("draw.dt.DT_details",
                    function(a, b) {
                        d === b && f.rows({
                            page: "current"
                        }).eq(0).each(function(a) {
                            a = h[a];
                            a._detailsShow && a._details.insertAfter(a.nTr)
                        })
                    }), f.on("column-visibility.dt.DT_details",
                    function(a, b) {
                        if (d === b) for (var c, e = aa(b), f = 0, g = h.length; f < g; f++) c = h[f],
                        c._details && c._details.children("td[colspan]").attr("colspan", e)
                    }), f.on("destroy.dt.DT_details",
                    function(a, b) {
                        if (d === b) for (var c = 0,
                        e = h.length; c < e; c++) h[c]._details && bb(f, c)
                    }))
                }
            }
        };
        r("row().child()",
        function(a, b) {
            var c = this.context;
            if (a === l) return c.length && this.length ? c[0].aoData[this[0]]._details: l;
            if (!0 === a) this.child.show();
            else if (!1 === a) bb(this);
            else if (c.length && this.length) {
                var e = c[0],
                c = c[0].aoData[this[0]],
                d = [],
                f = function(a, b) {
                    if (a.nodeName && "tr" === a.nodeName.toLowerCase()) d.push(a);
                    else {
                        var c = g("<tr><td/></tr>").addClass(b);
                        g("td", c).addClass(b).html(a)[0].colSpan = aa(e);
                        d.push(c[0])
                    }
                };
                if (g.isArray(a) || a instanceof g) for (var h = 0,
                i = a.length; h < i; h++) f(a[h], b);
                else f(a, b);
                c._details && c._details.remove();
                c._details = g(d);
                c._detailsShow && c._details.insertAfter(c.nTr)
            }
            return this
        });
        r(["row().child.show()", "row().child().show()"],
        function() {
            Ub(this, !0);
            return this
        });
        r(["row().child.hide()", "row().child().hide()"],
        function() {
            Ub(this, !1);
            return this
        });
        r(["row().child.remove()", "row().child().remove()"],
        function() {
            bb(this);
            return this
        });
        r("row().child.isShown()",
        function() {
            var a = this.context;
            return a.length && this.length ? a[0].aoData[this[0]]._detailsShow || !1 : !1
        });
        var cc = /^(.+):(name|visIdx|visible)$/,
        Vb = function(a, b, c, e, d) {
            for (var c = [], e = 0, f = d.length; e < f; e++) c.push(v(a, d[e], b));
            return c
        };
        r("columns()",
        function(a, b) {
            a === l ? a = "": g.isPlainObject(a) && (b = a, a = "");
            var b = $a(b),
            c = this.iterator("table",
            function(c) {
                var d = a,
                f = b,
                h = c.aoColumns,
                i = C(h, "sName"),
                j = C(h, "nTh");
                return Za(d,
                function(a) {
                    var b = Ob(a);
                    if (a === "") return U(h.length);
                    if (b !== null) return [b >= 0 ? b: h.length + b];
                    if (typeof a === "function") {
                        var d = Ba(c, f);
                        return g.map(h,
                        function(b, f) {
                            return a(f, Vb(c, f, 0, 0, d), j[f]) ? f: null
                        })
                    }
                    var k = typeof a === "string" ? a.match(cc) : "";
                    if (k) switch (k[2]) {
                    case "visIdx":
                    case "visible":
                        b = parseInt(k[1], 10);
                        if (b < 0) {
                            var l = g.map(h,
                            function(a, b) {
                                return a.bVisible ? b: null
                            });
                            return [l[l.length + b]]
                        }
                        return [ka(c, b)];
                    case "name":
                        return g.map(i,
                        function(a, b) {
                            return a === k[1] ? b: null
                        })
                    } else return g(j).filter(a).map(function() {
                        return g.inArray(this, j)
                    }).toArray()
                })
            },
            1);
            c.selector.cols = a;
            c.selector.opts = b;
            return c
        });
        t("columns().header()", "column().header()",
        function() {
            return this.iterator("column",
            function(a, b) {
                return a.aoColumns[b].nTh
            },
            1)
        });
        t("columns().footer()", "column().footer()",
        function() {
            return this.iterator("column",
            function(a, b) {
                return a.aoColumns[b].nTf
            },
            1)
        });
        t("columns().data()", "column().data()",
        function() {
            return this.iterator("column-rows", Vb, 1)
        });
        t("columns().dataSrc()", "column().dataSrc()",
        function() {
            return this.iterator("column",
            function(a, b) {
                return a.aoColumns[b].mData
            },
            1)
        });
        t("columns().cache()", "column().cache()",
        function(a) {
            return this.iterator("column-rows",
            function(b, c, e, d, f) {
                return ha(b.aoData, f, "search" === a ? "_aFilterData": "_aSortData", c)
            },
            1)
        });
        t("columns().nodes()", "column().nodes()",
        function() {
            return this.iterator("column-rows",
            function(a, b, c, e, d) {
                return ha(a.aoData, d, "anCells", b)
            },
            1)
        });
        t("columns().visible()", "column().visible()",
        function(a, b) {
            return this.iterator("column",
            function(c, e) {
                if (a === l) return c.aoColumns[e].bVisible;
                var d = c.aoColumns,
                f = d[e],
                h = c.aoData,
                i,
                j,
                n;
                if (a !== l && f.bVisible !== a) {
                    if (a) {
                        var m = g.inArray(!0, C(d, "bVisible"), e + 1);
                        i = 0;
                        for (j = h.length; i < j; i++) n = h[i].nTr,
                        d = h[i].anCells,
                        n && n.insertBefore(d[e], d[m] || null)
                    } else g(C(c.aoData, "anCells", e)).detach();
                    f.bVisible = a;
                    ea(c, c.aoHeader);
                    ea(c, c.aoFooter);
                    if (b === l || b) X(c),
                    (c.oScroll.sX || c.oScroll.sY) && Y(c);
                    u(c, null, "column-visibility", [c, e, a]);
                    xa(c)
                }
            })
        });
        t("columns().indexes()", "column().index()",
        function(a) {
            return this.iterator("column",
            function(b, c) {
                return "visible" === a ? $(b, c) : c
            },
            1)
        });
        r("columns.adjust()",
        function() {
            return this.iterator("table",
            function(a) {
                X(a)
            },
            1)
        });
        r("column.index()",
        function(a, b) {
            if (0 !== this.context.length) {
                var c = this.context[0];
                if ("fromVisible" === a || "toData" === a) return ka(c, b);
                if ("fromData" === a || "toVisible" === a) return $(c, b)
            }
        });
        r("column()",
        function(a, b) {
            return ab(this.columns(a, b))
        });
        r("cells()",
        function(a, b, c) {
            g.isPlainObject(a) && (typeof a.row !== l ? (c = b, b = null) : (c = a, a = null));
            g.isPlainObject(b) && (c = b, b = null);
            if (null === b || b === l) return this.iterator("table",
            function(b) {
                var e = a,
                d = $a(c),
                f = b.aoData,
                h = Ba(b, d),
                d = Rb(ha(f, h, "anCells")),
                i = g([].concat.apply([], d)),
                j,
                m = b.aoColumns.length,
                n,
                p,
                r,
                q,
                s,
                t;
                return Za(e,
                function(a) {
                    var c = typeof a === "function";
                    if (a === null || a === l || c) {
                        n = [];
                        p = 0;
                        for (r = h.length; p < r; p++) {
                            j = h[p];
                            for (q = 0; q < m; q++) {
                                s = {
                                    row: j,
                                    column: q
                                };
                                if (c) {
                                    t = b.aoData[j];
                                    a(s, v(b, j, q), t.anCells[q]) && n.push(s)
                                } else n.push(s)
                            }
                        }
                        return n
                    }
                    return g.isPlainObject(a) ? [a] : i.filter(a).map(function(a, b) {
                        j = b.parentNode._DT_RowIndex;
                        return {
                            row: j,
                            column: g.inArray(b, f[j].anCells)
                        }
                    }).toArray()
                })
            });
            var e = this.columns(b, c),
            d = this.rows(a, c),
            f,
            h,
            i,
            j,
            n,
            m = this.iterator("table",
            function(a, b) {
                f = [];
                h = 0;
                for (i = d[b].length; h < i; h++) {
                    j = 0;
                    for (n = e[b].length; j < n; j++) f.push({
                        row: d[b][h],
                        column: e[b][j]
                    })
                }
                return f
            },
            1);
            g.extend(m.selector, {
                cols: b,
                rows: a,
                opts: c
            });
            return m
        });
        t("cells().nodes()", "cell().node()",
        function() {
            return this.iterator("cell",
            function(a, b, c) {
                return (a = a.aoData[b].anCells) ? a[c] : l
            },
            1)
        });
        r("cells().data()",
        function() {
            return this.iterator("cell",
            function(a, b, c) {
                return v(a, b, c)
            },
            1)
        });
        t("cells().cache()", "cell().cache()",
        function(a) {
            a = "search" === a ? "_aFilterData": "_aSortData";
            return this.iterator("cell",
            function(b, c, e) {
                return b.aoData[c][a][e]
            },
            1)
        });
        t("cells().render()", "cell().render()",
        function(a) {
            return this.iterator("cell",
            function(b, c, e) {
                return v(b, c, e, a)
            },
            1)
        });
        t("cells().indexes()", "cell().index()",
        function() {
            return this.iterator("cell",
            function(a, b, c) {
                return {
                    row: b,
                    column: c,
                    columnVisible: $(a, c)
                }
            },
            1)
        });
        t("cells().invalidate()", "cell().invalidate()",
        function(a) {
            return this.iterator("cell",
            function(b, c, e) {
                ca(b, c, a, e)
            })
        });
        r("cell()",
        function(a, b, c) {
            return ab(this.cells(a, b, c))
        });
        r("cell().data()",
        function(a) {
            var b = this.context,
            c = this[0];
            if (a === l) return b.length && c.length ? v(b[0], c[0].row, c[0].column) : l;
            Ha(b[0], c[0].row, c[0].column, a);
            ca(b[0], c[0].row, "data", c[0].column);
            return this
        });
        r("order()",
        function(a, b) {
            var c = this.context;
            if (a === l) return 0 !== c.length ? c[0].aaSorting: l;
            "number" === typeof a ? a = [[a, b]] : g.isArray(a[0]) || (a = Array.prototype.slice.call(arguments));
            return this.iterator("table",
            function(b) {
                b.aaSorting = a.slice()
            })
        });
        r("order.listener()",
        function(a, b, c) {
            return this.iterator("table",
            function(e) {
                Na(e, a, b, c)
            })
        });
        r(["columns().order()", "column().order()"],
        function(a) {
            var b = this;
            return this.iterator("table",
            function(c, e) {
                var d = [];
                g.each(b[e],
                function(b, c) {
                    d.push([c, a])
                });
                c.aaSorting = d
            })
        });
        r("search()",
        function(a, b, c, e) {
            var d = this.context;
            return a === l ? 0 !== d.length ? d[0].oPreviousSearch.sSearch: l: this.iterator("table",
            function(d) {
                d.oFeatures.bFilter && fa(d, g.extend({},
                d.oPreviousSearch, {
                    sSearch: a + "",
                    bRegex: null === b ? !1 : b,
                    bSmart: null === c ? !0 : c,
                    bCaseInsensitive: null === e ? !0 : e
                }), 1)
            })
        });
        t("columns().search()", "column().search()",
        function(a, b, c, e) {
            return this.iterator("column",
            function(d, f) {
                var h = d.aoPreSearchCols;
                if (a === l) return h[f].sSearch;
                d.oFeatures.bFilter && (g.extend(h[f], {
                    sSearch: a + "",
                    bRegex: null === b ? !1 : b,
                    bSmart: null === c ? !0 : c,
                    bCaseInsensitive: null === e ? !0 : e
                }), fa(d, d.oPreviousSearch, 1))
            })
        });
        r("state()",
        function() {
            return this.context.length ? this.context[0].oSavedState: null
        });
        r("state.clear()",
        function() {
            return this.iterator("table",
            function(a) {
                a.fnStateSaveCallback.call(a.oInstance, a, {})
            })
        });
        r("state.loaded()",
        function() {
            return this.context.length ? this.context[0].oLoadedState: null
        });
        r("state.save()",
        function() {
            return this.iterator("table",
            function(a) {
                xa(a)
            })
        });
        p.versionCheck = p.fnVersionCheck = function(a) {
            for (var b = p.version.split("."), a = a.split("."), c, e, d = 0, f = a.length; d < f; d++) if (c = parseInt(b[d], 10) || 0, e = parseInt(a[d], 10) || 0, c !== e) return c > e;
            return ! 0
        };
        p.isDataTable = p.fnIsDataTable = function(a) {
            var b = g(a).get(0),
            c = !1;
            g.each(p.settings,
            function(a, d) {
                if (d.nTable === b || d.nScrollHead === b || d.nScrollFoot === b) c = !0
            });
            return c
        };
        p.tables = p.fnTables = function(a) {
            return g.map(p.settings,
            function(b) {
                if (!a || a && g(b.nTable).is(":visible")) return b.nTable
            })
        };
        p.util = {
            throttle: ta,
            escapeRegex: ua
        };
        p.camelToHungarian = G;
        r("$()",
        function(a, b) {
            var c = this.rows(b).nodes(),
            c = g(c);
            return g([].concat(c.filter(a).toArray(), c.find(a).toArray()))
        });
        g.each(["on", "one", "off"],
        function(a, b) {
            r(b + "()",
            function() {
                var a = Array.prototype.slice.call(arguments);
                a[0].match(/\.dt\b/) || (a[0] += ".dt");
                var e = g(this.tables().nodes());
                e[b].apply(e, a);
                return this
            })
        });
        r("clear()",
        function() {
            return this.iterator("table",
            function(a) {
                na(a)
            })
        });
        r("settings()",
        function() {
            return new q(this.context, this.context)
        });
        r("data()",
        function() {
            return this.iterator("table",
            function(a) {
                return C(a.aoData, "_aData")
            }).flatten()
        });
        r("destroy()",
        function(a) {
            a = a || !1;
            return this.iterator("table",
            function(b) {
                var c = b.nTableWrapper.parentNode,
                e = b.oClasses,
                d = b.nTable,
                f = b.nTBody,
                h = b.nTHead,
                i = b.nTFoot,
                j = g(d),
                f = g(f),
                l = g(b.nTableWrapper),
                m = g.map(b.aoData,
                function(a) {
                    return a.nTr
                }),
                o;
                b.bDestroying = !0;
                u(b, "aoDestroyCallback", "destroy", [b]);
                a || (new q(b)).columns().visible(!0);
                l.unbind(".DT").find(":not(tbody *)").unbind(".DT");
                g(Da).unbind(".DT-" + b.sInstance);
                d != h.parentNode && (j.children("thead").detach(), j.append(h));
                i && d != i.parentNode && (j.children("tfoot").detach(), j.append(i));
                j.detach();
                l.detach();
                b.aaSorting = [];
                b.aaSortingFixed = [];
                wa(b);
                g(m).removeClass(b.asStripeClasses.join(" "));
                g("th, td", h).removeClass(e.sSortable + " " + e.sSortableAsc + " " + e.sSortableDesc + " " + e.sSortableNone);
                b.bJUI && (g("th span." + e.sSortIcon + ", td span." + e.sSortIcon, h).detach(), g("th, td", h).each(function() {
                    var a = g("div." + e.sSortJUIWrapper, this);
                    g(this).append(a.contents());
                    a.detach()
                })); ! a && c && c.insertBefore(d, b.nTableReinsertBefore);
                f.children().detach();
                f.append(m);
                j.css("width", b.sDestroyWidth).removeClass(e.sTable); (o = b.asDestroyStripes.length) && f.children().each(function(a) {
                    g(this).addClass(b.asDestroyStripes[a % o])
                });
                c = g.inArray(b, p.settings); - 1 !== c && p.settings.splice(c, 1)
            })
        });
        p.version = "1.10.4";
        p.settings = [];
        p.models = {};
        p.models.oSearch = {
            bCaseInsensitive: !0,
            sSearch: "",
            bRegex: !1,
            bSmart: !0
        };
        p.models.oRow = {
            nTr: null,
            anCells: null,
            _aData: [],
            _aSortData: null,
            _aFilterData: null,
            _sFilterRow: null,
            _sRowStripe: "",
            src: null
        };
        p.models.oColumn = {
            idx: null,
            aDataSort: null,
            asSorting: null,
            bSearchable: null,
            bSortable: null,
            bVisible: null,
            _sManualType: null,
            _bAttrSrc: !1,
            fnCreatedCell: null,
            fnGetData: null,
            fnSetData: null,
            mData: null,
            mRender: null,
            nTh: null,
            nTf: null,
            sClass: null,
            sContentPadding: null,
            sDefaultContent: null,
            sName: null,
            sSortDataType: "std",
            sSortingClass: null,
            sSortingClassJUI: null,
            sTitle: null,
            sType: null,
            sWidth: null,
            sWidthOrig: null
        };
        p.defaults = {
            aaData: null,
            aaSorting: [[0, "asc"]],
            aaSortingFixed: [],
            ajax: null,
            aLengthMenu: [10, 25, 50, 100],
            aoColumns: null,
            aoColumnDefs: null,
            aoSearchCols: [],
            asStripeClasses: null,
            bAutoWidth: !0,
            bDeferRender: !1,
            bDestroy: !1,
            bFilter: !0,
            bInfo: !0,
            bJQueryUI: !1,
            bLengthChange: !0,
            bPaginate: !0,
            bProcessing: !1,
            bRetrieve: !1,
            bScrollCollapse: !1,
            bServerSide: !1,
            bSort: !0,
            bSortMulti: !0,
            bSortCellsTop: !1,
            bSortClasses: !0,
            bStateSave: !1,
            fnCreatedRow: null,
            fnDrawCallback: null,
            fnFooterCallback: null,
            fnFormatNumber: function(a) {
                return a.toString().replace(/\B(?=(\d{3})+(?!\d))/g, this.oLanguage.sThousands)
            },
            fnHeaderCallback: null,
            fnInfoCallback: null,
            fnInitComplete: null,
            fnPreDrawCallback: null,
            fnRowCallback: null,
            fnServerData: null,
            fnServerParams: null,
            fnStateLoadCallback: function(a) {
                try {
                    return JSON.parse(( - 1 === a.iStateDuration ? sessionStorage: localStorage).getItem("DataTables_" + a.sInstance + "_" + location.pathname))
                } catch(b) {}
            },
            fnStateLoadParams: null,
            fnStateLoaded: null,
            fnStateSaveCallback: function(a, b) {
                try { ( - 1 === a.iStateDuration ? sessionStorage: localStorage).setItem("DataTables_" + a.sInstance + "_" + location.pathname, JSON.stringify(b))
                } catch(c) {}
            },
            fnStateSaveParams: null,
            iStateDuration: 7200,
            iDeferLoading: null,
            iDisplayLength: 10,
            iDisplayStart: 0,
            iTabIndex: 0,
            oClasses: {},
            oLanguage: {
                oAria: {
                    sSortAscending: ": activate to sort column ascending",
                    sSortDescending: ": activate to sort column descending"
                },
                oPaginate: {
                    sFirst: "First",
                    sLast: "Last",
                    sNext: "Next",
                    sPrevious: "Previous"
                },
                sEmptyTable: "No data available in table",
                sInfo: "Showing _START_ to _END_ of _TOTAL_ entries",
                sInfoEmpty: "Showing 0 to 0 of 0 entries",
                sInfoFiltered: "(filtered from _MAX_ total entries)",
                sInfoPostFix: "",
                sDecimal: "",
                sThousands: ",",
                sLengthMenu: "Show _MENU_ entries",
                sLoadingRecords: "Loading...",
                sProcessing: "Processing...",
                sSearch: "Search:",
                sSearchPlaceholder: "",
                sUrl: "",
                sZeroRecords: "No matching records found"
            },
            oSearch: g.extend({},
            p.models.oSearch),
            sAjaxDataProp: "data",
            sAjaxSource: null,
            sDom: "lfrtip",
            searchDelay: null,
            sPaginationType: "simple_numbers",
            sScrollX: "",
            sScrollXInner: "",
            sScrollY: "",
            sServerMethod: "GET",
            renderer: null
        };
        V(p.defaults);
        p.defaults.column = {
            aDataSort: null,
            iDataSort: -1,
            asSorting: ["desc", "asc"],
            bSearchable: !0,
            bSortable: !0,
            bVisible: !0,
            fnCreatedCell: null,
            mData: null,
            mRender: null,
            sCellType: "td",
            sClass: "",
            sContentPadding: "",
            sDefaultContent: null,
            sName: "",
            sSortDataType: "std",
            sTitle: null,
            sType: null,
            sWidth: null
        };
        V(p.defaults.column);
        p.models.oSettings = {
            oFeatures: {
                bAutoWidth: null,
                bDeferRender: null,
                bFilter: null,
                bInfo: null,
                bLengthChange: null,
                bPaginate: null,
                bProcessing: null,
                bServerSide: null,
                bSort: null,
                bSortMulti: null,
                bSortClasses: null,
                bStateSave: null
            },
            oScroll: {
                bCollapse: null,
                iBarWidth: 0,
                sX: null,
                sXInner: null,
                sY: null
            },
            oLanguage: {
                fnInfoCallback: null
            },
            oBrowser: {
                bScrollOversize: !1,
                bScrollbarLeft: !1
            },
            ajax: null,
            aanFeatures: [],
            aoData: [],
            aiDisplay: [],
            aiDisplayMaster: [],
            aoColumns: [],
            aoHeader: [],
            aoFooter: [],
            oPreviousSearch: {},
            aoPreSearchCols: [],
            aaSorting: null,
            aaSortingFixed: [],
            asStripeClasses: null,
            asDestroyStripes: [],
            sDestroyWidth: 0,
            aoRowCallback: [],
            aoHeaderCallback: [],
            aoFooterCallback: [],
            aoDrawCallback: [],
            aoRowCreatedCallback: [],
            aoPreDrawCallback: [],
            aoInitComplete: [],
            aoStateSaveParams: [],
            aoStateLoadParams: [],
            aoStateLoaded: [],
            sTableId: "",
            nTable: null,
            nTHead: null,
            nTFoot: null,
            nTBody: null,
            nTableWrapper: null,
            bDeferLoading: !1,
            bInitialised: !1,
            aoOpenRows: [],
            sDom: null,
            searchDelay: null,
            sPaginationType: "two_button",
            iStateDuration: 0,
            aoStateSave: [],
            aoStateLoad: [],
            oSavedState: null,
            oLoadedState: null,
            sAjaxSource: null,
            sAjaxDataProp: null,
            bAjaxDataGet: !0,
            jqXHR: null,
            json: l,
            oAjaxData: l,
            fnServerData: null,
            aoServerParams: [],
            sServerMethod: null,
            fnFormatNumber: null,
            aLengthMenu: null,
            iDraw: 0,
            bDrawing: !1,
            iDrawError: -1,
            _iDisplayLength: 10,
            _iDisplayStart: 0,
            _iRecordsTotal: 0,
            _iRecordsDisplay: 0,
            bJUI: null,
            oClasses: {},
            bFiltered: !1,
            bSorted: !1,
            bSortCellsTop: null,
            oInit: null,
            aoDestroyCallback: [],
            fnRecordsTotal: function() {
                return "ssp" == A(this) ? 1 * this._iRecordsTotal: this.aiDisplayMaster.length
            },
            fnRecordsDisplay: function() {
                return "ssp" == A(this) ? 1 * this._iRecordsDisplay: this.aiDisplay.length
            },
            fnDisplayEnd: function() {
                var a = this._iDisplayLength,
                b = this._iDisplayStart,
                c = b + a,
                e = this.aiDisplay.length,
                d = this.oFeatures,
                f = d.bPaginate;
                return d.bServerSide ? !1 === f || -1 === a ? b + e: Math.min(b + a, this._iRecordsDisplay) : !f || c > e || -1 === a ? e: c
            },
            oInstance: null,
            sInstance: null,
            iTabIndex: 0,
            nScrollHead: null,
            nScrollFoot: null,
            aLastSort: [],
            oPlugins: {}
        };
        p.ext = w = {
            classes: {},
            errMode: "alert",
            feature: [],
            search: [],
            internal: {},
            legacy: {
                ajax: null
            },
            pager: {},
            renderer: {
                pageButton: {},
                header: {}
            },
            order: {},
            type: {
                detect: [],
                search: {},
                order: {}
            },
            _unique: 0,
            fnVersionCheck: p.fnVersionCheck,
            iApiIndex: 0,
            oJUIClasses: {},
            sVersion: p.version
        };
        g.extend(w, {
            afnFiltering: w.search,
            aTypes: w.type.detect,
            ofnSearch: w.type.search,
            oSort: w.type.order,
            afnSortData: w.order,
            aoFeatures: w.feature,
            oApi: w.internal,
            oStdClasses: w.classes,
            oPagination: w.pager
        });
        g.extend(p.ext.classes, {
            sTable: "dataTable",
            sNoFooter: "no-footer",
            sPageButton: "paginate_button",
            sPageButtonActive: "current",
            sPageButtonDisabled: "disabled",
            sStripeOdd: "odd",
            sStripeEven: "even",
            sRowEmpty: "dataTables_empty",
            sWrapper: "dataTables_wrapper",
            sFilter: "dataTables_filter",
            sInfo: "dataTables_info",
            sPaging: "dataTables_paginate paging_",
            sLength: "dataTables_length",
            sProcessing: "dataTables_processing",
            sSortAsc: "sorting_asc",
            sSortDesc: "sorting_desc",
            sSortable: "sorting",
            sSortableAsc: "sorting_asc_disabled",
            sSortableDesc: "sorting_desc_disabled",
            sSortableNone: "sorting_disabled",
            sSortColumn: "sorting_",
            sFilterInput: "",
            sLengthSelect: "",
            sScrollWrapper: "dataTables_scroll",
            sScrollHead: "dataTables_scrollHead",
            sScrollHeadInner: "dataTables_scrollHeadInner",
            sScrollBody: "dataTables_scrollBody",
            sScrollFoot: "dataTables_scrollFoot",
            sScrollFootInner: "dataTables_scrollFootInner",
            sHeaderTH: "",
            sFooterTH: "",
            sSortJUIAsc: "",
            sSortJUIDesc: "",
            sSortJUI: "",
            sSortJUIAscAllowed: "",
            sSortJUIDescAllowed: "",
            sSortJUIWrapper: "",
            sSortIcon: "",
            sJUIHeader: "",
            sJUIFooter: ""
        });
        var Ca = "",
        Ca = "",
        E = Ca + "ui-state-default",
        ia = Ca + "css_right ui-icon ui-icon-",
        Wb = Ca + "fg-toolbar ui-toolbar ui-widget-header ui-helper-clearfix";
        g.extend(p.ext.oJUIClasses, p.ext.classes, {
            sPageButton: "fg-button ui-button " + E,
            sPageButtonActive: "ui-state-disabled",
            sPageButtonDisabled: "ui-state-disabled",
            sPaging: "dataTables_paginate fg-buttonset ui-buttonset fg-buttonset-multi ui-buttonset-multi paging_",
            sSortAsc: E + " sorting_asc",
            sSortDesc: E + " sorting_desc",
            sSortable: E + " sorting",
            sSortableAsc: E + " sorting_asc_disabled",
            sSortableDesc: E + " sorting_desc_disabled",
            sSortableNone: E + " sorting_disabled",
            sSortJUIAsc: ia + "triangle-1-n",
            sSortJUIDesc: ia + "triangle-1-s",
            sSortJUI: ia + "carat-2-n-s",
            sSortJUIAscAllowed: ia + "carat-1-n",
            sSortJUIDescAllowed: ia + "carat-1-s",
            sSortJUIWrapper: "DataTables_sort_wrapper",
            sSortIcon: "DataTables_sort_icon",
            sScrollHead: "dataTables_scrollHead " + E,
            sScrollFoot: "dataTables_scrollFoot " + E,
            sHeaderTH: E,
            sFooterTH: E,
            sJUIHeader: Wb + " ui-corner-tl ui-corner-tr",
            sJUIFooter: Wb + " ui-corner-bl ui-corner-br"
        });
        var Lb = p.ext.pager;
        g.extend(Lb, {
            simple: function() {
                return ["previous", "next"]
            },
            full: function() {
                return ["first", "previous", "next", "last"]
            },
            simple_numbers: function(a, b) {
                return ["previous", Va(a, b), "next"]
            },
            full_numbers: function(a, b) {
                return ["first", "previous", Va(a, b), "next", "last"]
            },
            _numbers: Va,
            numbers_length: 7
        });
        g.extend(!0, p.ext.renderer, {
            pageButton: {
                _: function(a, b, c, e, d, f) {
                    var h = a.oClasses,
                    i = a.oLanguage.oPaginate,
                    j, l, m = 0,
                    o = function(b, e) {
                        var k, p, r, q, s = function(b) {
                            Sa(a, b.data.action, true)
                        };
                        k = 0;
                        for (p = e.length; k < p; k++) {
                            q = e[k];
                            if (g.isArray(q)) {
                                r = g("<" + (q.DT_el || "div") + "/>").appendTo(b);
                                o(r, q)
                            } else {
                                l = j = "";
                                switch (q) {
                                case "ellipsis":
                                    b.append("<span>&hellip;</span>");
                                    break;
                                case "first":
                                    j = i.sFirst;
                                    l = q + (d > 0 ? "": " " + h.sPageButtonDisabled);
                                    break;
                                case "previous":
                                    j = i.sPrevious;
                                    l = q + (d > 0 ? "": " " + h.sPageButtonDisabled);
                                    break;
                                case "next":
                                    j = i.sNext;
                                    l = q + (d < f - 1 ? "": " " + h.sPageButtonDisabled);
                                    break;
                                case "last":
                                    j = i.sLast;
                                    l = q + (d < f - 1 ? "": " " + h.sPageButtonDisabled);
                                    break;
                                default:
                                    j = q + 1;
                                    l = d === q ? h.sPageButtonActive: ""
                                }
                                if (j) {
                                    r = g("<a>", {
                                        "class": h.sPageButton + " " + l,
                                        "aria-controls": a.sTableId,
                                        "data-dt-idx": m,
                                        tabindex: a.iTabIndex,
                                        id: c === 0 && typeof q === "string" ? a.sTableId + "_" + q: null
                                    }).html(j).appendTo(b);
                                    Ua(r, {
                                        action: q
                                    },
                                    s);
                                    m++
                                }
                            }
                        }
                    };
                    try {
                        var k = g(P.activeElement).data("dt-idx");
                        o(g(b).empty(), e);
                        k !== null && g(b).find("[data-dt-idx=" + k + "]").focus()
                    } catch(p) {}
                }
            }
        });
        g.extend(p.ext.type.detect, [function(a, b) {
            var c = b.oLanguage.sDecimal;
            return Ya(a, c) ? "num" + c: null
        },
        function(a) {
            if (a && !(a instanceof Date) && (!$b.test(a) || !ac.test(a))) return null;
            var b = Date.parse(a);
            return null !== b && !isNaN(b) || H(a) ? "date": null
        },
        function(a, b) {
            var c = b.oLanguage.sDecimal;
            return Ya(a, c, !0) ? "num-fmt" + c: null
        },
        function(a, b) {
            var c = b.oLanguage.sDecimal;
            return Qb(a, c) ? "html-num" + c: null
        },
        function(a, b) {
            var c = b.oLanguage.sDecimal;
            return Qb(a, c, !0) ? "html-num-fmt" + c: null
        },
        function(a) {
            return H(a) || "string" === typeof a && -1 !== a.indexOf("<") ? "html": null
        }]);
        g.extend(p.ext.type.search, {
            html: function(a) {
                return H(a) ? a: "string" === typeof a ? a.replace(Nb, " ").replace(Aa, "") : ""
            },
            string: function(a) {
                return H(a) ? a: "string" === typeof a ? a.replace(Nb, " ") : a
            }
        });
        var za = function(a, b, c, e) {
            if (0 !== a && (!a || "-" === a)) return - Infinity;
            b && (a = Pb(a, b));
            a.replace && (c && (a = a.replace(c, "")), e && (a = a.replace(e, "")));
            return 1 * a
        };
        g.extend(w.type.order, {
            "date-pre": function(a) {
                return Date.parse(a) || 0
            },
            "html-pre": function(a) {
                return H(a) ? "": a.replace ? a.replace(/<.*?>/g, "").toLowerCase() : a + ""
            },
            "string-pre": function(a) {
                return H(a) ? "": "string" === typeof a ? a.toLowerCase() : !a.toString ? "": a.toString()
            },
            "string-asc": function(a, b) {
                return a < b ? -1 : a > b ? 1 : 0
            },
            "string-desc": function(a, b) {
                return a < b ? 1 : a > b ? -1 : 0
            }
        });
        cb("");
        g.extend(!0, p.ext.renderer, {
            header: {
                _: function(a, b, c, e) {
                    g(a.nTable).on("order.dt.DT",
                    function(d, f, h, g) {
                        if (a === f) {
                            d = c.idx;
                            b.removeClass(c.sSortingClass + " " + e.sSortAsc + " " + e.sSortDesc).addClass(g[d] == "asc" ? e.sSortAsc: g[d] == "desc" ? e.sSortDesc: c.sSortingClass)
                        }
                    })
                },
                jqueryui: function(a, b, c, e) {
                    g("<div/>").addClass(e.sSortJUIWrapper).append(b.contents()).append(g("<span/>").addClass(e.sSortIcon + " " + c.sSortingClassJUI)).appendTo(b);
                    g(a.nTable).on("order.dt.DT",
                    function(d, f, g, i) {
                        if (a === f) {
                            d = c.idx;
                            b.removeClass(e.sSortAsc + " " + e.sSortDesc).addClass(i[d] == "asc" ? e.sSortAsc: i[d] == "desc" ? e.sSortDesc: c.sSortingClass);
                            b.find("span." + e.sSortIcon).removeClass(e.sSortJUIAsc + " " + e.sSortJUIDesc + " " + e.sSortJUI + " " + e.sSortJUIAscAllowed + " " + e.sSortJUIDescAllowed).addClass(i[d] == "asc" ? e.sSortJUIAsc: i[d] == "desc" ? e.sSortJUIDesc: c.sSortingClassJUI)
                        }
                    })
                }
            }
        });
        p.render = {
            number: function(a, b, c, e) {
                return {
                    display: function(d) {
                        var f = 0 > d ? "-": "",
                        d = Math.abs(parseFloat(d)),
                        g = parseInt(d, 10),
                        d = c ? b + (d - g).toFixed(c).substring(2) : "";
                        return f + (e || "") + g.toString().replace(/\B(?=(\d{3})+(?!\d))/g, a) + d
                    }
                }
            }
        };
        g.extend(p.ext.internal, {
            _fnExternApiFunc: Mb,
            _fnBuildAjax: qa,
            _fnAjaxUpdate: jb,
            _fnAjaxParameters: sb,
            _fnAjaxUpdateDraw: tb,
            _fnAjaxDataSrc: ra,
            _fnAddColumn: Ea,
            _fnColumnOptions: ja,
            _fnAdjustColumnSizing: X,
            _fnVisibleToColumnIndex: ka,
            _fnColumnIndexToVisible: $,
            _fnVisbleColumns: aa,
            _fnGetColumns: Z,
            _fnColumnTypes: Ga,
            _fnApplyColumnDefs: hb,
            _fnHungarianMap: V,
            _fnCamelToHungarian: G,
            _fnLanguageCompat: O,
            _fnBrowserDetect: fb,
            _fnAddData: I,
            _fnAddTr: la,
            _fnNodeToDataIndex: function(a, b) {
                return b._DT_RowIndex !== l ? b._DT_RowIndex: null
            },
            _fnNodeToColumnIndex: function(a, b, c) {
                return g.inArray(c, a.aoData[b].anCells)
            },
            _fnGetCellData: v,
            _fnSetCellData: Ha,
            _fnSplitObjNotation: Ja,
            _fnGetObjectDataFn: W,
            _fnSetObjectDataFn: Q,
            _fnGetDataMaster: Ka,
            _fnClearTable: na,
            _fnDeleteIndex: oa,
            _fnInvalidate: ca,
            _fnGetRowElements: ma,
            _fnCreateTr: Ia,
            _fnBuildHead: ib,
            _fnDrawHead: ea,
            _fnDraw: L,
            _fnReDraw: M,
            _fnAddOptionsHtml: lb,
            _fnDetectHeader: da,
            _fnGetUniqueThs: pa,
            _fnFeatureHtmlFilter: nb,
            _fnFilterComplete: fa,
            _fnFilterCustom: wb,
            _fnFilterColumn: vb,
            _fnFilter: ub,
            _fnFilterCreateSearch: Pa,
            _fnEscapeRegex: ua,
            _fnFilterData: xb,
            _fnFeatureHtmlInfo: qb,
            _fnUpdateInfo: Ab,
            _fnInfoMacros: Bb,
            _fnInitialise: ga,
            _fnInitComplete: sa,
            _fnLengthChange: Qa,
            _fnFeatureHtmlLength: mb,
            _fnFeatureHtmlPaginate: rb,
            _fnPageChange: Sa,
            _fnFeatureHtmlProcessing: ob,
            _fnProcessingDisplay: B,
            _fnFeatureHtmlTable: pb,
            _fnScrollDraw: Y,
            _fnApplyToChildren: F,
            _fnCalculateColumnWidths: Fa,
            _fnThrottle: ta,
            _fnConvertToWidth: Cb,
            _fnScrollingWidthAdjust: Eb,
            _fnGetWidestNode: Db,
            _fnGetMaxLenString: Fb,
            _fnStringToCss: s,
            _fnScrollBarWidth: Gb,
            _fnSortFlatten: T,
            _fnSort: kb,
            _fnSortAria: Ib,
            _fnSortListener: Ta,
            _fnSortAttachListener: Na,
            _fnSortingClasses: wa,
            _fnSortData: Hb,
            _fnSaveState: xa,
            _fnLoadState: Jb,
            _fnSettingsFromNode: ya,
            _fnLog: R,
            _fnMap: D,
            _fnBindAction: Ua,
            _fnCallbackReg: x,
            _fnCallbackFire: u,
            _fnLengthOverflow: Ra,
            _fnRenderer: Oa,
            _fnDataSource: A,
            _fnRowAttributes: La,
            _fnCalculateEnd: function() {}
        });
        g.fn.dataTable = p;
        g.fn.dataTableSettings = p.settings;
        g.fn.dataTableExt = p.ext;
        g.fn.DataTable = function(a) {
            return g(this).dataTable(a).api()
        };
        g.each(p,
        function(a, b) {
            g.fn.DataTable[a] = b
        });
        return g.fn.dataTable
    };
    "function" === typeof define && define.amd ? define("datatables", ["jquery"], O) : "object" === typeof exports ? O(require("jquery")) : jQuery && !jQuery.fn.dataTable && O(jQuery)
})(window, document);
/*
   Copyright 2008-2014 SpryMedia Ltd.

 This source file is free software, available under the following license:
   MIT license - http://datatables.net/license

 This source file is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 or FITNESS FOR A PARTICULAR PURPOSE. See the license files for details.

 For details please refer to: http://www.datatables.net
*/
(function(Ca, O, ic) { (function(g) {
        "function" === typeof define && define.amd ? define("datatables", ["jquery"], g) : "object" === typeof exports ? module.exports = g(require("jquery")) : jQuery && !jQuery.fn.hrdataTable && g(jQuery)
    })(function(g) {
        function U(a) {
            var b, c, d = {};
            g.each(a,
            function(e, f) { (b = e.match(/^([^A-Z]+?)([A-Z])/)) && -1 !== "a aa ai ao as b fn i m o s ".indexOf(b[1] + " ") && (c = e.replace(b[0], b[2].toLowerCase()), d[c] = e, "o" === b[1] && U(a[e]))
            });
            a._hungarianMap = d
        }
        function H(a, b, c) {
            a._hungarianMap || U(a);
            var d;
            g.each(b,
            function(e, f) {
                d = a._hungarianMap[e];
                void 0 === d || !c && void 0 !== b[d] || ("o" === d.charAt(0) ? (b[d] || (b[d] = {}), g.extend(!0, b[d], b[e]), H(a[d], b[d], c)) : b[d] = b[e])
            })
        }
        function Da(a) {
            var b = p.defaults.oLanguage,
            c = a.sZeroRecords; ! a.sEmptyTable && c && "No data available in table" === b.sEmptyTable && G(a, a, "sZeroRecords", "sEmptyTable"); ! a.sLoadingRecords && c && "Loading..." === b.sLoadingRecords && G(a, a, "sZeroRecords", "sLoadingRecords");
            a.sInfoThousands && (a.sThousands = a.sInfoThousands); (a = a.sDecimal) && cb(a)
        }
        function db(a) {
            C(a, "ordering", "bSort");
            C(a, "orderMulti", "bSortMulti");
            C(a, "orderClasses", "bSortClasses");
            C(a, "orderCellsTop", "bSortCellsTop");
            C(a, "order", "aaSorting");
            C(a, "orderFixed", "aaSortingFixed");
            C(a, "paging", "bPaginate");
            C(a, "pagingType", "sPaginationType");
            C(a, "pageLength", "iDisplayLength");
            C(a, "searching", "bFilter");
            if (a = a.aoSearchCols) for (var b = 0,
            c = a.length; b < c; b++) a[b] && H(p.models.oSearch, a[b])
        }
        function eb(a) {
            C(a, "orderable", "bSortable");
            C(a, "orderData", "aDataSort");
            C(a, "orderSequence", "asSorting");
            C(a, "orderDataType", "sortDataType")
        }
        function fb(a) {
            a = a.oBrowser;
            var b = g("\x3cdiv/\x3e").css({
                position: "absolute",
                top: 0,
                left: 0,
                height: 1,
                width: 1,
                overflow: "hidden"
            }).append(g("\x3cdiv/\x3e").css({
                position: "absolute",
                top: 1,
                left: 1,
                width: 100,
                overflow: "scroll"
            }).append(g('\x3cdiv class\x3d"test"/\x3e').css({
                width: "100%",
                height: 10
            }))).appendTo("body"),
            c = b.find(".test");
            a.bScrollOversize = 100 === c[0].offsetWidth;
            a.bScrollbarLeft = 1 !== c.offset().left;
            b.remove()
        }
        function gb(a, b, c, d, e, f) {
            var h, k = !1;
            void 0 !== c && (h = c, k = !0);
            for (; d !== e;) a.hasOwnProperty(d) && (h = k ? b(h, a[d], d, a) : a[d], k = !0, d += f);
            return h
        }
        function Ea(a, b) {
            var c = p.defaults.column,
            d = a.aoColumns.length,
            c = g.extend({},
            p.models.oColumn, c, {
                nTh: b ? b: O.createElement("th"),
                sTitle: c.sTitle ? c.sTitle: b ? b.innerHTML: "",
                aDataSort: c.aDataSort ? c.aDataSort: [d],
                mData: c.mData ? c.mData: d,
                idx: d
            });
            a.aoColumns.push(c);
            c = a.aoPreSearchCols;
            c[d] = g.extend({},
            p.models.oSearch, c[d]);
            ia(a, d, g(b).data())
        }
        function ia(a, b, c) {
            b = a.aoColumns[b];
            var d = a.oClasses,
            e = g(b.nTh);
            if (!b.sWidthOrig) {
                b.sWidthOrig = e.attr("width") || null;
                var f = (e.attr("style") || "").match(/width:\s*(\d+[pxem%]+)/);
                f && (b.sWidthOrig = f[1])
            }
            void 0 !== c && null !== c && (eb(c), H(p.defaults.column, c), void 0 === c.mDataProp || c.mData || (c.mData = c.mDataProp), c.sType && (b._sManualType = c.sType), c.className && !c.sClass && (c.sClass = c.className), g.extend(b, c), G(b, c, "sWidth", "sWidthOrig"), "number" === typeof c.iDataSort && (b.aDataSort = [c.iDataSort]), G(b, c, "aDataSort"));
            var h = b.mData,
            k = V(h),
            l = b.mRender ? V(b.mRender) : null;
            c = function(a) {
                return "string" === typeof a && -1 !== a.indexOf("@")
            };
            b._bAttrSrc = g.isPlainObject(h) && (c(h.sort) || c(h.type) || c(h.filter));
            b.fnGetData = function(a, b, c) {
                var d = k(a, b, void 0, c);
                return l && b ? l(d, b, a, c) : d
            };
            b.fnSetData = function(a, b, c) {
                return P(h)(a, b, c)
            };
            "number" !== typeof h && (a._rowReadObject = !0);
            a.oFeatures.bSort || (b.bSortable = !1, e.addClass(d.sSortableNone));
            a = -1 !== g.inArray("asc", b.asSorting);
            c = -1 !== g.inArray("desc", b.asSorting);
            b.bSortable && (a || c) ? a && !c ? (b.sSortingClass = d.sSortableAsc, b.sSortingClassJUI = d.sSortJUIAscAllowed) : !a && c ? (b.sSortingClass = d.sSortableDesc, b.sSortingClassJUI = d.sSortJUIDescAllowed) : (b.sSortingClass = d.sSortable, b.sSortingClassJUI = d.sSortJUI) : (b.sSortingClass = d.sSortableNone, b.sSortingClassJUI = "")
        }
        function W(a) {
            if (!1 !== a.oFeatures.bAutoWidth) {
                var b = a.aoColumns;
                Fa(a);
                for (var c = 0,
                d = b.length; c < d; c++) b[c].nTh.style.width = b[c].sWidth
            }
            b = a.oScroll;
            "" === b.sY && "" === b.sX || X(a);
            x(a, null, "column-sizing", [a])
        }
        function ja(a, b) {
            var c = Y(a, "bVisible");
            return "number" === typeof c[b] ? c[b] : null
        }
        function Z(a, b) {
            var c = Y(a, "bVisible"),
            c = g.inArray(b, c);
            return - 1 !== c ? c: null
        }
        function $(a) {
            return Y(a, "bVisible").length
        }
        function Y(a, b) {
            var c = [];
            g.map(a.aoColumns,
            function(a, e) {
                a[b] && c.push(e)
            });
            return c
        }
        function Ga(a) {
            var b = a.aoColumns,
            c = a.aoData,
            d = p.ext.type.detect,
            e, f, h, k, l, g, m, r, q;
            e = 0;
            for (f = b.length; e < f; e++) if (m = b[e], q = [], !m.sType && m._sManualType) m.sType = m._sManualType;
            else if (!m.sType) {
                h = 0;
                for (k = d.length; h < k; h++) {
                    l = 0;
                    for (g = c.length; l < g; l++) {
                        void 0 === q[l] && (q[l] = A(a, l, e, "type"));
                        r = d[h](q[l], a);
                        if (!r && h !== d.length - 1) break;
                        if ("html" === r) break
                    }
                    if (r) {
                        m.sType = r;
                        break
                    }
                }
                m.sType || (m.sType = "string")
            }
        }
        function hb(a, b, c, d) {
            var e, f, h, k, l, n, m = a.aoColumns;
            if (b) for (e = b.length - 1; 0 <= e; e--) {
                n = b[e];
                var r = void 0 !== n.targets ? n.targets: n.aTargets;
                g.isArray(r) || (r = [r]);
                f = 0;
                for (h = r.length; f < h; f++) if ("number" === typeof r[f] && 0 <= r[f]) {
                    for (; m.length <= r[f];) Ea(a);
                    d(r[f], n)
                } else if ("number" === typeof r[f] && 0 > r[f]) d(m.length + r[f], n);
                else if ("string" === typeof r[f]) for (k = 0, l = m.length; k < l; k++)("_all" == r[f] || g(m[k].nTh).hasClass(r[f])) && d(k, n)
            }
            if (c) for (e = 0, a = c.length; e < a; e++) d(e, c[e])
        }
        function K(a, b, c, d) {
            var e = a.aoData.length,
            f = g.extend(!0, {},
            p.models.oRow, {
                src: c ? "dom": "data"
            });
            f._aData = b;
            a.aoData.push(f);
            b = a.aoColumns;
            for (var f = 0,
            h = b.length; f < h; f++) c && Ha(a, e, f, A(a, e, f)),
            b[f].sType = null;
            a.aiDisplayMaster.push(e); ! c && a.oFeatures.bDeferRender || Ia(a, e, c, d);
            return e
        }
        function ka(a, b) {
            var c;
            b instanceof g || (b = g(b));
            return b.map(function(b, e) {
                c = la(a, e);
                return K(a, c.data, e, c.cells)
            })
        }
        function A(a, b, c, d) {
            var e = a.iDraw,
            f = a.aoColumns[c],
            h = a.aoData[b]._aData,
            k = f.sDefaultContent;
            c = f.fnGetData(h, d, {
                settings: a,
                row: b,
                col: c
            });
            if (void 0 === c) return a.iDrawError != e && null === k && (Q(a, 0, "Requested unknown parameter " + ("function" == typeof f.mData ? "{function}": "'" + f.mData + "'") + " for row " + b, 4), a.iDrawError = e),
            k;
            if ((c === h || null === c) && null !== k) c = k;
            else if ("function" === typeof c) return c.call(h);
            return null === c && "display" == d ? "": c
        }
        function Ha(a, b, c, d) {
            a.aoColumns[c].fnSetData(a.aoData[b]._aData, d, {
                settings: a,
                row: b,
                col: c
            })
        }
        function Ja(a) {
            return g.map(a.match(/(\\.|[^\.])+/g),
            function(a) {
                return a.replace(/\\./g, ".")
            })
        }
        function V(a) {
            if (g.isPlainObject(a)) {
                var b = {};
                g.each(a,
                function(a, c) {
                    c && (b[a] = V(c))
                });
                return function(a, c, f, h) {
                    var k = b[c] || b._;
                    return void 0 !== k ? k(a, c, f, h) : a
                }
            }
            if (null === a) return function(a) {
                return a
            };
            if ("function" === typeof a) return function(b, c, f, h) {
                return a(b, c, f, h)
            };
            if ("string" !== typeof a || -1 === a.indexOf(".") && -1 === a.indexOf("[") && -1 === a.indexOf("(")) return function(b, c) {
                return b[a]
            };
            var c = function(a, b, f) {
                var h, k;
                if ("" !== f) {
                    k = Ja(f);
                    for (var l = 0,
                    g = k.length; l < g; l++) {
                        f = k[l].match(aa);
                        h = k[l].match(R);
                        if (f) {
                            k[l] = k[l].replace(aa, "");
                            "" !== k[l] && (a = a[k[l]]);
                            h = [];
                            k.splice(0, l + 1);
                            k = k.join(".");
                            l = 0;
                            for (g = a.length; l < g; l++) h.push(c(a[l], b, k));
                            a = f[0].substring(1, f[0].length - 1);
                            a = "" === a ? h: h.join(a);
                            break
                        } else if (h) {
                            k[l] = k[l].replace(R, "");
                            a = a[k[l]]();
                            continue
                        }
                        if (null === a || void 0 === a[k[l]]) return;
                        a = a[k[l]]
                    }
                }
                return a
            };
            return function(b, e) {
                return c(b, e, a)
            }
        }
        function P(a) {
            if (g.isPlainObject(a)) return P(a._);
            if (null === a) return function() {};
            if ("function" === typeof a) return function(b, d, e) {
                a(b, "set", d, e)
            };
            if ("string" !== typeof a || -1 === a.indexOf(".") && -1 === a.indexOf("[") && -1 === a.indexOf("(")) return function(b, d) {
                b[a] = d
            };
            var b = function(a, d, e) {
                e = Ja(e);
                var f;
                f = e[e.length - 1];
                for (var h, k, l = 0,
                g = e.length - 1; l < g; l++) {
                    h = e[l].match(aa);
                    k = e[l].match(R);
                    if (h) {
                        e[l] = e[l].replace(aa, "");
                        a[e[l]] = [];
                        f = e.slice();
                        f.splice(0, l + 1);
                        h = f.join(".");
                        k = 0;
                        for (g = d.length; k < g; k++) f = {},
                        b(f, d[k], h),
                        a[e[l]].push(f);
                        return
                    }
                    k && (e[l] = e[l].replace(R, ""), a = a[e[l]](d));
                    if (null === a[e[l]] || void 0 === a[e[l]]) a[e[l]] = {};
                    a = a[e[l]]
                }
                if (f.match(R)) a[f.replace(R, "")](d);
                else a[f.replace(aa, "")] = d
            };
            return function(c, d) {
                return b(c, d, a)
            }
        }
        function Ka(a) {
            return F(a.aoData, "_aData")
        }
        function ma(a) {
            a.aoData.length = 0;
            a.aiDisplayMaster.length = 0;
            a.aiDisplay.length = 0
        }
        function na(a, b, c) {
            for (var d = -1,
            e = 0,
            f = a.length; e < f; e++) a[e] == b ? d = e: a[e] > b && a[e]--; - 1 != d && void 0 === c && a.splice(d, 1)
        }
        function ba(a, b, c, d) {
            var e = a.aoData[b],
            f,
            h = function(c, d) {
                for (; c.childNodes.length;) c.removeChild(c.firstChild);
                c.innerHTML = A(a, b, d, "display")
            };
            if ("dom" !== c && (c && "auto" !== c || "dom" !== e.src)) {
                var k = e.anCells;
                if (k) if (void 0 !== d) h(k[d], d);
                else for (c = 0, f = k.length; c < f; c++) h(k[c], c)
            } else e._aData = la(a, e, d, void 0 === d ? void 0 : e._aData).data;
            e._aSortData = null;
            e._aFilterData = null;
            h = a.aoColumns;
            if (void 0 !== d) h[d].sType = null;
            else {
                c = 0;
                for (f = h.length; c < f; c++) h[c].sType = null;
                La(e)
            }
        }
        function la(a, b, c, d) {
            var e = [],
            f = b.firstChild,
            h,
            k = 0,
            l,
            n = a.aoColumns,
            m = a._rowReadObject;
            d = d || m ? {}: [];
            var r = function(a, b) {
                if ("string" === typeof a) {
                    var c = a.indexOf("@"); - 1 !== c && (c = a.substring(c + 1), P(a)(d, b.getAttribute(c)))
                }
            };
            a = function(a) {
                if (void 0 === c || c === k) h = n[k],
                l = g.trim(a.innerHTML),
                h && h._bAttrSrc ? (P(h.mData._)(d, l), r(h.mData.sort, a), r(h.mData.type, a), r(h.mData.filter, a)) : m ? (h._setter || (h._setter = P(h.mData)), h._setter(d, l)) : d[k] = l;
                k++
            };
            if (f) for (; f;) {
                b = f.nodeName.toUpperCase();
                if ("TD" == b || "TH" == b) a(f),
                e.push(f);
                f = f.nextSibling
            } else for (e = b.anCells, f = 0, b = e.length; f < b; f++) a(e[f]);
            return {
                data: d,
                cells: e
            }
        }
        function Ia(a, b, c, d) {
            var e = a.aoData[b],
            f = e._aData,
            h = [],
            k,
            l,
            g,
            m,
            r;
            if (null === e.nTr) {
                k = c || O.createElement("tr");
                e.nTr = k;
                e.anCells = h;
                k._DT_RowIndex = b;
                La(e);
                m = 0;
                for (r = a.aoColumns.length; m < r; m++) {
                    g = a.aoColumns[m];
                    l = c ? d[m] : O.createElement(g.sCellType);
                    h.push(l);
                    if (!c || g.mRender || g.mData !== m) l.innerHTML = A(a, b, m, "display");
                    g.sClass && (l.className += " " + g.sClass);
                    g.bVisible && !c ? k.appendChild(l) : !g.bVisible && c && l.parentNode.removeChild(l);
                    g.fnCreatedCell && g.fnCreatedCell.call(a.oInstance, l, A(a, b, m), f, b, m)
                }
                x(a, "aoRowCreatedCallback", null, [k, f, b])
            }
            e.nTr.setAttribute("role", "row")
        }
        function La(a) {
            var b = a.nTr,
            c = a._aData;
            if (b) {
                c.DT_RowId && (b.id = c.DT_RowId);
                if (c.DT_RowClass) {
                    var d = c.DT_RowClass.split(" ");
                    a.__rowc = a.__rowc ? Ma(a.__rowc.concat(d)) : d;
                    g(b).removeClass(a.__rowc.join(" ")).addClass(c.DT_RowClass)
                }
                c.DT_RowAttr && g(b).attr(c.DT_RowAttr);
                c.DT_RowData && g(b).data(c.DT_RowData)
            }
        }
        function ib(a) {
            var b, c, d, e, f, h = a.nTHead,
            k = a.nTFoot,
            l = 0 === g("th, td", h).length,
            n = a.oClasses,
            m = a.aoColumns;
            l && (e = g("\x3ctr/\x3e").appendTo(h));
            b = 0;
            for (c = m.length; b < c; b++) f = m[b],
            d = g(f.nTh).addClass(f.sClass),
            l && d.appendTo(e),
            a.oFeatures.bSort && (d.addClass(f.sSortingClass), !1 !== f.bSortable && (d.attr("tabindex", a.iTabIndex).attr("aria-controls", a.sTableId), Na(a, f.nTh, b))),
            f.sTitle != d.html() && d.html(f.sTitle),
            Oa(a, "header")(a, d, f, n);
            l && ca(a.aoHeader, h);
            g(h).find("\x3etr").attr("role", "row");
            g(h).find("\x3etr\x3eth, \x3etr\x3etd").addClass(n.sHeaderTH);
            g(k).find("\x3etr\x3eth, \x3etr\x3etd").addClass(n.sFooterTH);
            if (null !== k) for (a = a.aoFooter[0], b = 0, c = a.length; b < c; b++) f = m[b],
            f.nTf = a[b].cell,
            f.sClass && g(f.nTf).addClass(f.sClass)
        }
        function da(a, b, c) {
            var d, e, f, h = [],
            k = [],
            l = a.aoColumns.length,
            n;
            if (b) {
                void 0 === c && (c = !1);
                d = 0;
                for (e = b.length; d < e; d++) {
                    h[d] = b[d].slice();
                    h[d].nTr = b[d].nTr;
                    for (f = l - 1; 0 <= f; f--) a.aoColumns[f].bVisible || c || h[d].splice(f, 1);
                    k.push([])
                }
                d = 0;
                for (e = h.length; d < e; d++) {
                    if (a = h[d].nTr) for (; f = a.firstChild;) a.removeChild(f);
                    f = 0;
                    for (b = h[d].length; f < b; f++) if (n = l = 1, void 0 === k[d][f]) {
                        a.appendChild(h[d][f].cell);
                        for (k[d][f] = 1; void 0 !== h[d + l] && h[d][f].cell == h[d + l][f].cell;) k[d + l][f] = 1,
                        l++;
                        for (; void 0 !== h[d][f + n] && h[d][f].cell == h[d][f + n].cell;) {
                            for (c = 0; c < l; c++) k[d + c][f + n] = 1;
                            n++
                        }
                        g(h[d][f].cell).attr("rowspan", l).attr("colspan", n)
                    }
                }
            }
        }
        function M(a) {
            var b = x(a, "aoPreDrawCallback", "preDraw", [a]);
            if ( - 1 !== g.inArray(!1, b)) E(a, !1);
            else {
                var b = [],
                c = 0,
                d = a.asStripeClasses,
                e = d.length,
                f = a.oLanguage,
                h = a.iInitDisplayStart,
                k = "ssp" == D(a),
                l = a.aiDisplay;
                a.bDrawing = !0;
                void 0 !== h && -1 !== h && (a._iDisplayStart = k ? h: h >= a.fnRecordsDisplay() ? 0 : h, a.iInitDisplayStart = -1);
                var h = a._iDisplayStart,
                n = a.fnDisplayEnd();
                if (a.bDeferLoading) a.bDeferLoading = !1,
                a.iDraw++,
                E(a, !1);
                else if (!k) a.iDraw++;
                else if (!a.bDestroying && !jb(a)) return;
                if (0 !== l.length) for (f = k ? a.aoData.length: n, k = k ? 0 : h; k < f; k++) {
                    var m = l[k],
                    r = a.aoData[m];
                    null === r.nTr && Ia(a, m);
                    m = r.nTr;
                    if (0 !== e) {
                        var q = d[c % e];
                        r._sRowStripe != q && (g(m).removeClass(r._sRowStripe).addClass(q), r._sRowStripe = q)
                    }
                    x(a, "aoRowCallback", null, [m, r._aData, c, k]);
                    b.push(m);
                    c++
                } else c = f.sZeroRecords,
                1 == a.iDraw && "ajax" == D(a) ? c = f.sLoadingRecords: f.sEmptyTable && 0 === a.fnRecordsTotal() && (c = f.sEmptyTable),
                b[0] = g("\x3ctr/\x3e", {
                    "class": e ? d[0] : ""
                }).append(g("\x3ctd /\x3e", {
                    valign: "top",
                    colSpan: $(a),
                    "class": a.oClasses.sRowEmpty
                }).html(c))[0];
                x(a, "aoHeaderCallback", "header", [g(a.nTHead).children("tr")[0], Ka(a), h, n, l]);
                x(a, "aoFooterCallback", "footer", [g(a.nTFoot).children("tr")[0], Ka(a), h, n, l]);
                d = g(a.nTBody);
                a.oInit.oInstance.cleartb && d.children().detach();
                d.append(g(b));
                x(a, "aoDrawCallback", "draw", [a]);
                a.bSorted = !1;
                a.bFiltered = !1;
                a.bDrawing = !1
            }
        }
        function N(a, b) {
            var c = a.oFeatures,
            d = c.bFilter;
            c.bSort && kb(a);
            d ? ea(a, a.oPreviousSearch) : a.aiDisplay = a.aiDisplayMaster.slice(); ! 0 !== b && (a._iDisplayStart = 0);
            a._drawHold = b;
            M(a);
            a._drawHold = !1
        }
        function lb(a) {
            var b = a.oClasses,
            c = g(a.nTable),
            c = g("\x3cdiv/\x3e").insertBefore(c),
            d = a.oFeatures,
            e = g("\x3cdiv/\x3e", {
                id: a.sTableId + "_wrapper",
                "class": b.sWrapper + (a.nTFoot ? "": " " + b.sNoFooter)
            });
            a.nHolding = c[0];
            a.nTableWrapper = e[0];
            a.nTableReinsertBefore = a.nTable.nextSibling;
            for (var f = a.sDom.split(""), h, k, l, n, m, r, q = 0; q < f.length; q++) {
                h = null;
                k = f[q];
                if ("\x3c" == k) {
                    l = g("\x3cdiv/\x3e")[0];
                    n = f[q + 1];
                    if ("'" == n || '"' == n) {
                        m = "";
                        for (r = 2; f[q + r] != n;) m += f[q + r],
                        r++;
                        "H" == m ? m = b.sJUIHeader: "F" == m && (m = b.sJUIFooter); - 1 != m.indexOf(".") ? (n = m.split("."), l.id = n[0].substr(1, n[0].length - 1), l.className = n[1]) : "#" == m.charAt(0) ? l.id = m.substr(1, m.length - 1) : l.className = m;
                        q += r
                    }
                    e.append(l);
                    e = g(l)
                } else if ("\x3e" == k) e = e.parent();
                else if ("l" == k && d.bPaginate && d.bLengthChange) h = mb(a);
                else if ("f" == k && d.bFilter) h = nb(a);
                else if ("r" == k && d.bProcessing) h = ob(a);
                else if ("t" == k) h = pb(a);
                else if ("i" == k && d.bInfo) h = qb(a);
                else if ("p" == k && d.bPaginate) h = rb(a);
                else if (0 !== p.ext.feature.length) for (l = p.ext.feature, r = 0, n = l.length; r < n; r++) if (k == l[r].cFeature) {
                    h = l[r].fnInit(a);
                    break
                }
                h && (l = a.aanFeatures, l[k] || (l[k] = []), l[k].push(h), e.append(h))
            }
            c.replaceWith(e)
        }
        function ca(a, b) {
            var c = g(b).children("tr"),
            d,
            e,
            f,
            h,
            k,
            l,
            n,
            m,
            r,
            q;
            a.splice(0, a.length);
            f = 0;
            for (l = c.length; f < l; f++) a.push([]);
            f = 0;
            for (l = c.length; f < l; f++) for (d = c[f], e = d.firstChild; e;) {
                if ("TD" == e.nodeName.toUpperCase() || "TH" == e.nodeName.toUpperCase()) {
                    m = 1 * e.getAttribute("colspan");
                    r = 1 * e.getAttribute("rowspan");
                    m = m && 0 !== m && 1 !== m ? m: 1;
                    r = r && 0 !== r && 1 !== r ? r: 1;
                    h = 0;
                    for (k = a[f]; k[h];) h++;
                    n = h;
                    q = 1 === m ? !0 : !1;
                    for (k = 0; k < m; k++) for (h = 0; h < r; h++) a[f + h][n + k] = {
                        cell: e,
                        unique: q
                    },
                    a[f + h].nTr = d
                }
                e = e.nextSibling
            }
        }
        function pa(a, b, c) {
            var d = [];
            c || (c = a.aoHeader, b && (c = [], ca(c, b)));
            b = 0;
            for (var e = c.length; b < e; b++) for (var f = 0,
            h = c[b].length; f < h; f++) ! c[b][f].unique || d[f] && a.bSortCellsTop || (d[f] = c[b][f].cell);
            return d
        }
        function qa(a, b, c) {
            x(a, "aoServerParams", "serverParams", [b]);
            if (b && g.isArray(b)) {
                var d = {},
                e = /(.*?)\[\]$/;
                g.each(b,
                function(a, b) {
                    var c = b.name.match(e);
                    c ? (c = c[0], d[c] || (d[c] = []), d[c].push(b.value)) : d[b.name] = b.value
                });
                b = d
            }
            var f, h = a.ajax,
            k = a.oInstance;
            if (g.isPlainObject(h) && h.data) {
                f = h.data;
                var l = g.isFunction(f) ? f(b) : f;
                b = g.isFunction(f) && l ? l: g.extend(!0, b, l);
                delete h.data
            }
            l = {
                data: b,
                success: function(b) {
                    var d = b.error || b.sError;
                    d && a.oApi._fnLog(a, 0, d);
                    a.json = b;
                    x(a, null, "xhr", [a, b]);
                    c(b)
                },
                dataType: "json",
                cache: !1,
                type: a.sServerMethod,
                error: function(b, c, d) {
                    d = a.oApi._fnLog;
                    "parsererror" == c ? d(a, 0, "Invalid JSON response", 1) : 4 === b.readyState && d(a, 0, "Ajax error", 7);
                    E(a, !1)
                }
            };
            a.oAjaxData = b;
            x(a, null, "preXhr", [a, b]);
            a.fnServerData ? a.fnServerData.call(k, a.sAjaxSource, g.map(b,
            function(a, b) {
                return {
                    name: b,
                    value: a
                }
            }), c, a) : a.sAjaxSource || "string" === typeof h ? a.jqXHR = g.ajax(g.extend(l, {
                url: h || a.sAjaxSource
            })) : g.isFunction(h) ? a.jqXHR = h.call(k, b, c, a) : (a.jqXHR = g.ajax(g.extend(l, h)), h.data = f)
        }
        function jb(a) {
            return a.bAjaxDataGet ? (a.iDraw++, E(a, !0), qa(a, sb(a),
            function(b) {
                tb(a, b)
            }), !1) : !0
        }
        function sb(a) {
            var b = a.aoColumns,
            c = b.length,
            d = a.oFeatures,
            e = a.oPreviousSearch,
            f = a.aoPreSearchCols,
            h, k = [],
            l,
            n,
            m,
            r = S(a);
            h = a._iDisplayStart;
            l = !1 !== d.bPaginate ? a._iDisplayLength: -1;
            var q = function(a, b) {
                k.push({
                    name: a,
                    value: b
                })
            };
            q("sEcho", a.iDraw);
            q("iColumns", c);
            q("sColumns", F(b, "sName").join(","));
            q("iDisplayStart", h);
            q("iDisplayLength", l);
            var oa = {
                draw: a.iDraw,
                columns: [],
                order: [],
                start: h,
                length: l,
                search: {
                    value: e.sSearch,
                    regex: e.bRegex
                }
            };
            for (h = 0; h < c; h++) n = b[h],
            m = f[h],
            l = "function" == typeof n.mData ? "function": n.mData,
            oa.columns.push({
                data: l,
                name: n.sName,
                searchable: n.bSearchable,
                orderable: n.bSortable,
                search: {
                    value: m.sSearch,
                    regex: m.bRegex
                }
            }),
            q("mDataProp_" + h, l),
            d.bFilter && (q("sSearch_" + h, m.sSearch), q("bRegex_" + h, m.bRegex), q("bSearchable_" + h, n.bSearchable)),
            d.bSort && q("bSortable_" + h, n.bSortable);
            d.bFilter && (q("sSearch", e.sSearch), q("bRegex", e.bRegex));
            d.bSort && (g.each(r,
            function(a, b) {
                oa.order.push({
                    column: b.col,
                    dir: b.dir
                });
                q("iSortCol_" + a, b.col);
                q("sSortDir_" + a, b.dir)
            }), q("iSortingCols", r.length));
            b = p.ext.legacy.ajax;
            return null === b ? a.sAjaxSource ? k: oa: b ? k: oa
        }
        function tb(a, b) {
            var c = function(a, c) {
                return void 0 !== b[a] ? b[a] : b[c]
            },
            d = c("sEcho", "draw"),
            e = c("iTotalRecords", "recordsTotal"),
            c = c("iTotalDisplayRecords", "recordsFiltered");
            if (d) {
                if (1 * d < a.iDraw) return;
                a.iDraw = 1 * d
            }
            ma(a);
            a._iRecordsTotal = parseInt(e, 10);
            a._iRecordsDisplay = parseInt(c, 10);
            d = ra(a, b);
            e = 0;
            for (c = d.length; e < c; e++) K(a, d[e]);
            a.aiDisplay = a.aiDisplayMaster.slice();
            a.bAjaxDataGet = !1;
            M(a);
            a._bInitComplete || sa(a, b);
            a.bAjaxDataGet = !0;
            E(a, !1)
        }
        function ra(a, b) {
            var c = g.isPlainObject(a.ajax) && void 0 !== a.ajax.dataSrc ? a.ajax.dataSrc: a.sAjaxDataProp;
            return "data" === c ? b.aaData || b[c] : "" !== c ? V(c)(b) : b
        }
        function nb(a) {
            var b = a.oClasses,
            c = a.sTableId,
            d = a.oLanguage,
            e = a.oPreviousSearch,
            f = a.aanFeatures,
            h = '\x3cinput type\x3d"search" class\x3d"' + b.sFilterInput + '"/\x3e',
            k = d.sSearch,
            k = k.match(/_INPUT_/) ? k.replace("_INPUT_", h) : k + h,
            b = g("\x3cdiv/\x3e", {
                id: f.f ? null: c + "_filter",
                "class": b.sFilter
            }).append(g("\x3clabel/\x3e").append(k)),
            f = function() {
                var b = this.value ? this.value: "";
                b != e.sSearch && (ea(a, {
                    sSearch: b,
                    bRegex: e.bRegex,
                    bSmart: e.bSmart,
                    bCaseInsensitive: e.bCaseInsensitive
                }), a._iDisplayStart = 0, M(a))
            },
            h = null !== a.searchDelay ? a.searchDelay: "ssp" === D(a) ? 400 : 0,
            l = g("input", b).val(e.sSearch).attr("placeholder", d.sSearchPlaceholder).bind("keyup.DT search.DT input.DT paste.DT cut.DT", h ? ta(f, h) : f).bind("keypress.DT",
            function(a) {
                if (13 == a.keyCode) return ! 1
            }).attr("aria-controls", c);
            g(a.nTable).on("search.dt.DT",
            function(b, c) {
                if (a === c) try {
                    l[0] !== O.activeElement && l.val(e.sSearch)
                } catch(d) {}
            });
            return b[0]
        }
        function ea(a, b, c) {
            var d = a.oPreviousSearch,
            e = a.aoPreSearchCols,
            f = function(a) {
                d.sSearch = a.sSearch;
                d.bRegex = a.bRegex;
                d.bSmart = a.bSmart;
                d.bCaseInsensitive = a.bCaseInsensitive
            },
            h = function(a) {
                return void 0 !== a.bEscapeRegex ? !a.bEscapeRegex: a.bRegex
            };
            Ga(a);
            if ("ssp" != D(a)) {
                ub(a, b.sSearch, c, h(b), b.bSmart, b.bCaseInsensitive);
                f(b);
                for (b = 0; b < e.length; b++) vb(a, e[b].sSearch, b, h(e[b]), e[b].bSmart, e[b].bCaseInsensitive);
                wb(a)
            } else f(b);
            a.bFiltered = !0;
            x(a, null, "search", [a])
        }
        function wb(a) {
            for (var b = p.ext.search,
            c = a.aiDisplay,
            d, e, f = 0,
            h = b.length; f < h; f++) {
                for (var k = [], g = 0, n = c.length; g < n; g++) e = c[g],
                d = a.aoData[e],
                b[f](a, d._aFilterData, e, d._aData, g) && k.push(e);
                c.length = 0;
                c.push.apply(c, k)
            }
        }
        function vb(a, b, c, d, e, f) {
            if ("" !== b) {
                var h = a.aiDisplay;
                d = Pa(b, d, e, f);
                for (e = h.length - 1; 0 <= e; e--) b = a.aoData[h[e]]._aFilterData[c],
                d.test(b) || h.splice(e, 1)
            }
        }
        function ub(a, b, c, d, e, f) {
            d = Pa(b, d, e, f);
            e = a.oPreviousSearch.sSearch;
            f = a.aiDisplayMaster;
            var h;
            0 !== p.ext.search.length && (c = !0);
            h = xb(a);
            if (0 >= b.length) a.aiDisplay = f.slice();
            else {
                if (h || c || e.length > b.length || 0 !== b.indexOf(e) || a.bSorted) a.aiDisplay = f.slice();
                b = a.aiDisplay;
                for (c = b.length - 1; 0 <= c; c--) d.test(a.aoData[b[c]]._sFilterRow) || b.splice(c, 1)
            }
        }
        function Pa(a, b, c, d) {
            a = b ? a: ua(a);
            c && (a = "^(?\x3d.*?" + g.map(a.match(/"[^"]+"|[^ ]+/g) || "",
            function(a) {
                if ('"' === a.charAt(0)) {
                    var b = a.match(/^"(.*)"$/);
                    a = b ? b[1] : a
                }
                return a.replace('"', "")
            }).join(")(?\x3d.*?") + ").*$");
            return new RegExp(a, d ? "i": "")
        }
        function ua(a) {
            return a.replace(Wb, "\\$1")
        }
        function xb(a) {
            var b = a.aoColumns,
            c, d, e, f, h, k, g, n, m = p.ext.type.search;
            c = !1;
            d = 0;
            for (f = a.aoData.length; d < f; d++) if (n = a.aoData[d], !n._aFilterData) {
                k = [];
                e = 0;
                for (h = b.length; e < h; e++) c = b[e],
                c.bSearchable ? (g = A(a, d, e, "filter"), m[c.sType] && (g = m[c.sType](g)), null === g && (g = ""), "string" !== typeof g && g.toString && (g = g.toString())) : g = "",
                g.indexOf && -1 !== g.indexOf("\x26") && (va.innerHTML = g, g = Xb ? va.textContent: va.innerText),
                g.replace && (g = g.replace(/[\r\n]/g, "")),
                k.push(g);
                n._aFilterData = k;
                n._sFilterRow = k.join("  ");
                c = !0
            }
            return c
        }
        function yb(a) {
            return {
                search: a.sSearch,
                smart: a.bSmart,
                regex: a.bRegex,
                caseInsensitive: a.bCaseInsensitive
            }
        }
        function zb(a) {
            return {
                sSearch: a.search,
                bSmart: a.smart,
                bRegex: a.regex,
                bCaseInsensitive: a.caseInsensitive
            }
        }
        function qb(a) {
            var b = a.sTableId,
            c = a.aanFeatures.i,
            d = g("\x3cdiv/\x3e", {
                "class": a.oClasses.sInfo,
                id: c ? null: b + "_info"
            });
            c || (a.aoDrawCallback.push({
                fn: Ab,
                sName: "information"
            }), d.attr("role", "status").attr("aria-live", "polite"), g(a.nTable).attr("aria-describedby", b + "_info"));
            return d[0]
        }
        function Ab(a) {
            var b = a.aanFeatures.i;
            if (0 !== b.length) {
                var c = a.oLanguage,
                d = a._iDisplayStart + 1,
                e = a.fnDisplayEnd(),
                f = a.fnRecordsTotal(),
                h = a.fnRecordsDisplay(),
                k = h ? c.sInfo: c.sInfoEmpty;
                h !== f && (k += " " + c.sInfoFiltered);
                k += c.sInfoPostFix;
                k = Bb(a, k);
                c = c.fnInfoCallback;
                null !== c && (k = c.call(a.oInstance, a, d, e, f, h, k));
                g(b).html(k)
            }
        }
        function Bb(a, b) {
            var c = a.fnFormatNumber,
            d = a._iDisplayStart + 1,
            e = a._iDisplayLength,
            f = a.fnRecordsDisplay(),
            h = -1 === e;
            return b.replace(/_START_/g, c.call(a, d)).replace(/_END_/g, c.call(a, a.fnDisplayEnd())).replace(/_MAX_/g, c.call(a, a.fnRecordsTotal())).replace(/_TOTAL_/g, c.call(a, f)).replace(/_PAGE_/g, c.call(a, h ? 1 : Math.ceil(d / e))).replace(/_PAGES_/g, c.call(a, h ? 1 : Math.ceil(f / e)))
        }
        function fa(a) {
            var b, c, d = a.iInitDisplayStart,
            e = a.aoColumns,
            f;
            c = a.oFeatures;
            if (a.bInitialised) {
                lb(a);
                ib(a);
                da(a, a.aoHeader);
                da(a, a.aoFooter);
                E(a, !0);
                c.bAutoWidth && Fa(a);
                b = 0;
                for (c = e.length; b < c; b++) f = e[b],
                f.sWidth && (f.nTh.style.width = w(f.sWidth));
                N(a);
                e = D(a);
                "ssp" != e && ("ajax" == e ? qa(a, [],
                function(c) {
                    var f = ra(a, c);
                    for (b = 0; b < f.length; b++) K(a, f[b]);
                    a.iInitDisplayStart = d;
                    N(a);
                    E(a, !1);
                    sa(a, c)
                },
                a) : (E(a, !1), sa(a)))
            } else setTimeout(function() {
                fa(a)
            },
            200)
        }
        function sa(a, b) {
            a._bInitComplete = !0;
            b && W(a);
            x(a, "aoInitComplete", "init", [a, b])
        }
        function Qa(a, b) {
            var c = parseInt(b, 10);
            a._iDisplayLength = c;
            Ra(a);
            x(a, null, "length", [a, c])
        }
        function mb(a) {
            for (var b = a.oClasses,
            c = a.sTableId,
            d = a.aLengthMenu,
            e = g.isArray(d[0]), f = e ? d[0] : d, d = e ? d[1] : d, e = g("\x3cselect/\x3e", {
                name: c + "_length",
                "aria-controls": c,
                "class": b.sLengthSelect
            }), h = 0, k = f.length; h < k; h++) e[0][h] = new Option(d[h], f[h]);
            var l = g("\x3cdiv\x3e\x3clabel/\x3e\x3c/div\x3e").addClass(b.sLength);
            a.aanFeatures.l || (l[0].id = c + "_length");
            l.children().append(a.oLanguage.sLengthMenu.replace("_MENU_", e[0].outerHTML));
            g("select", l).val(a._iDisplayLength).bind("change.DT",
            function(b) {
                Qa(a, g(this).val());
                M(a)
            });
            g(a.nTable).bind("length.dt.DT",
            function(b, c, d) {
                a === c && g("select", l).val(d)
            });
            return l[0]
        }
        function rb(a) {
            var b = a.sPaginationType,
            c = p.ext.pager[b],
            d = "function" === typeof c,
            e = function(a) {
                M(a)
            },
            b = g("\x3cdiv/\x3e").addClass(a.oClasses.sPaging + b)[0],
            f = a.aanFeatures;
            d || c.fnInit(a, b, e);
            f.p || (b.id = a.sTableId + "_paginate", a.aoDrawCallback.push({
                fn: function(a) {
                    if (d) {
                        var b = a._iDisplayStart,
                        g = a._iDisplayLength,
                        n = a.fnRecordsDisplay(),
                        m = -1 === g,
                        b = m ? 0 : Math.ceil(b / g),
                        g = m ? 1 : Math.ceil(n / g),
                        n = c(b, g),
                        r,
                        m = 0;
                        for (r = f.p.length; m < r; m++) Oa(a, "pageButton")(a, f.p[m], m, n, b, g)
                    } else c.fnUpdate(a, e)
                },
                sName: "pagination"
            }));
            return b
        }
        function Sa(a, b, c) {
            var d = a._iDisplayStart,
            e = a._iDisplayLength,
            f = a.fnRecordsDisplay();
            0 === f || -1 === e ? d = 0 : "number" === typeof b ? (d = b * e, d > f && (d = 0)) : "first" == b ? d = 0 : "previous" == b ? (d = 0 <= e ? d - e: 0, 0 > d && (d = 0)) : "next" == b ? d + e < f && (d += e) : "last" == b ? d = Math.floor((f - 1) / e) * e: Q(a, 0, "Unknown paging action: " + b, 5);
            b = a._iDisplayStart !== d;
            a._iDisplayStart = d;
            b && (x(a, null, "page", [a]), c && M(a));
            return b
        }
        function ob(a) {
            return g("\x3cdiv/\x3e", {
                id: a.aanFeatures.r ? null: a.sTableId + "_processing",
                "class": a.oClasses.sProcessing
            }).html(a.oLanguage.sProcessing).insertBefore(a.nTable)[0]
        }
        function E(a, b) {
            a.oFeatures.bProcessing && g(a.aanFeatures.r).css("display", b ? "block": "none");
            x(a, null, "processing", [a, b])
        }
        function pb(a) {
            var b = g(a.nTable);
            b.attr("role", "grid");
            var c = a.oScroll;
            if ("" === c.sX && "" === c.sY) return a.nTable;
            var d = c.sX,
            e = a.oClasses,
            f = b.children("caption"),
            h = f.length ? f[0]._captionSide: null,
            k = g(b[0].cloneNode(!1)),
            l = g(b[0].cloneNode(!1)),
            n = b.children("tfoot");
            c.sX && "100%" === b.attr("width") && b.removeAttr("width");
            n.length || (n = null);
            c = g("\x3cdiv/\x3e", {
                "class": e.sScrollWrapper
            }).append(g("\x3cdiv/\x3e", {
                "class": e.sScrollHead
            }).css({
                overflow: "hidden",
                position: "relative",
                border: 0,
                width: d ? d ? w(d) : null: "100%"
            }).append(g("\x3cdiv/\x3e", {
                "class": e.sScrollHeadInner
            }).css({
                "box-sizing": "content-box",
                width: c.sXInner || "100%"
            }).append(k.removeAttr("id").css("margin-left", 0).append("top" === h ? f: null).append(b.children("thead"))))).append(g("\x3cdiv/\x3e", {
                "class": e.sScrollBody
            }).css({
                overflow: "auto",
                width: d ? w(d) : null
            }).append(b));
            n && c.append(g("\x3cdiv/\x3e", {
                "class": e.sScrollFoot
            }).css({
                overflow: "hidden",
                border: 0,
                width: d ? d ? w(d) : null: "100%"
            }).append(g("\x3cdiv/\x3e", {
                "class": e.sScrollFootInner
            }).append(l.removeAttr("id").css("margin-left", 0).append("bottom" === h ? f: null).append(b.children("tfoot")))));
            var b = c.children(),
            m = b[0],
            e = b[1],
            r = n ? b[2] : null;
            if (d) g(e).on("scroll.DT",
            function(a) {
                a = this.scrollLeft;
                m.scrollLeft = a;
                n && (r.scrollLeft = a)
            });
            a.nScrollHead = m;
            a.nScrollBody = e;
            a.nScrollFoot = r;
            a.aoDrawCallback.push({
                fn: X,
                sName: "scrolling"
            });
            return c[0]
        }
        function X(a) {
            var b = a.oScroll,
            c = b.sX,
            d = b.sXInner,
            e = b.sY,
            f = b.iBarWidth,
            h = g(a.nScrollHead),
            k = h[0].style,
            l = h.children("div"),
            n = l[0].style,
            m = l.children("table"),
            l = a.nScrollBody,
            r = g(l),
            q = l.style,
            p = g(a.nScrollFoot).children("div"),
            s = p.children("table"),
            t = g(a.nTHead),
            u = g(a.nTable),
            v = u[0],
            x = v.style,
            L = a.nTFoot ? g(a.nTFoot) : null,
            ga = a.oBrowser,
            B = ga.bScrollOversize,
            y,
            A,
            z,
            C,
            D = [],
            E = [],
            F = [],
            G,
            H = function(a) {
                a = a.style;
                a.paddingTop = "0";
                a.paddingBottom = "0";
                a.borderTopWidth = "0";
                a.borderBottomWidth = "0";
                a.height = 0
            };
            u.children("thead, tfoot").remove();
            C = t.clone().prependTo(u);
            t = t.find("tr");
            A = C.find("tr");
            C.find("th, td").removeAttr("tabindex");
            L && (z = L.clone().prependTo(u), y = L.find("tr"), z = z.find("tr"));
            c || (q.width = "100%", h[0].style.width = "100%");
            g.each(pa(a, C),
            function(b, c) {
                G = ja(a, b);
                c.style.width = a.aoColumns[G].sWidth
            });
            L && I(function(a) {
                a.style.width = ""
            },
            z);
            h = u.outerWidth();
            "" === c ? (x.width = "100%", B && (u.find("tbody").height() > l.offsetHeight || "scroll" == r.css("overflow-y")) && (x.width = w(u.outerWidth() - f))) : "" !== d ? x.width = w(d) : h == r.width() && r.height() < u.height() ? (x.width = w(h - f), u.outerWidth() > h - f && (x.width = w(h))) : x.width = w(h);
            h = u.outerWidth();
            I(H, A);
            I(function(a) {
                F.push(a.innerHTML);
                D.push(w(g(a).css("width")))
            },
            A);
            I(function(a, b) {
                a.style.width = D[b]
            },
            t);
            g(A).height(0);
            L && (I(H, z), I(function(a) {
                E.push(w(g(a).css("width")))
            },
            z), I(function(a, b) {
                a.style.width = E[b]
            },
            y), g(z).height(0));
            I(function(a, b) {
                a.innerHTML = '\x3cdiv class\x3d"dataTables_sizing" style\x3d"height:0;overflow:hidden;"\x3e' + F[b] + "\x3c/div\x3e";
                a.style.width = D[b]
            },
            A);
            L && I(function(a, b) {
                a.innerHTML = "";
                a.style.width = E[b]
            },
            z);
            u.outerWidth() < h ? (y = l.scrollHeight > l.offsetHeight || "scroll" == r.css("overflow-y") ? h + f: h, "" !== c && "" === d || Q(a, 1, "Possible column misalignment", 6)) : y = "100%";
            q.width = w(y);
            k.width = w(y);
            L && (a.nScrollFoot.style.width = w(y));
            e && b.bCollapse && (b = c && v.offsetWidth > l.offsetWidth ? f: 0, v.offsetHeight < l.offsetHeight && (q.height = w(v.offsetHeight + b)));
            b = u.outerWidth();
            m[0].style.width = w(b);
            n.width = w(b);
            m = u.height() > l.clientHeight || "scroll" == r.css("overflow-y");
            ga = "padding" + (ga.bScrollbarLeft ? "Left": "Right");
            n[ga] = m ? f + "px": "0px";
            L && (s[0].style.width = w(b), p[0].style.width = w(b), p[0].style[ga] = m ? f + "px": "0px");
            r.scroll(); ! a.bSorted && !a.bFiltered || a._drawHold || (l.scrollTop = 0)
        }
        function I(a, b, c) {
            for (var d = 0,
            e = 0,
            f = b.length,
            h, g; e < f;) {
                h = b[e].firstChild;
                for (g = c ? c[e].firstChild: null; h;) 1 === h.nodeType && (c ? a(h, g, d) : a(h, d), d++),
                h = h.nextSibling,
                g = c ? g.nextSibling: null;
                e++
            }
        }
        function Fa(a) {
            var b = a.nTable,
            c = a.aoColumns,
            d = a.oScroll,
            e = d.sY,
            f = d.sX,
            h = d.sXInner,
            k = c.length,
            d = Y(a, "bVisible"),
            l = g("th", a.nTHead),
            n = b.style.width || b.getAttribute("width"),
            m = b.parentNode,
            r = !1,
            q,
            p;
            for (q = 0; q < d.length; q++) p = c[d[q]],
            null !== p.sWidth && (p.sWidth = Cb(p.sWidthOrig, m), r = !0);
            if (r || f || e || k != $(a) || k != l.length) {
                k = g(b).clone().empty().css("visibility", "hidden").removeAttr("id").append(g(a.nTHead).clone(!1)).append(g(a.nTFoot).clone(!1)).append(g("\x3ctbody\x3e\x3ctr/\x3e\x3c/tbody\x3e"));
                k.find("tfoot th, tfoot td").css("width", "");
                var s = k.find("tbody tr"),
                l = pa(a, k.find("thead")[0]);
                for (q = 0; q < d.length; q++) p = c[d[q]],
                l[q].style.width = null !== p.sWidthOrig && "" !== p.sWidthOrig ? w(p.sWidthOrig) : "";
                if (a.aoData.length) for (q = 0; q < d.length; q++) r = d[q],
                p = c[r],
                g(Db(a, r)).clone(!1).append(p.sContentPadding).appendTo(s);
                k.appendTo(m);
                f && h ? k.width(h) : f ? (k.css("width", "auto"), k.width() < m.offsetWidth && k.width(m.offsetWidth)) : e ? k.width(m.offsetWidth) : n && k.width(n);
                Eb(a, k[0]);
                if (f) {
                    for (q = h = 0; q < d.length; q++) p = c[d[q]],
                    e = g(l[q]).outerWidth(),
                    h += null === p.sWidthOrig ? e: parseInt(p.sWidth, 10) + e - g(l[q]).width();
                    k.width(w(h));
                    b.style.width = w(h)
                }
                for (q = 0; q < d.length; q++) if (p = c[d[q]], e = g(l[q]).width()) p.sWidth = w(e);
                b.style.width = w(k.css("width"));
                k.remove()
            } else for (q = 0; q < k; q++) c[q].sWidth = w(l.eq(q).width());
            n && (b.style.width = w(n)); ! n && !f || a._reszEvt || (g(Ca).bind("resize.DT-" + a.sInstance, ta(function() {
                W(a)
            })), a._reszEvt = !0)
        }
        function ta(a, b) {
            var c = void 0 !== b ? b: 200,
            d,
            e;
            return function() {
                var b = this,
                h = +new Date,
                g = arguments;
                d && h < d + c ? (clearTimeout(e), e = setTimeout(function() {
                    d = void 0;
                    a.apply(b, g)
                },
                c)) : (d = h, a.apply(b, g))
            }
        }
        function Cb(a, b) {
            if (!a) return 0;
            var c = g("\x3cdiv/\x3e").css("width", w(a)).appendTo(b || O.body),
            d = c[0].offsetWidth;
            c.remove();
            return d
        }
        function Eb(a, b) {
            var c = a.oScroll;
            if (c.sX || c.sY) c = c.sX ? 0 : c.iBarWidth,
            b.style.width = w(g(b).outerWidth() - c)
        }
        function Db(a, b) {
            var c = Fb(a, b);
            if (0 > c) return null;
            var d = a.aoData[c];
            return d.nTr ? d.anCells[b] : g("\x3ctd/\x3e").html(A(a, c, b, "display"))[0]
        }
        function Fb(a, b) {
            for (var c, d = -1,
            e = -1,
            f = 0,
            h = a.aoData.length; f < h; f++) c = A(a, f, b, "display") + "",
            c = c.replace(Yb, ""),
            c.length > d && (d = c.length, e = f);
            return e
        }
        function w(a) {
            return null === a ? "0px": "number" == typeof a ? 0 > a ? "0px": a + "px": a.match(/\d$/) ? a + "px": a
        }
        function Gb() {
            if (!p.__scrollbarWidth) {
                var a = g("\x3cp/\x3e").css({
                    width: "100%",
                    height: 200,
                    padding: 0
                })[0],
                b = g("\x3cdiv/\x3e").css({
                    position: "absolute",
                    top: 0,
                    left: 0,
                    width: 200,
                    height: 150,
                    padding: 0,
                    overflow: "hidden",
                    visibility: "hidden"
                }).append(a).appendTo("body"),
                c = a.offsetWidth;
                b.css("overflow", "scroll");
                a = a.offsetWidth;
                c === a && (a = b[0].clientWidth);
                b.remove();
                p.__scrollbarWidth = c - a
            }
            return p.__scrollbarWidth
        }
        function S(a) {
            var b, c, d = [],
            e = a.aoColumns,
            f,
            h,
            k,
            l;
            b = a.aaSortingFixed;
            c = g.isPlainObject(b);
            var n = [];
            f = function(a) {
                a.length && !g.isArray(a[0]) ? n.push(a) : n.push.apply(n, a)
            };
            g.isArray(b) && f(b);
            c && b.pre && f(b.pre);
            f(a.aaSorting);
            c && b.post && f(b.post);
            for (a = 0; a < n.length; a++) for (l = n[a][0], f = e[l].aDataSort, b = 0, c = f.length; b < c; b++) h = f[b],
            k = e[h].sType || "string",
            void 0 === n[a]._idx && (n[a]._idx = g.inArray(n[a][1], e[h].asSorting)),
            d.push({
                src: l,
                col: h,
                dir: n[a][1],
                index: n[a]._idx,
                type: k,
                formatter: p.ext.type.order[k + "-pre"]
            });
            return d
        }
        function kb(a) {
            var b, c, d = [],
            e = p.ext.type.order,
            f = a.aoData,
            h = 0,
            g,
            l = a.aiDisplayMaster,
            n;
            Ga(a);
            n = S(a);
            b = 0;
            for (c = n.length; b < c; b++) g = n[b],
            g.formatter && h++,
            Hb(a, g.col);
            if ("ssp" != D(a) && 0 !== n.length) {
                b = 0;
                for (c = l.length; b < c; b++) d[l[b]] = b;
                h === n.length ? l.sort(function(a, b) {
                    var c, e, h, g, k = n.length,
                    l = f[a]._aSortData,
                    p = f[b]._aSortData;
                    for (h = 0; h < k; h++) if (g = n[h], c = l[g.col], e = p[g.col], c = c < e ? -1 : c > e ? 1 : 0, 0 !== c) return "asc" === g.dir ? c: -c;
                    c = d[a];
                    e = d[b];
                    return c < e ? -1 : c > e ? 1 : 0
                }) : l.sort(function(a, b) {
                    var c, h, g, k, l = n.length,
                    p = f[a]._aSortData,
                    t = f[b]._aSortData;
                    for (g = 0; g < l; g++) if (k = n[g], c = p[k.col], h = t[k.col], k = e[k.type + "-" + k.dir] || e["string-" + k.dir], c = k(c, h), 0 !== c) return c;
                    c = d[a];
                    h = d[b];
                    return c < h ? -1 : c > h ? 1 : 0
                })
            }
            a.bSorted = !0
        }
        function Ib(a) {
            var b, c, d = a.aoColumns,
            e = S(a);
            a = a.oLanguage.oAria;
            for (var f = 0,
            h = d.length; f < h; f++) {
                c = d[f];
                var g = c.asSorting;
                b = c.sTitle.replace(/<.*?>/g, "");
                var l = c.nTh;
                l.removeAttribute("aria-sort");
                c.bSortable && (0 < e.length && e[0].col == f ? (l.setAttribute("aria-sort", "asc" == e[0].dir ? "ascending": "descending"), c = g[e[0].index + 1] || g[0]) : c = g[0], b += "asc" === c ? a.sSortAscending: a.sSortDescending);
                l.setAttribute("aria-label", b)
            }
        }
        function Ta(a, b, c, d) {
            var e = a.aaSorting,
            f = a.aoColumns[b].asSorting,
            h = function(a, b) {
                var c = a._idx;
                void 0 === c && (c = g.inArray(a[1], f));
                return c + 1 < f.length ? c + 1 : b ? null: 0
            };
            "number" === typeof e[0] && (e = a.aaSorting = [e]);
            c && a.oFeatures.bSortMulti ? (c = g.inArray(b, F(e, "0")), -1 !== c ? (b = h(e[c], !0), null === b ? e.splice(c, 1) : (e[c][1] = f[b], e[c]._idx = b)) : (e.push([b, f[0], 0]), e[e.length - 1]._idx = 0)) : e.length && e[0][0] == b ? (b = h(e[0]), e.length = 1, e[0][1] = f[b], e[0]._idx = b) : (e.length = 0, e.push([b, f[0]]), e[0]._idx = 0);
            N(a);
            "function" == typeof d && d(a)
        }
        function Na(a, b, c, d) {
            var e = a.aoColumns[c];
            Ua(b, {},
            function(b) { ! 1 !== e.bSortable && (a.oFeatures.bProcessing ? (E(a, !0), setTimeout(function() {
                    Ta(a, c, b.shiftKey, d);
                    "ssp" !== D(a) && E(a, !1)
                },
                0)) : Ta(a, c, b.shiftKey, d))
            })
        }
        function wa(a) {
            var b = a.aLastSort,
            c = a.oClasses.sSortColumn,
            d = S(a),
            e = a.oFeatures,
            f,
            h;
            if (e.bSort && e.bSortClasses) {
                e = 0;
                for (f = b.length; e < f; e++) h = b[e].src,
                g(F(a.aoData, "anCells", h)).removeClass(c + (2 > e ? e + 1 : 3));
                e = 0;
                for (f = d.length; e < f; e++) h = d[e].src,
                g(F(a.aoData, "anCells", h)).addClass(c + (2 > e ? e + 1 : 3))
            }
            a.aLastSort = d
        }
        function Hb(a, b) {
            var c = a.aoColumns[b],
            d = p.ext.order[c.sSortDataType],
            e;
            d && (e = d.call(a.oInstance, a, b, Z(a, b)));
            for (var f, h = p.ext.type.order[c.sType + "-pre"], g = 0, l = a.aoData.length; g < l; g++) if (c = a.aoData[g], c._aSortData || (c._aSortData = []), !c._aSortData[b] || d) f = d ? e[g] : A(a, g, b, "sort"),
            c._aSortData[b] = h ? h(f) : f
        }
        function xa(a) {
            if (a.oFeatures.bStateSave && !a.bDestroying) {
                var b = {
                    time: +new Date,
                    start: a._iDisplayStart,
                    length: a._iDisplayLength,
                    order: g.extend(!0, [], a.aaSorting),
                    search: yb(a.oPreviousSearch),
                    columns: g.map(a.aoColumns,
                    function(b, d) {
                        return {
                            visible: b.bVisible,
                            search: yb(a.aoPreSearchCols[d])
                        }
                    })
                };
                x(a, "aoStateSaveParams", "stateSaveParams", [a, b]);
                a.oSavedState = b;
                a.fnStateSaveCallback.call(a.oInstance, a, b)
            }
        }
        function Jb(a, b) {
            var c, d, e = a.aoColumns;
            if (a.oFeatures.bStateSave) {
                var f = a.fnStateLoadCallback.call(a.oInstance, a);
                if (f && f.time && (c = x(a, "aoStateLoadParams", "stateLoadParams", [a, f]), -1 === g.inArray(!1, c) && (c = a.iStateDuration, !(0 < c && f.time < +new Date - 1E3 * c) && e.length === f.columns.length))) {
                    a.oLoadedState = g.extend(!0, {},
                    f);
                    a._iDisplayStart = f.start;
                    a.iInitDisplayStart = f.start;
                    a._iDisplayLength = f.length;
                    a.aaSorting = [];
                    g.each(f.order,
                    function(b, c) {
                        a.aaSorting.push(c[0] >= e.length ? [0, c[1]] : c)
                    });
                    g.extend(a.oPreviousSearch, zb(f.search));
                    c = 0;
                    for (d = f.columns.length; c < d; c++) {
                        var h = f.columns[c];
                        e[c].bVisible = h.visible;
                        g.extend(a.aoPreSearchCols[c], zb(h.search))
                    }
                    x(a, "aoStateLoaded", "stateLoaded", [a, f])
                }
            }
        }
        function ya(a) {
            var b = p.settings;
            a = g.inArray(a, F(b, "nTable"));
            return - 1 !== a ? b[a] : null
        }
        function Q(a, b, c, d) {
            c = "DataTables warning: " + (null !== a ? "table id\x3d" + a.sTableId + " - ": "") + c;
            d && (c += ". For more information about this error, please see http://datatables.net/tn/" + d);
            if (b) Ca.console && console.log && console.log(c);
            else if (b = p.ext, b = b.sErrMode || b.errMode, x(a, null, "error", [a, d, c]), "alert" == b) alert(c);
            else {
                if ("throw" == b) throw Error(c);
                "function" == typeof b && b(a, d, c)
            }
        }
        function G(a, b, c, d) {
            g.isArray(c) ? g.each(c,
            function(c, d) {
                g.isArray(d) ? G(a, b, d[0], d[1]) : G(a, b, d)
            }) : (void 0 === d && (d = c), void 0 !== b[c] && (a[d] = b[c]))
        }
        function Kb(a, b, c) {
            var d, e;
            for (e in b) b.hasOwnProperty(e) && (d = b[e], g.isPlainObject(d) ? (g.isPlainObject(a[e]) || (a[e] = {}), g.extend(!0, a[e], d)) : c && "data" !== e && "aaData" !== e && g.isArray(d) ? a[e] = d.slice() : a[e] = d);
            return a
        }
        function Ua(a, b, c) {
            g(a).bind("click.DT", b,
            function(b) {
                a.blur();
                c(b)
            }).bind("keypress.DT", b,
            function(a) {
                13 === a.which && (a.preventDefault(), c(a))
            }).bind("selectstart.DT",
            function() {
                return ! 1
            })
        }
        function z(a, b, c, d) {
            c && a[b].push({
                fn: c,
                sName: d
            })
        }
        function x(a, b, c, d) {
            var e = [];
            b && (e = g.map(a[b].slice().reverse(),
            function(b, c) {
                return b.fn.apply(a.oInstance, d)
            }));
            null !== c && g(a.nTable).trigger(c + ".dt", d);
            return e
        }
        function Ra(a) {
            var b = a._iDisplayStart,
            c = a.fnDisplayEnd(),
            d = a._iDisplayLength;
            b >= c && (b = c - d);
            b -= b % d;
            if ( - 1 === d || 0 > b) b = 0;
            a._iDisplayStart = b
        }
        function Oa(a, b) {
            var c = a.renderer,
            d = p.ext.renderer[b];
            return g.isPlainObject(c) && c[b] ? d[c[b]] || d._: "string" === typeof c ? d[c] || d._: d._
        }
        function D(a) {
            return a.oFeatures.bServerSide ? "ssp": a.ajax || a.sAjaxSource ? "ajax": "dom"
        }
        function Va(a, b) {
            var c = [],
            c = Lb.numbers_length,
            d = Math.floor(c / 2);
            b <= c ? c = T(0, b) : a <= d ? (c = T(0, c - 2), c.push("ellipsis"), c.push(b - 1)) : (a >= b - 1 - d ? c = T(b - (c - 2), b) : (c = T(a - 1, a + 2), c.push("ellipsis"), c.push(b - 1)), c.splice(0, 0, "ellipsis"), c.splice(0, 0, 0));
            c.DT_el = "span";
            return c
        }
        function cb(a) {
            g.each({
                num: function(b) {
                    return za(b, a)
                },
                "num-fmt": function(b) {
                    return za(b, a, Wa)
                },
                "html-num": function(b) {
                    return za(b, a, Aa)
                },
                "html-num-fmt": function(b) {
                    return za(b, a, Aa, Wa)
                }
            },
            function(b, c) {
                B.type.order[b + a + "-pre"] = c;
                b.match(/^html\-/) && (B.type.search[b + a] = B.type.search.html)
            })
        }
        function Mb(a) {
            return function() {
                var b = [ya(this[p.ext.iApiIndex])].concat(Array.prototype.slice.call(arguments));
                return p.ext.internal[a].apply(this, b)
            }
        }
        var p, B, u, t, v, Xa = {},
        Nb = /[\r\n]/g,
        Aa = /<.*?>/g,
        Zb = /^[\w\+\-]/,
        $b = /[\w\+\-]$/,
        Wb = RegExp("(\\/|\\.|\\*|\\+|\\?|\\||\\(|\\)|\\[|\\]|\\{|\\}|\\\\|\\$|\\^|\\-)", "g"),
        Wa = /[',$\u00a3\u20ac\u00a5%\u2009\u202F]/g,
        J = function(a) {
            return a && !0 !== a && "-" !== a ? !1 : !0
        },
        Ob = function(a) {
            var b = parseInt(a, 10);
            return ! isNaN(b) && isFinite(a) ? b: null
        },
        Pb = function(a, b) {
            Xa[b] || (Xa[b] = new RegExp(ua(b), "g"));
            return "string" === typeof a && "." !== b ? a.replace(/\./g, "").replace(Xa[b], ".") : a
        },
        Ya = function(a, b, c) {
            var d = "string" === typeof a;
            b && d && (a = Pb(a, b));
            c && d && (a = a.replace(Wa, ""));
            return J(a) || !isNaN(parseFloat(a)) && isFinite(a)
        },
        Qb = function(a, b, c) {
            return J(a) ? !0 : J(a) || "string" === typeof a ? Ya(a.replace(Aa, ""), b, c) ? !0 : null: null
        },
        F = function(a, b, c) {
            var d = [],
            e = 0,
            f = a.length;
            if (void 0 !== c) for (; e < f; e++) a[e] && a[e][b] && d.push(a[e][b][c]);
            else for (; e < f; e++) a[e] && d.push(a[e][b]);
            return d
        },
        ha = function(a, b, c, d) {
            var e = [],
            f = 0,
            h = b.length;
            if (void 0 !== d) for (; f < h; f++) a[b[f]][c] && e.push(a[b[f]][c][d]);
            else for (; f < h; f++) e.push(a[b[f]][c]);
            return e
        },
        T = function(a, b) {
            var c = [],
            d;
            void 0 === b ? (b = 0, d = a) : (d = b, b = a);
            for (var e = b; e < d; e++) c.push(e);
            return c
        },
        Rb = function(a) {
            for (var b = [], c = 0, d = a.length; c < d; c++) a[c] && b.push(a[c]);
            return b
        },
        Ma = function(a) {
            var b = [],
            c,
            d,
            e = a.length,
            f,
            h = 0;
            d = 0;
            a: for (; d < e; d++) {
                c = a[d];
                for (f = 0; f < h; f++) if (b[f] === c) continue a;
                b.push(c);
                h++
            }
            return b
        },
        C = function(a, b, c) {
            void 0 !== a[b] && (a[c] = a[b])
        },
        aa = /\[.*?\]$/,
        R = /\(\)$/,
        va = g("\x3cdiv\x3e")[0],
        Xb = void 0 !== va.textContent,
        Yb = /<.*?>/g;
        p = function(a) {
            this.$ = function(a, b) {
                return this.api(!0).$(a, b)
            };
            this._ = function(a, b) {
                return this.api(!0).rows(a, b).data()
            };
            this.api = function(a) {
                return a ? new u(ya(this[B.iApiIndex])) : new u(this)
            };
            this.fnAddData = function(a, b) {
                var c = this.api(!0),
                d = g.isArray(a) && (g.isArray(a[0]) || g.isPlainObject(a[0])) ? c.rows.add(a) : c.row.add(a); (void 0 === b || b) && c.draw();
                return d.flatten().toArray()
            };
            this.fnAdjustColumnSizing = function(a) {
                var b = this.api(!0).columns.adjust(),
                c = b.settings()[0],
                d = c.oScroll;
                void 0 === a || a ? b.draw(!1) : "" === d.sX && "" === d.sY || X(c)
            };
            this.fnClearTable = function(a) {
                var b = this.api(!0).clear(); (void 0 === a || a) && b.draw()
            };
            this.fnClose = function(a) {
                this.api(!0).row(a).child.hide()
            };
            this.fnDeleteRow = function(a, b, c) {
                var d = this.api(!0);
                a = d.rows(a);
                var e = a.settings()[0],
                g = e.aoData[a[0][0]];
                a.remove();
                b && b.call(this, e, g); (void 0 === c || c) && d.draw();
                return g
            };
            this.fnDestroy = function(a) {
                this.api(!0).destroy(a)
            };
            this.fnDraw = function(a) {
                this.api(!0).draw(!a)
            };
            this.fnFilter = function(a, b, c, d, e, g) {
                e = this.api(!0);
                null === b || void 0 === b ? e.search(a, c, d, g) : e.column(b).search(a, c, d, g);
                e.draw()
            };
            this.fnGetData = function(a, b) {
                var c = this.api(!0);
                if (void 0 !== a) {
                    var d = a.nodeName ? a.nodeName.toLowerCase() : "";
                    return void 0 !== b || "td" == d || "th" == d ? c.cell(a, b).data() : c.row(a).data() || null
                }
                return c.data().toArray()
            };
            this.fnGetNodes = function(a) {
                var b = this.api(!0);
                return void 0 !== a ? b.row(a).node() : b.rows().nodes().flatten().toArray()
            };
            this.fnGetPosition = function(a) {
                var b = this.api(!0),
                c = a.nodeName.toUpperCase();
                return "TR" == c ? b.row(a).index() : "TD" == c || "TH" == c ? (a = b.cell(a).index(), [a.row, a.columnVisible, a.column]) : null
            };
            this.fnIsOpen = function(a) {
                return this.api(!0).row(a).child.isShown()
            };
            this.fnOpen = function(a, b, c) {
                return this.api(!0).row(a).child(b, c).show().child()[0]
            };
            this.fnPageChange = function(a, b) {
                var c = this.api(!0).page(a); (void 0 === b || b) && c.draw(!1)
            };
            this.fnSetColumnVis = function(a, b, c) {
                a = this.api(!0).column(a).visible(b); (void 0 === c || c) && a.columns.adjust().draw()
            };
            this.fnSettings = function() {
                return ya(this[B.iApiIndex])
            };
            this.fnSort = function(a) {
                this.api(!0).order(a).draw()
            };
            this.fnSortListener = function(a, b, c) {
                this.api(!0).order.listener(a, b, c)
            };
            this.fnUpdate = function(a, b, c, d, e) {
                var g = this.api(!0);
                void 0 === c || null === c ? g.row(b).data(a) : g.cell(b, c).data(a); (void 0 === e || e) && g.columns.adjust(); (void 0 === d || d) && g.draw();
                return 0
            };
            this.fnVersionCheck = B.fnVersionCheck;
            var b = this,
            c = void 0 === a,
            d = this.length;
            c && (a = {});
            this.oApi = this.internal = B.internal;
            for (var e in p.ext.internal) e && (this[e] = Mb(e));
            this.each(function() {
                var e = {},
                e = 1 < d ? Kb(e, a, !0) : a,
                h = 0,
                k,
                l = this.getAttribute("id"),
                n = !1,
                m = p.defaults,
                r = g(this);
                if ("table" != this.nodeName.toLowerCase()) Q(null, 0, "Non-table node initialisation (" + this.nodeName + ")", 2);
                else {
                    db(m);
                    eb(m.column);
                    H(m, m, !0);
                    H(m.column, m.column, !0);
                    H(m, g.extend(e, r.data()));
                    var q = p.settings,
                    h = 0;
                    for (k = q.length; h < k; h++) {
                        var t = q[h];
                        if (t.nTable == this || t.nTHead.parentNode == this || t.nTFoot && t.nTFoot.parentNode == this) {
                            h = void 0 !== e.bRetrieve ? e.bRetrieve: m.bRetrieve;
                            if (c || h) return t.oInstance;
                            if (void 0 !== e.bDestroy ? e.bDestroy: m.bDestroy) {
                                t.oInstance.fnDestroy();
                                break
                            } else {
                                Q(t, 0, "Cannot reinitialise DataTable", 3);
                                return
                            }
                        }
                        if (t.sTableId == this.id) {
                            q.splice(h, 1);
                            break
                        }
                    }
                    if (null === l || "" === l) this.id = l = "DataTables_Table_" + p.ext._unique++;
                    var s = g.extend(!0, {},
                    p.models.oSettings, {
                        nTable: this,
                        oApi: b.internal,
                        oInit: e,
                        sDestroyWidth: r[0].style.width,
                        sInstance: l,
                        sTableId: l
                    });
                    q.push(s);
                    s.oInstance = 1 === b.length ? b: r.dataTable();
                    db(e);
                    e.oLanguage && Da(e.oLanguage);
                    e.aLengthMenu && !e.iDisplayLength && (e.iDisplayLength = g.isArray(e.aLengthMenu[0]) ? e.aLengthMenu[0][0] : e.aLengthMenu[0]);
                    e = Kb(g.extend(!0, {},
                    m), e);
                    G(s.oFeatures, e, "bPaginate bLengthChange bFilter bSort bSortMulti bInfo bProcessing bAutoWidth bSortClasses bServerSide bDeferRender".split(" "));
                    G(s, e, ["asStripeClasses", "ajax", "fnServerData", "fnFormatNumber", "sServerMethod", "aaSorting", "aaSortingFixed", "aLengthMenu", "sPaginationType", "sAjaxSource", "sAjaxDataProp", "iStateDuration", "sDom", "bSortCellsTop", "iTabIndex", "fnStateLoadCallback", "fnStateSaveCallback", "renderer", "searchDelay", ["iCookieDuration", "iStateDuration"], ["oSearch", "oPreviousSearch"], ["aoSearchCols", "aoPreSearchCols"], ["iDisplayLength", "_iDisplayLength"], ["bJQueryUI", "bJUI"]]);
                    G(s.oScroll, e, [["sScrollX", "sX"], ["sScrollXInner", "sXInner"], ["sScrollY", "sY"], ["bScrollCollapse", "bCollapse"]]);
                    G(s.oLanguage, e, "fnInfoCallback");
                    z(s, "aoDrawCallback", e.fnDrawCallback, "user");
                    z(s, "aoServerParams", e.fnServerParams, "user");
                    z(s, "aoStateSaveParams", e.fnStateSaveParams, "user");
                    z(s, "aoStateLoadParams", e.fnStateLoadParams, "user");
                    z(s, "aoStateLoaded", e.fnStateLoaded, "user");
                    z(s, "aoRowCallback", e.fnRowCallback, "user");
                    z(s, "aoRowCreatedCallback", e.fnCreatedRow, "user");
                    z(s, "aoHeaderCallback", e.fnHeaderCallback, "user");
                    z(s, "aoFooterCallback", e.fnFooterCallback, "user");
                    z(s, "aoInitComplete", e.fnInitComplete, "user");
                    z(s, "aoPreDrawCallback", e.fnPreDrawCallback, "user");
                    l = s.oClasses;
                    e.bJQueryUI ? (g.extend(l, p.ext.oJUIClasses, e.oClasses), e.sDom === m.sDom && "lfrtip" === m.sDom && (s.sDom = '\x3c"H"lfr\x3et\x3c"F"ip\x3e'), s.renderer) ? g.isPlainObject(s.renderer) && !s.renderer.header && (s.renderer.header = "jqueryui") : s.renderer = "jqueryui": g.extend(l, p.ext.classes, e.oClasses);
                    r.addClass(l.sTable);
                    if ("" !== s.oScroll.sX || "" !== s.oScroll.sY) s.oScroll.iBarWidth = Gb(); ! 0 === s.oScroll.sX && (s.oScroll.sX = "100%");
                    void 0 === s.iInitDisplayStart && (s.iInitDisplayStart = e.iDisplayStart, s._iDisplayStart = e.iDisplayStart);
                    null !== e.iDeferLoading && (s.bDeferLoading = !0, h = g.isArray(e.iDeferLoading), s._iRecordsDisplay = h ? e.iDeferLoading[0] : e.iDeferLoading, s._iRecordsTotal = h ? e.iDeferLoading[1] : e.iDeferLoading);
                    var u = s.oLanguage;
                    g.extend(!0, u, e.oLanguage);
                    "" !== u.sUrl && (g.ajax({
                        dataType: "json",
                        url: u.sUrl,
                        success: function(a) {
                            Da(a);
                            H(m.oLanguage, a);
                            g.extend(!0, u, a);
                            fa(s)
                        },
                        error: function() {
                            fa(s)
                        }
                    }), n = !0);
                    null === e.asStripeClasses && (s.asStripeClasses = [l.sStripeOdd, l.sStripeEven]);
                    var h = s.asStripeClasses,
                    w = g("tbody tr", this).eq(0); - 1 !== g.inArray(!0, g.map(h,
                    function(a, b) {
                        return w.hasClass(a)
                    })) && (g("tbody tr", this).removeClass(h.join(" ")), s.asDestroyStripes = h.slice());
                    q = [];
                    h = this.getElementsByTagName("thead");
                    0 !== h.length && (ca(s.aoHeader, h[0]), q = pa(s));
                    if (null === e.aoColumns) for (t = [], h = 0, k = q.length; h < k; h++) t.push(null);
                    else t = e.aoColumns;
                    h = 0;
                    for (k = t.length; h < k; h++) Ea(s, q ? q[h] : null);
                    hb(s, e.aoColumnDefs, t,
                    function(a, b) {
                        ia(s, a, b)
                    });
                    if (w.length) {
                        var v = function(a, b) {
                            return null !== a.getAttribute("data-" + b) ? b: null
                        };
                        g.each(la(s, w[0]).cells,
                        function(a, b) {
                            var c = s.aoColumns[a];
                            if (c.mData === a) {
                                var d = v(b, "sort") || v(b, "order"),
                                e = v(b, "filter") || v(b, "search");
                                if (null !== d || null !== e) c.mData = {
                                    _: a + ".display",
                                    sort: null !== d ? a + ".@data-" + d: void 0,
                                    type: null !== d ? a + ".@data-" + d: void 0,
                                    filter: null !== e ? a + ".@data-" + e: void 0
                                },
                                ia(s, a)
                            }
                        })
                    }
                    var y = s.oFeatures;
                    e.bStateSave && (y.bStateSave = !0, Jb(s, e), z(s, "aoDrawCallback", xa, "state_save"));
                    if (void 0 === e.aaSorting) for (q = s.aaSorting, h = 0, k = q.length; h < k; h++) q[h][1] = s.aoColumns[h].asSorting[0];
                    wa(s);
                    y.bSort && z(s, "aoDrawCallback",
                    function() {
                        if (s.bSorted) {
                            var a = S(s),
                            b = {};
                            g.each(a,
                            function(a, c) {
                                b[c.src] = c.dir
                            });
                            x(s, null, "order", [s, a, b]);
                            Ib(s)
                        }
                    });
                    z(s, "aoDrawCallback",
                    function() { (s.bSorted || "ssp" === D(s) || y.bDeferRender) && wa(s)
                    },
                    "sc");
                    fb(s);
                    h = r.children("caption").each(function() {
                        this._captionSide = r.css("caption-side")
                    });
                    k = r.children("thead");
                    0 === k.length && (k = g("\x3cthead/\x3e").appendTo(this));
                    s.nTHead = k[0];
                    k = r.children("tbody");
                    0 === k.length && (k = g("\x3ctbody/\x3e").appendTo(this));
                    s.nTBody = k[0];
                    k = r.children("tfoot");
                    0 === k.length && 0 < h.length && ("" !== s.oScroll.sX || "" !== s.oScroll.sY) && (k = g("\x3ctfoot/\x3e").appendTo(this));
                    0 === k.length || 0 === k.children().length ? r.addClass(l.sNoFooter) : 0 < k.length && (s.nTFoot = k[0], ca(s.aoFooter, s.nTFoot));
                    if (e.aaData) for (h = 0; h < e.aaData.length; h++) K(s, e.aaData[h]);
                    else(s.bDeferLoading || "dom" == D(s)) && ka(s, g(s.nTBody).children("tr"));
                    s.aiDisplay = s.aiDisplayMaster.slice();
                    s.bInitialised = !0; ! 1 === n && fa(s)
                }
            });
            b = null;
            return this
        };
        var Sb = [],
        y = Array.prototype,
        ac = function(a) {
            var b, c, d = p.settings,
            e = g.map(d,
            function(a, b) {
                return a.nTable
            });
            if (a) {
                if (a.nTable && a.oApi) return [a];
                if (a.nodeName && "table" === a.nodeName.toLowerCase()) return b = g.inArray(a, e),
                -1 !== b ? [d[b]] : null;
                if (a && "function" === typeof a.settings) return a.settings().toArray();
                "string" === typeof a ? c = g(a) : a instanceof g && (c = a)
            } else return [];
            if (c) return c.map(function(a) {
                b = g.inArray(this, e);
                return - 1 !== b ? d[b] : null
            }).toArray()
        };
        u = function(a, b) {
            if (!this instanceof u) throw "DT API must be constructed as a new object";
            var c = [],
            d = function(a) { (a = ac(a)) && c.push.apply(c, a)
            };
            if (g.isArray(a)) for (var e = 0,
            f = a.length; e < f; e++) d(a[e]);
            else d(a);
            this.context = Ma(c);
            b && this.push.apply(this, b.toArray ? b.toArray() : b);
            this.selector = {
                rows: null,
                cols: null,
                opts: null
            };
            u.extend(this, this, Sb)
        };
        p.Api = u;
        u.prototype = {
            concat: y.concat,
            context: [],
            each: function(a) {
                for (var b = 0,
                c = this.length; b < c; b++) a.call(this, this[b], b, this);
                return this
            },
            eq: function(a) {
                var b = this.context;
                return b.length > a ? new u(b[a], this[a]) : null
            },
            filter: function(a) {
                var b = [];
                if (y.filter) b = y.filter.call(this, a, this);
                else for (var c = 0,
                d = this.length; c < d; c++) a.call(this, this[c], c, this) && b.push(this[c]);
                return new u(this.context, b)
            },
            flatten: function() {
                var a = [];
                return new u(this.context, a.concat.apply(a, this.toArray()))
            },
            join: y.join,
            indexOf: y.indexOf ||
            function(a, b) {
                for (var c = b || 0,
                d = this.length; c < d; c++) if (this[c] === a) return c;
                return - 1
            },
            iterator: function(a, b, c, d) {
                var e = [],
                f,
                h,
                g,
                l,
                n,
                m = this.context,
                r,
                q,
                p = this.selector;
                "string" === typeof a && (d = c, c = b, b = a, a = !1);
                h = 0;
                for (g = m.length; h < g; h++) {
                    var s = new u(m[h]);
                    if ("table" === b) f = c.call(s, m[h], h),
                    void 0 !== f && e.push(f);
                    else if ("columns" === b || "rows" === b) f = c.call(s, m[h], this[h], h),
                    void 0 !== f && e.push(f);
                    else if ("column" === b || "column-rows" === b || "row" === b || "cell" === b) for (q = this[h], "column-rows" === b && (r = Ba(m[h], p.opts)), l = 0, n = q.length; l < n; l++) f = q[l],
                    f = "cell" === b ? c.call(s, m[h], f.row, f.column, h, l) : c.call(s, m[h], f, h, l, r),
                    void 0 !== f && e.push(f)
                }
                return e.length || d ? (a = new u(m, a ? e.concat.apply([], e) : e), b = a.selector, b.rows = p.rows, b.cols = p.cols, b.opts = p.opts, a) : this
            },
            lastIndexOf: y.lastIndexOf ||
            function(a, b) {
                return this.indexOf.apply(this.toArray.reverse(), arguments)
            },
            length: 0,
            map: function(a) {
                var b = [];
                if (y.map) b = y.map.call(this, a, this);
                else for (var c = 0,
                d = this.length; c < d; c++) b.push(a.call(this, this[c], c));
                return new u(this.context, b)
            },
            pluck: function(a) {
                return this.map(function(b) {
                    return b[a]
                })
            },
            pop: y.pop,
            push: y.push,
            reduce: y.reduce ||
            function(a, b) {
                return gb(this, a, b, 0, this.length, 1)
            },
            reduceRight: y.reduceRight ||
            function(a, b) {
                return gb(this, a, b, this.length - 1, -1, -1)
            },
            reverse: y.reverse,
            selector: null,
            shift: y.shift,
            sort: y.sort,
            splice: y.splice,
            toArray: function() {
                return y.slice.call(this)
            },
            to$: function() {
                return g(this)
            },
            toJQuery: function() {
                return g(this)
            },
            unique: function() {
                return new u(this.context, Ma(this))
            },
            unshift: y.unshift
        };
        u.extend = function(a, b, c) {
            if (c.length && b && (b instanceof u || b.__dt_wrapper)) {
                var d, e, f, h = function(a, b, c) {
                    return function() {
                        var d = b.apply(a, arguments);
                        u.extend(d, d, c.methodExt);
                        return d
                    }
                };
                d = 0;
                for (e = c.length; d < e; d++) f = c[d],
                b[f.name] = "function" === typeof f.val ? h(a, f.val, f) : g.isPlainObject(f.val) ? {}: f.val,
                b[f.name].__dt_wrapper = !0,
                u.extend(a, b[f.name], f.propExt)
            }
        };
        u.register = t = function(a, b) {
            if (g.isArray(a)) for (var c = 0,
            d = a.length; c < d; c++) u.register(a[c], b);
            else for (var e = a.split("."), f = Sb, h, k, c = 0, d = e.length; c < d; c++) {
                h = (k = -1 !== e[c].indexOf("()")) ? e[c].replace("()", "") : e[c];
                var l;
                a: {
                    l = 0;
                    for (var n = f.length; l < n; l++) if (f[l].name === h) {
                        l = f[l];
                        break a
                    }
                    l = null
                }
                l || (l = {
                    name: h,
                    val: {},
                    methodExt: [],
                    propExt: []
                },
                f.push(l));
                c === d - 1 ? l.val = b: f = k ? l.methodExt: l.propExt
            }
        };
        u.registerPlural = v = function(a, b, c) {
            u.register(a, c);
            u.register(b,
            function() {
                var a = c.apply(this, arguments);
                return a === this ? this: a instanceof u ? a.length ? g.isArray(a[0]) ? new u(a.context, a[0]) : a[0] : void 0 : a
            })
        };
        var bc = function(a, b) {
            if ("number" === typeof a) return [b[a]];
            var c = g.map(b,
            function(a, b) {
                return a.nTable
            });
            return g(c).filter(a).map(function(a) {
                a = g.inArray(this, c);
                return b[a]
            }).toArray()
        };
        t("tables()",
        function(a) {
            return a ? new u(bc(a, this.context)) : this
        });
        t("table()",
        function(a) {
            a = this.tables(a);
            var b = a.context;
            return b.length ? new u(b[0]) : a
        });
        v("tables().nodes()", "table().node()",
        function() {
            return this.iterator("table",
            function(a) {
                return a.nTable
            },
            1)
        });
        v("tables().body()", "table().body()",
        function() {
            return this.iterator("table",
            function(a) {
                return a.nTBody
            },
            1)
        });
        v("tables().header()", "table().header()",
        function() {
            return this.iterator("table",
            function(a) {
                return a.nTHead
            },
            1)
        });
        v("tables().footer()", "table().footer()",
        function() {
            return this.iterator("table",
            function(a) {
                return a.nTFoot
            },
            1)
        });
        v("tables().containers()", "table().container()",
        function() {
            return this.iterator("table",
            function(a) {
                return a.nTableWrapper
            },
            1)
        });
        t("draw()",
        function(a) {
            return this.iterator("table",
            function(b) {
                N(b, !1 === a)
            })
        });
        t("page()",
        function(a) {
            return void 0 === a ? this.page.info().page: this.iterator("table",
            function(b) {
                Sa(b, a)
            })
        });
        t("page.info()",
        function(a) {
            if (0 !== this.context.length) {
                a = this.context[0];
                var b = a._iDisplayStart,
                c = a._iDisplayLength,
                d = a.fnRecordsDisplay(),
                e = -1 === c;
                return {
                    page: e ? 0 : Math.floor(b / c),
                    pages: e ? 1 : Math.ceil(d / c),
                    start: b,
                    end: a.fnDisplayEnd(),
                    length: c,
                    recordsTotal: a.fnRecordsTotal(),
                    recordsDisplay: d
                }
            }
        });
        t("page.len()",
        function(a) {
            return void 0 === a ? 0 !== this.context.length ? this.context[0]._iDisplayLength: void 0 : this.iterator("table",
            function(b) {
                Qa(b, a)
            })
        });
        var Tb = function(a, b, c) {
            "ssp" == D(a) ? N(a, b) : (E(a, !0), qa(a, [],
            function(c) {
                ma(a);
                c = ra(a, c);
                for (var d = 0,
                h = c.length; d < h; d++) K(a, c[d]);
                N(a, b);
                E(a, !1)
            }));
            if (c) {
                var d = new u(a);
                d.one("draw",
                function() {
                    c(d.ajax.json())
                })
            }
        };
        t("ajax.json()",
        function() {
            var a = this.context;
            if (0 < a.length) return a[0].json
        });
        t("ajax.params()",
        function() {
            var a = this.context;
            if (0 < a.length) return a[0].oAjaxData
        });
        t("ajax.reload()",
        function(a, b) {
            return this.iterator("table",
            function(c) {
                Tb(c, !1 === b, a)
            })
        });
        t("ajax.url()",
        function(a) {
            var b = this.context;
            if (void 0 === a) {
                if (0 === b.length) return;
                b = b[0];
                return b.ajax ? g.isPlainObject(b.ajax) ? b.ajax.url: b.ajax: b.sAjaxSource
            }
            return this.iterator("table",
            function(b) {
                g.isPlainObject(b.ajax) ? b.ajax.url = a: b.ajax = a
            })
        });
        t("ajax.url().load()",
        function(a, b) {
            return this.iterator("table",
            function(c) {
                Tb(c, !1 === b, a)
            })
        });
        var Za = function(a, b) {
            var c = [],
            d,
            e,
            f,
            h,
            k,
            l;
            d = typeof a;
            a && "string" !== d && "function" !== d && void 0 !== a.length || (a = [a]);
            f = 0;
            for (h = a.length; f < h; f++) for (e = a[f] && a[f].split ? a[f].split(",") : [a[f]], k = 0, l = e.length; k < l; k++)(d = b("string" === typeof e[k] ? g.trim(e[k]) : e[k])) && d.length && c.push.apply(c, d);
            return c
        },
        $a = function(a) {
            a || (a = {});
            a.filter && !a.search && (a.search = a.filter);
            return {
                search: a.search || "none",
                order: a.order || "current",
                page: a.page || "all"
            }
        },
        ab = function(a) {
            for (var b = 0,
            c = a.length; b < c; b++) if (0 < a[b].length) return a[0] = a[b],
            a.length = 1,
            a.context = [a.context[b]],
            a;
            a.length = 0;
            return a
        },
        Ba = function(a, b) {
            var c, d, e, f = [],
            h = a.aiDisplay;
            c = a.aiDisplayMaster;
            var k = b.search;
            d = b.order;
            e = b.page;
            if ("ssp" == D(a)) return "removed" === k ? [] : T(0, c.length);
            if ("current" == e) for (c = a._iDisplayStart, d = a.fnDisplayEnd(); c < d; c++) f.push(h[c]);
            else if ("current" == d || "applied" == d) f = "none" == k ? c.slice() : "applied" == k ? h.slice() : g.map(c,
            function(a, b) {
                return - 1 === g.inArray(a, h) ? a: null
            });
            else if ("index" == d || "original" == d) for (c = 0, d = a.aoData.length; c < d; c++)"none" == k ? f.push(c) : (e = g.inArray(c, h), ( - 1 === e && "removed" == k || 0 <= e && "applied" == k) && f.push(c));
            return f
        },
        cc = function(a, b, c) {
            return Za(b,
            function(b) {
                var e = Ob(b);
                if (null !== e && !c) return [e];
                var f = Ba(a, c);
                if (null !== e && -1 !== g.inArray(e, f)) return [e];
                if (!b) return f;
                if ("function" === typeof b) return g.map(f,
                function(c) {
                    var e = a.aoData[c];
                    return b(c, e._aData, e.nTr) ? c: null
                });
                e = Rb(ha(a.aoData, f, "nTr"));
                return b.nodeName && -1 !== g.inArray(b, e) ? [b._DT_RowIndex] : g(e).filter(b).map(function() {
                    return this._DT_RowIndex
                }).toArray()
            })
        };
        t("rows()",
        function(a, b) {
            void 0 === a ? a = "": g.isPlainObject(a) && (b = a, a = "");
            b = $a(b);
            var c = this.iterator("table",
            function(c) {
                return cc(c, a, b)
            },
            1);
            c.selector.rows = a;
            c.selector.opts = b;
            return c
        });
        t("rows().nodes()",
        function() {
            return this.iterator("row",
            function(a, b) {
                return a.aoData[b].nTr || void 0
            },
            1)
        });
        t("rows().data()",
        function() {
            return this.iterator(!0, "rows",
            function(a, b) {
                return ha(a.aoData, b, "_aData")
            },
            1)
        });
        v("rows().cache()", "row().cache()",
        function(a) {
            return this.iterator("row",
            function(b, c) {
                var d = b.aoData[c];
                return "search" === a ? d._aFilterData: d._aSortData
            },
            1)
        });
        v("rows().invalidate()", "row().invalidate()",
        function(a) {
            return this.iterator("row",
            function(b, c) {
                ba(b, c, a)
            })
        });
        v("rows().indexes()", "row().index()",
        function() {
            return this.iterator("row",
            function(a, b) {
                return b
            },
            1)
        });
        v("rows().remove()", "row().remove()",
        function() {
            var a = this;
            return this.iterator("row",
            function(b, c, d) {
                var e = b.aoData;
                e.splice(c, 1);
                for (var f = 0,
                h = e.length; f < h; f++) null !== e[f].nTr && (e[f].nTr._DT_RowIndex = f);
                g.inArray(c, b.aiDisplay);
                na(b.aiDisplayMaster, c);
                na(b.aiDisplay, c);
                na(a[d], c, !1);
                Ra(b)
            })
        });
        t("rows.add()",
        function(a) {
            var b = this.iterator("table",
            function(b) {
                var c, f, h, g = [];
                f = 0;
                for (h = a.length; f < h; f++) c = a[f],
                c.nodeName && "TR" === c.nodeName.toUpperCase() ? g.push(ka(b, c)[0]) : g.push(K(b, c));
                return g
            },
            1),
            c = this.rows( - 1);
            c.pop();
            c.push.apply(c, b.toArray());
            return c
        });
        t("row()",
        function(a, b) {
            return ab(this.rows(a, b))
        });
        t("row().data()",
        function(a) {
            var b = this.context;
            if (void 0 === a) return b.length && this.length ? b[0].aoData[this[0]]._aData: void 0;
            b[0].aoData[this[0]]._aData = a;
            ba(b[0], this[0], "data");
            return this
        });
        t("row().node()",
        function() {
            var a = this.context;
            return a.length && this.length ? a[0].aoData[this[0]].nTr || null: null
        });
        t("row.add()",
        function(a) {
            a instanceof g && a.length && (a = a[0]);
            var b = this.iterator("table",
            function(b) {
                return a.nodeName && "TR" === a.nodeName.toUpperCase() ? ka(b, a)[0] : K(b, a)
            });
            return this.row(b[0])
        });
        var dc = function(a, b, c, d) {
            var e = [],
            f = function(b, c) {
                if (b.nodeName && "tr" === b.nodeName.toLowerCase()) e.push(b);
                else {
                    var d = g("\x3ctr\x3e\x3ctd/\x3e\x3c/tr\x3e").addClass(c);
                    g("td", d).addClass(c).html(b)[0].colSpan = $(a);
                    e.push(d[0])
                }
            };
            if (g.isArray(c) || c instanceof g) for (var h = 0,
            k = c.length; h < k; h++) f(c[h], d);
            else f(c, d);
            b._details && b._details.remove();
            b._details = g(e);
            b._detailsShow && b._details.insertAfter(b.nTr)
        },
        bb = function(a, b) {
            var c = a.context;
            c.length && (c = c[0].aoData[void 0 !== b ? b: a[0]], c._details && (c._details.remove(), c._detailsShow = void 0, c._details = void 0))
        },
        Ub = function(a, b) {
            var c = a.context;
            if (c.length && a.length) {
                var d = c[0].aoData[a[0]];
                d._details && ((d._detailsShow = b) ? d._details.insertAfter(d.nTr) : d._details.detach(), ec(c[0]))
            }
        },
        ec = function(a) {
            var b = new u(a),
            c = a.aoData;
            b.off("draw.dt.DT_details column-visibility.dt.DT_details destroy.dt.DT_details");
            0 < F(c, "_details").length && (b.on("draw.dt.DT_details",
            function(d, e) {
                a === e && b.rows({
                    page: "current"
                }).eq(0).each(function(a) {
                    a = c[a];
                    a._detailsShow && a._details.insertAfter(a.nTr)
                })
            }), b.on("column-visibility.dt.DT_details",
            function(b, e, f, h) {
                if (a === e) for (e = $(e), f = 0, h = c.length; f < h; f++) b = c[f],
                b._details && b._details.children("td[colspan]").attr("colspan", e)
            }), b.on("destroy.dt.DT_details",
            function(d, e) {
                if (a === e) for (var f = 0,
                h = c.length; f < h; f++) c[f]._details && bb(b, f)
            }))
        };
        t("row().child()",
        function(a, b) {
            var c = this.context;
            if (void 0 === a) return c.length && this.length ? c[0].aoData[this[0]]._details: void 0; ! 0 === a ? this.child.show() : !1 === a ? bb(this) : c.length && this.length && dc(c[0], c[0].aoData[this[0]], a, b);
            return this
        });
        t(["row().child.show()", "row().child().show()"],
        function(a) {
            Ub(this, !0);
            return this
        });
        t(["row().child.hide()", "row().child().hide()"],
        function() {
            Ub(this, !1);
            return this
        });
        t(["row().child.remove()", "row().child().remove()"],
        function() {
            bb(this);
            return this
        });
        t("row().child.isShown()",
        function() {
            var a = this.context;
            return a.length && this.length ? a[0].aoData[this[0]]._detailsShow || !1 : !1
        });
        var fc = /^(.+):(name|visIdx|visible)$/,
        Vb = function(a, b, c, d, e) {
            c = [];
            d = 0;
            for (var f = e.length; d < f; d++) c.push(A(a, e[d], b));
            return c
        },
        gc = function(a, b, c) {
            var d = a.aoColumns,
            e = F(d, "sName"),
            f = F(d, "nTh");
            return Za(b,
            function(b) {
                var k = Ob(b);
                if ("" === b) return T(d.length);
                if (null !== k) return [0 <= k ? k: d.length + k];
                if ("function" === typeof b) {
                    var l = Ba(a, c);
                    return g.map(d,
                    function(c, d) {
                        return b(d, Vb(a, d, 0, 0, l), f[d]) ? d: null
                    })
                }
                var n = "string" === typeof b ? b.match(fc) : "";
                if (n) switch (n[2]) {
                case "visIdx":
                case "visible":
                    k = parseInt(n[1], 10);
                    if (0 > k) {
                        var m = g.map(d,
                        function(a, b) {
                            return a.bVisible ? b: null
                        });
                        return [m[m.length + k]]
                    }
                    return [ja(a, k)];
                case "name":
                    return g.map(e,
                    function(a, b) {
                        return a === n[1] ? b: null
                    })
                } else return g(f).filter(b).map(function() {
                    return g.inArray(this, f)
                }).toArray()
            })
        };
        t("columns()",
        function(a, b) {
            void 0 === a ? a = "": g.isPlainObject(a) && (b = a, a = "");
            b = $a(b);
            var c = this.iterator("table",
            function(c) {
                return gc(c, a, b)
            },
            1);
            c.selector.cols = a;
            c.selector.opts = b;
            return c
        });
        v("columns().header()", "column().header()",
        function(a, b) {
            return this.iterator("column",
            function(a, b) {
                return a.aoColumns[b].nTh
            },
            1)
        });
        v("columns().footer()", "column().footer()",
        function(a, b) {
            return this.iterator("column",
            function(a, b) {
                return a.aoColumns[b].nTf
            },
            1)
        });
        v("columns().data()", "column().data()",
        function() {
            return this.iterator("column-rows", Vb, 1)
        });
        v("columns().dataSrc()", "column().dataSrc()",
        function() {
            return this.iterator("column",
            function(a, b) {
                return a.aoColumns[b].mData
            },
            1)
        });
        v("columns().cache()", "column().cache()",
        function(a) {
            return this.iterator("column-rows",
            function(b, c, d, e, f) {
                return ha(b.aoData, f, "search" === a ? "_aFilterData": "_aSortData", c)
            },
            1)
        });
        v("columns().nodes()", "column().nodes()",
        function() {
            return this.iterator("column-rows",
            function(a, b, c, d, e) {
                return ha(a.aoData, e, "anCells", b)
            },
            1)
        });
        v("columns().visible()", "column().visible()",
        function(a, b) {
            return this.iterator("column",
            function(c, d) {
                if (void 0 === a) return c.aoColumns[d].bVisible;
                var e = c.aoColumns,
                f = e[d],
                h = c.aoData,
                k,
                l,
                n;
                if (void 0 !== a && f.bVisible !== a) {
                    if (a) {
                        var m = g.inArray(!0, F(e, "bVisible"), d + 1);
                        k = 0;
                        for (l = h.length; k < l; k++) n = h[k].nTr,
                        e = h[k].anCells,
                        n && n.insertBefore(e[d], e[m] || null)
                    } else g(F(c.aoData, "anCells", d)).detach();
                    f.bVisible = a;
                    da(c, c.aoHeader);
                    da(c, c.aoFooter);
                    if (void 0 === b || b) W(c),
                    (c.oScroll.sX || c.oScroll.sY) && X(c);
                    x(c, null, "column-visibility", [c, d, a]);
                    xa(c)
                }
            })
        });
        v("columns().indexes()", "column().index()",
        function(a) {
            return this.iterator("column",
            function(b, c) {
                return "visible" === a ? Z(b, c) : c
            },
            1)
        });
        t("columns.adjust()",
        function() {
            return this.iterator("table",
            function(a) {
                W(a)
            },
            1)
        });
        t("column.index()",
        function(a, b) {
            if (0 !== this.context.length) {
                var c = this.context[0];
                if ("fromVisible" === a || "toData" === a) return ja(c, b);
                if ("fromData" === a || "toVisible" === a) return Z(c, b)
            }
        });
        t("column()",
        function(a, b) {
            return ab(this.columns(a, b))
        });
        var hc = function(a, b, c) {
            var d = a.aoData,
            e = Ba(a, c);
            c = Rb(ha(d, e, "anCells"));
            var f = g([].concat.apply([], c)),
            h,
            k = a.aoColumns.length,
            l,
            n,
            m,
            p,
            q,
            t;
            return Za(b,
            function(b) {
                var c = "function" === typeof b;
                if (null === b || void 0 === b || c) {
                    l = [];
                    n = 0;
                    for (m = e.length; n < m; n++) for (h = e[n], p = 0; p < k; p++) q = {
                        row: h,
                        column: p
                    },
                    c ? (t = a.aoData[h], b(q, A(a, h, p), t.anCells[p]) && l.push(q)) : l.push(q);
                    return l
                }
                return g.isPlainObject(b) ? [b] : f.filter(b).map(function(a, b) {
                    h = b.parentNode._DT_RowIndex;
                    return {
                        row: h,
                        column: g.inArray(b, d[h].anCells)
                    }
                }).toArray()
            })
        };
        t("cells()",
        function(a, b, c) {
            g.isPlainObject(a) && (void 0 !== typeof a.row ? (c = b, b = null) : (c = a, a = null));
            g.isPlainObject(b) && (c = b, b = null);
            if (null === b || void 0 === b) return this.iterator("table",
            function(b) {
                return hc(b, a, $a(c))
            });
            var d = this.columns(b, c),
            e = this.rows(a, c),
            f,
            h,
            k,
            l,
            n,
            m = this.iterator("table",
            function(a, b) {
                f = [];
                h = 0;
                for (k = e[b].length; h < k; h++) for (l = 0, n = d[b].length; l < n; l++) f.push({
                    row: e[b][h],
                    column: d[b][l]
                });
                return f
            },
            1);
            g.extend(m.selector, {
                cols: b,
                rows: a,
                opts: c
            });
            return m
        });
        v("cells().nodes()", "cell().node()",
        function() {
            return this.iterator("cell",
            function(a, b, c) {
                return (a = a.aoData[b].anCells) ? a[c] : void 0
            },
            1)
        });
        t("cells().data()",
        function() {
            return this.iterator("cell",
            function(a, b, c) {
                return A(a, b, c)
            },
            1)
        });
        v("cells().cache()", "cell().cache()",
        function(a) {
            a = "search" === a ? "_aFilterData": "_aSortData";
            return this.iterator("cell",
            function(b, c, d) {
                return b.aoData[c][a][d]
            },
            1)
        });
        v("cells().render()", "cell().render()",
        function(a) {
            return this.iterator("cell",
            function(b, c, d) {
                return A(b, c, d, a)
            },
            1)
        });
        v("cells().indexes()", "cell().index()",
        function() {
            return this.iterator("cell",
            function(a, b, c) {
                return {
                    row: b,
                    column: c,
                    columnVisible: Z(a, c)
                }
            },
            1)
        });
        v("cells().invalidate()", "cell().invalidate()",
        function(a) {
            return this.iterator("cell",
            function(b, c, d) {
                ba(b, c, a, d)
            })
        });
        t("cell()",
        function(a, b, c) {
            return ab(this.cells(a, b, c))
        });
        t("cell().data()",
        function(a) {
            var b = this.context,
            c = this[0];
            if (void 0 === a) return b.length && c.length ? A(b[0], c[0].row, c[0].column) : void 0;
            Ha(b[0], c[0].row, c[0].column, a);
            ba(b[0], c[0].row, "data", c[0].column);
            return this
        });
        t("order()",
        function(a, b) {
            var c = this.context;
            if (void 0 === a) return 0 !== c.length ? c[0].aaSorting: void 0;
            "number" === typeof a ? a = [[a, b]] : g.isArray(a[0]) || (a = Array.prototype.slice.call(arguments));
            return this.iterator("table",
            function(b) {
                b.aaSorting = a.slice()
            })
        });
        t("order.listener()",
        function(a, b, c) {
            return this.iterator("table",
            function(d) {
                Na(d, a, b, c)
            })
        });
        t(["columns().order()", "column().order()"],
        function(a) {
            var b = this;
            return this.iterator("table",
            function(c, d) {
                var e = [];
                g.each(b[d],
                function(b, c) {
                    e.push([c, a])
                });
                c.aaSorting = e
            })
        });
        t("search()",
        function(a, b, c, d) {
            var e = this.context;
            return void 0 === a ? 0 !== e.length ? e[0].oPreviousSearch.sSearch: void 0 : this.iterator("table",
            function(e) {
                e.oFeatures.bFilter && ea(e, g.extend({},
                e.oPreviousSearch, {
                    sSearch: a + "",
                    bRegex: null === b ? !1 : b,
                    bSmart: null === c ? !0 : c,
                    bCaseInsensitive: null === d ? !0 : d
                }), 1)
            })
        });
        v("columns().search()", "column().search()",
        function(a, b, c, d) {
            return this.iterator("column",
            function(e, f) {
                var h = e.aoPreSearchCols;
                if (void 0 === a) return h[f].sSearch;
                e.oFeatures.bFilter && (g.extend(h[f], {
                    sSearch: a + "",
                    bRegex: null === b ? !1 : b,
                    bSmart: null === c ? !0 : c,
                    bCaseInsensitive: null === d ? !0 : d
                }), ea(e, e.oPreviousSearch, 1))
            })
        });
        t("state()",
        function() {
            return this.context.length ? this.context[0].oSavedState: null
        });
        t("state.clear()",
        function() {
            return this.iterator("table",
            function(a) {
                a.fnStateSaveCallback.call(a.oInstance, a, {})
            })
        });
        t("state.loaded()",
        function() {
            return this.context.length ? this.context[0].oLoadedState: null
        });
        t("state.save()",
        function() {
            return this.iterator("table",
            function(a) {
                xa(a)
            })
        });
        p.versionCheck = p.fnVersionCheck = function(a) {
            var b = p.version.split(".");
            a = a.split(".");
            for (var c, d, e = 0,
            f = a.length; e < f; e++) if (c = parseInt(b[e], 10) || 0, d = parseInt(a[e], 10) || 0, c !== d) return c > d;
            return ! 0
        };
        p.isDataTable = p.fnIsDataTable = function(a) {
            var b = g(a).get(0),
            c = !1;
            g.each(p.settings,
            function(a, e) {
                if (e.nTable === b || g("table", e.nScrollHead)[0] === b || g("table", e.nScrollFoot)[0] === b) c = !0
            });
            return c
        };
        p.tables = p.fnTables = function(a) {
            return g.map(p.settings,
            function(b) {
                if (!a || a && g(b.nTable).is(":visible")) return b.nTable
            })
        };
        p.util = {
            throttle: ta,
            escapeRegex: ua
        };
        p.camelToHungarian = H;
        t("$()",
        function(a, b) {
            var c = this.rows(b).nodes(),
            c = g(c);
            return g([].concat(c.filter(a).toArray(), c.find(a).toArray()))
        });
        g.each(["on", "one", "off"],
        function(a, b) {
            t(b + "()",
            function() {
                var a = Array.prototype.slice.call(arguments);
                a[0].match(/\.dt\b/) || (a[0] += ".dt");
                var d = g(this.tables().nodes());
                d[b].apply(d, a);
                return this
            })
        });
        t("clear()",
        function() {
            return this.iterator("table",
            function(a) {
                ma(a)
            })
        });
        t("settings()",
        function() {
            return new u(this.context, this.context)
        });
        t("data()",
        function() {
            return this.iterator("table",
            function(a) {
                return F(a.aoData, "_aData")
            }).flatten()
        });
        t("destroy()",
        function(a) {
            a = a || !1;
            return this.iterator("table",
            function(b) {
                var c = b.nTableWrapper.parentNode,
                d = b.oClasses,
                e = b.nTable,
                f = b.nTBody,
                h = b.nTHead,
                k = b.nTFoot,
                l = g(e),
                f = g(f),
                n = g(b.nTableWrapper),
                m = g.map(b.aoData,
                function(a) {
                    return a.nTr
                }),
                r;
                b.bDestroying = !0;
                x(b, "aoDestroyCallback", "destroy", [b]);
                a || (new u(b)).columns().visible(!0);
                n.unbind(".DT").find(":not(tbody *)").unbind(".DT");
                g(Ca).unbind(".DT-" + b.sInstance);
                e != h.parentNode && (l.children("thead").detach(), l.append(h));
                k && e != k.parentNode && (l.children("tfoot").detach(), l.append(k));
                l.detach();
                n.detach();
                b.aaSorting = [];
                b.aaSortingFixed = [];
                wa(b);
                g(m).removeClass(b.asStripeClasses.join(" "));
                g("th, td", h).removeClass(d.sSortable + " " + d.sSortableAsc + " " + d.sSortableDesc + " " + d.sSortableNone);
                b.bJUI && (g("th span." + d.sSortIcon + ", td span." + d.sSortIcon, h).detach(), g("th, td", h).each(function() {
                    var a = g("div." + d.sSortJUIWrapper, this);
                    g(this).append(a.contents());
                    a.detach()
                })); ! a && c && c.insertBefore(e, b.nTableReinsertBefore);
                f.children().detach();
                f.append(m);
                l.css("width", b.sDestroyWidth).removeClass(d.sTable); (r = b.asDestroyStripes.length) && f.children().each(function(a) {
                    g(this).addClass(b.asDestroyStripes[a % r])
                });
                c = g.inArray(b, p.settings); - 1 !== c && p.settings.splice(c, 1)
            })
        });
        p.version = "1.10.5";
        p.settings = [];
        p.models = {};
        p.models.oSearch = {
            bCaseInsensitive: !0,
            sSearch: "",
            bRegex: !1,
            bSmart: !0
        };
        p.models.oRow = {
            nTr: null,
            anCells: null,
            _aData: [],
            _aSortData: null,
            _aFilterData: null,
            _sFilterRow: null,
            _sRowStripe: "",
            src: null
        };
        p.models.oColumn = {
            idx: null,
            aDataSort: null,
            asSorting: null,
            bSearchable: null,
            bSortable: null,
            bVisible: null,
            _sManualType: null,
            _bAttrSrc: !1,
            fnCreatedCell: null,
            fnGetData: null,
            fnSetData: null,
            mData: null,
            mRender: null,
            nTh: null,
            nTf: null,
            sClass: null,
            sContentPadding: null,
            sDefaultContent: null,
            sName: null,
            sSortDataType: "std",
            sSortingClass: null,
            sSortingClassJUI: null,
            sTitle: null,
            sType: null,
            sWidth: null,
            sWidthOrig: null
        };
        p.defaults = {
            aaData: null,
            aaSorting: [[0, "asc"]],
            aaSortingFixed: [],
            ajax: null,
            aLengthMenu: [10, 25, 50, 100],
            aoColumns: null,
            aoColumnDefs: null,
            aoSearchCols: [],
            asStripeClasses: null,
            bAutoWidth: !0,
            bDeferRender: !1,
            bDestroy: !1,
            bFilter: !0,
            bInfo: !0,
            bJQueryUI: !1,
            bLengthChange: !0,
            bPaginate: !0,
            bProcessing: !1,
            bRetrieve: !1,
            bScrollCollapse: !1,
            bServerSide: !1,
            bSort: !0,
            bSortMulti: !0,
            bSortCellsTop: !1,
            bSortClasses: !0,
            bStateSave: !1,
            fnCreatedRow: null,
            fnDrawCallback: null,
            fnFooterCallback: null,
            fnFormatNumber: function(a) {
                return a.toString().replace(/\B(?=(\d{3})+(?!\d))/g, this.oLanguage.sThousands)
            },
            fnHeaderCallback: null,
            fnInfoCallback: null,
            fnInitComplete: null,
            fnPreDrawCallback: null,
            fnRowCallback: null,
            fnServerData: null,
            fnServerParams: null,
            fnStateLoadCallback: function(a) {
                try {
                    return JSON.parse(( - 1 === a.iStateDuration ? sessionStorage: localStorage).getItem("DataTables_" + a.sInstance + "_" + location.pathname))
                } catch(b) {}
            },
            fnStateLoadParams: null,
            fnStateLoaded: null,
            fnStateSaveCallback: function(a, b) {
                try { ( - 1 === a.iStateDuration ? sessionStorage: localStorage).setItem("DataTables_" + a.sInstance + "_" + location.pathname, JSON.stringify(b))
                } catch(c) {}
            },
            fnStateSaveParams: null,
            iStateDuration: 7200,
            iDeferLoading: null,
            iDisplayLength: 10,
            iDisplayStart: 0,
            iTabIndex: 0,
            oClasses: {},
            oLanguage: {
                oAria: {
                    sSortAscending: ": activate to sort column ascending",
                    sSortDescending: ": activate to sort column descending"
                },
                oPaginate: {
                    sFirst: "First",
                    sLast: "Last",
                    sNext: "Next",
                    sPrevious: "Previous"
                },
                sEmptyTable: "No data available in table",
                sInfo: "Showing _START_ to _END_ of _TOTAL_ entries",
                sInfoEmpty: "Showing 0 to 0 of 0 entries",
                sInfoFiltered: "(filtered from _MAX_ total entries)",
                sInfoPostFix: "",
                sDecimal: "",
                sThousands: ",",
                sLengthMenu: "Show _MENU_ entries",
                sLoadingRecords: "Loading...",
                sProcessing: "Processing...",
                sSearch: "Search:",
                sSearchPlaceholder: "",
                sUrl: "",
                sZeroRecords: "No matching records found"
            },
            oSearch: g.extend({},
            p.models.oSearch),
            sAjaxDataProp: "data",
            sAjaxSource: null,
            sDom: "lfrtip",
            searchDelay: null,
            sPaginationType: "simple_numbers",
            sScrollX: "",
            sScrollXInner: "",
            sScrollY: "",
            sServerMethod: "GET",
            renderer: null
        };
        U(p.defaults);
        p.defaults.column = {
            aDataSort: null,
            iDataSort: -1,
            asSorting: ["desc", "asc"],
            bSearchable: !0,
            bSortable: !0,
            bVisible: !0,
            fnCreatedCell: null,
            mData: null,
            mRender: null,
            sCellType: "td",
            sClass: "",
            sContentPadding: "",
            sDefaultContent: null,
            sName: "",
            sSortDataType: "std",
            sTitle: null,
            sType: null,
            sWidth: null
        };
        U(p.defaults.column);
        p.models.oSettings = {
            oFeatures: {
                bAutoWidth: null,
                bDeferRender: null,
                bFilter: null,
                bInfo: null,
                bLengthChange: null,
                bPaginate: null,
                bProcessing: null,
                bServerSide: null,
                bSort: null,
                bSortMulti: null,
                bSortClasses: null,
                bStateSave: null
            },
            oScroll: {
                bCollapse: null,
                iBarWidth: 0,
                sX: null,
                sXInner: null,
                sY: null
            },
            oLanguage: {
                fnInfoCallback: null
            },
            oBrowser: {
                bScrollOversize: !1,
                bScrollbarLeft: !1
            },
            ajax: null,
            aanFeatures: [],
            aoData: [],
            aiDisplay: [],
            aiDisplayMaster: [],
            aoColumns: [],
            aoHeader: [],
            aoFooter: [],
            oPreviousSearch: {},
            aoPreSearchCols: [],
            aaSorting: null,
            aaSortingFixed: [],
            asStripeClasses: null,
            asDestroyStripes: [],
            sDestroyWidth: 0,
            aoRowCallback: [],
            aoHeaderCallback: [],
            aoFooterCallback: [],
            aoDrawCallback: [],
            aoRowCreatedCallback: [],
            aoPreDrawCallback: [],
            aoInitComplete: [],
            aoStateSaveParams: [],
            aoStateLoadParams: [],
            aoStateLoaded: [],
            sTableId: "",
            nTable: null,
            nTHead: null,
            nTFoot: null,
            nTBody: null,
            nTableWrapper: null,
            bDeferLoading: !1,
            bInitialised: !1,
            aoOpenRows: [],
            sDom: null,
            searchDelay: null,
            sPaginationType: "two_button",
            iStateDuration: 0,
            aoStateSave: [],
            aoStateLoad: [],
            oSavedState: null,
            oLoadedState: null,
            sAjaxSource: null,
            sAjaxDataProp: null,
            bAjaxDataGet: !0,
            jqXHR: null,
            json: void 0,
            oAjaxData: void 0,
            fnServerData: null,
            aoServerParams: [],
            sServerMethod: null,
            fnFormatNumber: null,
            aLengthMenu: null,
            iDraw: 0,
            bDrawing: !1,
            iDrawError: -1,
            _iDisplayLength: 10,
            _iDisplayStart: 0,
            _iRecordsTotal: 0,
            _iRecordsDisplay: 0,
            bJUI: null,
            oClasses: {},
            bFiltered: !1,
            bSorted: !1,
            bSortCellsTop: null,
            oInit: null,
            aoDestroyCallback: [],
            fnRecordsTotal: function() {
                return "ssp" == D(this) ? 1 * this._iRecordsTotal: this.aiDisplayMaster.length
            },
            fnRecordsDisplay: function() {
                return "ssp" == D(this) ? 1 * this._iRecordsDisplay: this.aiDisplay.length
            },
            fnDisplayEnd: function() {
                var a = this._iDisplayLength,
                b = this._iDisplayStart,
                c = b + a,
                d = this.aiDisplay.length,
                e = this.oFeatures,
                f = e.bPaginate;
                return e.bServerSide ? !1 === f || -1 === a ? b + d: Math.min(b + a, this._iRecordsDisplay) : !f || c > d || -1 === a ? d: c
            },
            oInstance: null,
            sInstance: null,
            iTabIndex: 0,
            nScrollHead: null,
            nScrollFoot: null,
            aLastSort: [],
            oPlugins: {}
        };
        p.ext = B = {
            buttons: {},
            classes: {},
            errMode: "alert",
            feature: [],
            search: [],
            internal: {},
            legacy: {
                ajax: null
            },
            pager: {},
            renderer: {
                pageButton: {},
                header: {}
            },
            order: {},
            type: {
                detect: [],
                search: {},
                order: {}
            },
            _unique: 0,
            fnVersionCheck: p.fnVersionCheck,
            iApiIndex: 0,
            oJUIClasses: {},
            sVersion: p.version
        };
        g.extend(B, {
            afnFiltering: B.search,
            aTypes: B.type.detect,
            ofnSearch: B.type.search,
            oSort: B.type.order,
            afnSortData: B.order,
            aoFeatures: B.feature,
            oApi: B.internal,
            oStdClasses: B.classes,
            oPagination: B.pager
        });
        g.extend(p.ext.classes, {
            sTable: "dataTable",
            sNoFooter: "no-footer",
            sPageButton: "paginate_button",
            sPageButtonActive: "current",
            sPageButtonDisabled: "disabled",
            sStripeOdd: "odd",
            sStripeEven: "even",
            sRowEmpty: "dataTables_empty",
            sWrapper: "dataTables_wrapper",
            sFilter: "dataTables_filter",
            sInfo: "dataTables_info",
            sPaging: "dataTables_paginate paging_",
            sLength: "dataTables_length",
            sProcessing: "dataTables_processing",
            sSortAsc: "sorting_asc",
            sSortDesc: "sorting_desc",
            sSortable: "sorting",
            sSortableAsc: "sorting_asc_disabled",
            sSortableDesc: "sorting_desc_disabled",
            sSortableNone: "sorting_disabled",
            sSortColumn: "sorting_",
            sFilterInput: "",
            sLengthSelect: "",
            sScrollWrapper: "dataTables_scroll eui-scroll",
            sScrollHead: "dataTables_scrollHead",
            sScrollHeadInner: "dataTables_scrollHeadInner",
            sScrollBody: "dataTables_scrollBody",
            sScrollFoot: "dataTables_scrollFoot",
            sScrollFootInner: "dataTables_scrollFootInner",
            sHeaderTH: "",
            sFooterTH: "",
            sSortJUIAsc: "",
            sSortJUIDesc: "",
            sSortJUI: "",
            sSortJUIAscAllowed: "",
            sSortJUIDescAllowed: "",
            sSortJUIWrapper: "",
            sSortIcon: "",
            sJUIHeader: "",
            sJUIFooter: ""
        }); (function() {
            var a = "",
            a = "",
            b = a + "ui-state-default",
            c = a + "css_right ui-icon ui-icon-",
            a = a + "fg-toolbar ui-toolbar ui-widget-header ui-helper-clearfix";
            g.extend(p.ext.oJUIClasses, p.ext.classes, {
                sPageButton: "fg-button ui-button " + b,
                sPageButtonActive: "ui-state-disabled",
                sPageButtonDisabled: "ui-state-disabled",
                sPaging: "dataTables_paginate fg-buttonset ui-buttonset fg-buttonset-multi ui-buttonset-multi paging_",
                sSortAsc: b + " sorting_asc",
                sSortDesc: b + " sorting_desc",
                sSortable: b + " sorting",
                sSortableAsc: b + " sorting_asc_disabled",
                sSortableDesc: b + " sorting_desc_disabled",
                sSortableNone: b + " sorting_disabled",
                sSortJUIAsc: c + "triangle-1-n",
                sSortJUIDesc: c + "triangle-1-s",
                sSortJUI: c + "carat-2-n-s",
                sSortJUIAscAllowed: c + "carat-1-n",
                sSortJUIDescAllowed: c + "carat-1-s",
                sSortJUIWrapper: "DataTables_sort_wrapper",
                sSortIcon: "DataTables_sort_icon",
                sScrollHead: "dataTables_scrollHead " + b,
                sScrollFoot: "dataTables_scrollFoot " + b,
                sHeaderTH: b,
                sFooterTH: b,
                sJUIHeader: a + " ui-corner-tl ui-corner-tr",
                sJUIFooter: a + " ui-corner-bl ui-corner-br"
            })
        })();
        var Lb = p.ext.pager;
        g.extend(Lb, {
            simple: function(a, b) {
                return ["previous", "next"]
            },
            full: function(a, b) {
                return ["first", "previous", "next", "last"]
            },
            simple_numbers: function(a, b) {
                return ["previous", Va(a, b), "next"]
            },
            full_numbers: function(a, b) {
                return ["first", "previous", Va(a, b), "next", "last"]
            },
            _numbers: Va,
            numbers_length: 7
        });
        g.extend(!0, p.ext.renderer, {
            pageButton: {
                _: function(a, b, c, d, e, f) {
                    var h = a.oClasses,
                    k = a.oLanguage.oPaginate,
                    l, n, m = 0,
                    p = function(b, d) {
                        var q, t, u, v, w = function(b) {
                            Sa(a, b.data.action, !0)
                        };
                        q = 0;
                        for (t = d.length; q < t; q++) if (v = d[q], g.isArray(v)) u = g("\x3c" + (v.DT_el || "div") + "/\x3e").appendTo(b),
                        p(u, v);
                        else {
                            n = l = "";
                            switch (v) {
                            case "ellipsis":
                                b.append("\x3cspan\x3e\x26hellip;\x3c/span\x3e");
                                break;
                            case "first":
                                l = k.sFirst;
                                n = v + (0 < e ? "": " " + h.sPageButtonDisabled);
                                break;
                            case "previous":
                                l = k.sPrevious;
                                n = v + (0 < e ? "": " " + h.sPageButtonDisabled);
                                break;
                            case "next":
                                l = k.sNext;
                                n = v + (e < f - 1 ? "": " " + h.sPageButtonDisabled);
                                break;
                            case "last":
                                l = k.sLast;
                                n = v + (e < f - 1 ? "": " " + h.sPageButtonDisabled);
                                break;
                            default:
                                l = v + 1,
                                n = e === v ? h.sPageButtonActive: ""
                            }
                            l && (u = g("\x3ca\x3e", {
                                "class": h.sPageButton + " " + n,
                                "aria-controls": a.sTableId,
                                "data-dt-idx": m,
                                tabindex: a.iTabIndex,
                                id: 0 === c && "string" === typeof v ? a.sTableId + "_" + v: null
                            }).html(l).appendTo(b), Ua(u, {
                                action: v
                            },
                            w), m++)
                        }
                    },
                    q;
                    try {
                        q = g(O.activeElement).data("dt-idx")
                    } catch(t) {}
                    p(g(b).empty(), d);
                    q && g(b).find("[data-dt-idx\x3d" + q + "]").focus()
                }
            }
        });
        g.extend(p.ext.type.detect, [function(a, b) {
            var c = b.oLanguage.sDecimal;
            return Ya(a, c) ? "num" + c: null
        },
        function(a, b) {
            if (! (!a || a instanceof Date || Zb.test(a) && $b.test(a))) return null;
            var c = Date.parse(a);
            return null !== c && !isNaN(c) || J(a) ? "date": null
        },
        function(a, b) {
            var c = b.oLanguage.sDecimal;
            return Ya(a, c, !0) ? "num-fmt" + c: null
        },
        function(a, b) {
            var c = b.oLanguage.sDecimal;
            return Qb(a, c) ? "html-num" + c: null
        },
        function(a, b) {
            var c = b.oLanguage.sDecimal;
            return Qb(a, c, !0) ? "html-num-fmt" + c: null
        },
        function(a, b) {
            return J(a) || "string" === typeof a && -1 !== a.indexOf("\x3c") ? "html": null
        }]);
        g.extend(p.ext.type.search, {
            html: function(a) {
                return J(a) ? a: "string" === typeof a ? a.replace(Nb, " ").replace(Aa, "") : ""
            },
            string: function(a) {
                return J(a) ? a: "string" === typeof a ? a.replace(Nb, " ") : a
            }
        });
        var za = function(a, b, c, d) {
            if (0 !== a && (!a || "-" === a)) return - Infinity;
            b && (a = Pb(a, b));
            a.replace && (c && (a = a.replace(c, "")), d && (a = a.replace(d, "")));
            return 1 * a
        };
        g.extend(B.type.order, {
            "date-pre": function(a) {
                return Date.parse(a) || 0
            },
            "html-pre": function(a) {
                return J(a) ? "": a.replace ? a.replace(/<.*?>/g, "").toLowerCase() : a + ""
            },
            "string-pre": function(a) {
                return J(a) ? "": "string" === typeof a ? a.toLowerCase() : a.toString ? a.toString() : ""
            },
            "string-asc": function(a, b) {
                return a < b ? -1 : a > b ? 1 : 0
            },
            "string-desc": function(a, b) {
                return a < b ? 1 : a > b ? -1 : 0
            }
        });
        cb("");
        g.extend(!0, p.ext.renderer, {
            header: {
                _: function(a, b, c, d) {
                    g(a.nTable).on("order.dt.DT",
                    function(e, f, g, k) {
                        a === f && (e = c.idx, b.removeClass(c.sSortingClass + " " + d.sSortAsc + " " + d.sSortDesc).addClass("asc" == k[e] ? d.sSortAsc: "desc" == k[e] ? d.sSortDesc: c.sSortingClass))
                    })
                },
                jqueryui: function(a, b, c, d) {
                    g("\x3cdiv/\x3e").addClass(d.sSortJUIWrapper).append(b.contents()).append(g("\x3cspan/\x3e").addClass(d.sSortIcon + " " + c.sSortingClassJUI)).appendTo(b);
                    g(a.nTable).on("order.dt.DT",
                    function(e, f, g, k) {
                        a === f && (e = c.idx, b.removeClass(d.sSortAsc + " " + d.sSortDesc).addClass("asc" == k[e] ? d.sSortAsc: "desc" == k[e] ? d.sSortDesc: c.sSortingClass), b.find("span." + d.sSortIcon).removeClass(d.sSortJUIAsc + " " + d.sSortJUIDesc + " " + d.sSortJUI + " " + d.sSortJUIAscAllowed + " " + d.sSortJUIDescAllowed).addClass("asc" == k[e] ? d.sSortJUIAsc: "desc" == k[e] ? d.sSortJUIDesc: c.sSortingClassJUI))
                    })
                }
            }
        });
        p.render = {
            number: function(a, b, c, d) {
                return {
                    display: function(e) {
                        var f = 0 > e ? "-": "";
                        e = Math.abs(parseFloat(e));
                        var g = parseInt(e, 10);
                        e = c ? b + (e - g).toFixed(c).substring(2) : "";
                        return f + (d || "") + g.toString().replace(/\B(?=(\d{3})+(?!\d))/g, a) + e
                    }
                }
            }
        };
        g.extend(p.ext.internal, {
            _fnExternApiFunc: Mb,
            _fnBuildAjax: qa,
            _fnAjaxUpdate: jb,
            _fnAjaxParameters: sb,
            _fnAjaxUpdateDraw: tb,
            _fnAjaxDataSrc: ra,
            _fnAddColumn: Ea,
            _fnColumnOptions: ia,
            _fnAdjustColumnSizing: W,
            _fnVisibleToColumnIndex: ja,
            _fnColumnIndexToVisible: Z,
            _fnVisbleColumns: $,
            _fnGetColumns: Y,
            _fnColumnTypes: Ga,
            _fnApplyColumnDefs: hb,
            _fnHungarianMap: U,
            _fnCamelToHungarian: H,
            _fnLanguageCompat: Da,
            _fnBrowserDetect: fb,
            _fnAddData: K,
            _fnAddTr: ka,
            _fnNodeToDataIndex: function(a, b) {
                return void 0 !== b._DT_RowIndex ? b._DT_RowIndex: null
            },
            _fnNodeToColumnIndex: function(a, b, c) {
                return g.inArray(c, a.aoData[b].anCells)
            },
            _fnGetCellData: A,
            _fnSetCellData: Ha,
            _fnSplitObjNotation: Ja,
            _fnGetObjectDataFn: V,
            _fnSetObjectDataFn: P,
            _fnGetDataMaster: Ka,
            _fnClearTable: ma,
            _fnDeleteIndex: na,
            _fnInvalidate: ba,
            _fnGetRowElements: la,
            _fnCreateTr: Ia,
            _fnBuildHead: ib,
            _fnDrawHead: da,
            _fnDraw: M,
            _fnReDraw: N,
            _fnAddOptionsHtml: lb,
            _fnDetectHeader: ca,
            _fnGetUniqueThs: pa,
            _fnFeatureHtmlFilter: nb,
            _fnFilterComplete: ea,
            _fnFilterCustom: wb,
            _fnFilterColumn: vb,
            _fnFilter: ub,
            _fnFilterCreateSearch: Pa,
            _fnEscapeRegex: ua,
            _fnFilterData: xb,
            _fnFeatureHtmlInfo: qb,
            _fnUpdateInfo: Ab,
            _fnInfoMacros: Bb,
            _fnInitialise: fa,
            _fnInitComplete: sa,
            _fnLengthChange: Qa,
            _fnFeatureHtmlLength: mb,
            _fnFeatureHtmlPaginate: rb,
            _fnPageChange: Sa,
            _fnFeatureHtmlProcessing: ob,
            _fnProcessingDisplay: E,
            _fnFeatureHtmlTable: pb,
            _fnScrollDraw: X,
            _fnApplyToChildren: I,
            _fnCalculateColumnWidths: Fa,
            _fnThrottle: ta,
            _fnConvertToWidth: Cb,
            _fnScrollingWidthAdjust: Eb,
            _fnGetWidestNode: Db,
            _fnGetMaxLenString: Fb,
            _fnStringToCss: w,
            _fnScrollBarWidth: Gb,
            _fnSortFlatten: S,
            _fnSort: kb,
            _fnSortAria: Ib,
            _fnSortListener: Ta,
            _fnSortAttachListener: Na,
            _fnSortingClasses: wa,
            _fnSortData: Hb,
            _fnSaveState: xa,
            _fnLoadState: Jb,
            _fnSettingsFromNode: ya,
            _fnLog: Q,
            _fnMap: G,
            _fnBindAction: Ua,
            _fnCallbackReg: z,
            _fnCallbackFire: x,
            _fnLengthOverflow: Ra,
            _fnRenderer: Oa,
            _fnDataSource: D,
            _fnRowAttributes: La,
            _fnCalculateEnd: function() {}
        });
        g.fn.hrdataTable = p;
        g.fn.hrdataTableSettings = p.settings;
        g.fn.hrdataTableExt = p.ext;
        g.fn.hrDataTable = function(a) {
            return g(this).hrdataTable(a).api()
        };
        g.each(p,
        function(a, b) {
            g.fn.hrDataTable[a] = b
        });
        return g.fn.hrdataTable
    })
})(window, document); (function(d, f) {
    "function" === typeof define && define.amd ? define(["jquery"],
    function(h) {
        return d.returnExportsGlobal = f(h)
    }) : "object" === typeof exports ? module.exports = f(require("jquery")) : f(jQuery)
})(this,
function(d) {
    var f, h, r, p, k, e, g, q;
    f = function() {
        function c(a) {
            this.$inputor = a;
            this.domInputor = this.$inputor[0]
        }
        c.prototype.setPos = function(a) {
            var b, c, m, d;
            if (d = g.getSelection()) m = 0,
            c = !1,
            (b = function(a, g) {
                var f, l, h, k, n;
                k = g.childNodes;
                n = [];
                l = 0;
                for (h = k.length; l < h; l++) {
                    f = k[l];
                    if (c) break;
                    if (3 === f.nodeType) if (m + f.length >= a) {
                        c = !0;
                        l = e.createRange();
                        l.setStart(f, a - m);
                        d.removeAllRanges();
                        d.addRange(l);
                        break
                    } else n.push(m += f.length);
                    else n.push(b(a, f))
                }
                return n
            })(a, this.domInputor);
            return this.domInputor
        };
        c.prototype.getIEPosition = function() {
            return this.getPosition()
        };
        c.prototype.getPosition = function() {
            var a, b;
            b = this.getOffset();
            a = this.$inputor.offset();
            b.left -= a.left;
            b.top -= a.top;
            return b
        };
        c.prototype.getOldIEPos = function() {
            var a, b;
            b = e.selection.createRange();
            a = e.body.createTextRange();
            a.moveToElementText(this.domInputor);
            a.setEndPoint("EndToEnd", b);
            return a.text.length
        };
        c.prototype.getPos = function() {
            var a, b;
            if (b = this.range()) return a = b.cloneRange(),
            a.selectNodeContents(this.domInputor),
            a.setEnd(b.endContainer, b.endOffset),
            b = a.toString().length,
            a.detach(),
            b;
            if (e.selection) return this.getOldIEPos()
        };
        c.prototype.getOldIEOffset = function() {
            var a;
            a = e.selection.createRange().duplicate();
            a.moveStart("character", -1);
            a = a.getBoundingClientRect();
            return {
                height: a.bottom - a.top,
                left: a.left,
                top: a.top
            }
        };
        c.prototype.getOffset = function(a) {
            var b, c;
            g.getSelection && (c = this.range()) ? (0 < c.endOffset - 1 && c.endContainer !== this.domInputor && (a = c.cloneRange(), a.setStart(c.endContainer, c.endOffset - 1), a.setEnd(c.endContainer, c.endOffset), b = a.getBoundingClientRect(), b = {
                height: b.height,
                left: b.left + b.width,
                top: b.top
            },
            a.detach()), b && 0 !== (null != b ? b.height: void 0) || (a = c.cloneRange(), c = d(e.createTextNode("|")), a.insertNode(c[0]), a.selectNode(c[0]), b = a.getBoundingClientRect(), b = {
                height: b.height,
                left: b.left,
                top: b.top
            },
            c.remove(), a.detach())) : e.selection && (b = this.getOldIEOffset());
            b && (b.top += d(g).scrollTop(), b.left += d(g).scrollLeft());
            return b
        };
        c.prototype.range = function() {
            var a;
            if (g.getSelection) return a = g.getSelection(),
            0 < a.rangeCount ? a.getRangeAt(0) : null
        };
        return c
    } ();
    h = function() {
        function c(a) {
            this.$inputor = a;
            this.domInputor = this.$inputor[0]
        }
        c.prototype.getIEPos = function() {
            var a, b, c, d;
            a = this.domInputor;
            c = e.selection.createRange();
            b = 0;
            c && c.parentElement() === a && (b = a.value.replace(/\r\n/g, "\n"), b = b.length, d = a.createTextRange(), d.moveToBookmark(c.getBookmark()), a = a.createTextRange(), a.collapse(!1), b = -1 < d.compareEndPoints("StartToEnd", a) ? b: -d.moveStart("character", -b));
            return b
        };
        c.prototype.getPos = function() {
            return e.selection ? this.getIEPos() : this.domInputor.selectionStart
        };
        c.prototype.setPos = function(a) {
            var b, c;
            b = this.domInputor;
            e.selection ? (c = b.createTextRange(), c.move("character", a), c.select()) : b.setSelectionRange && b.setSelectionRange(a, a);
            return b
        };
        c.prototype.getIEOffset = function(a) {
            var b;
            b = this.domInputor.createTextRange();
            a || (a = this.getPos());
            b.move("character", a);
            return {
                left: b.boundingLeft,
                top: b.boundingTop,
                height: b.boundingHeight
            }
        };
        c.prototype.getOffset = function(a) {
            var b, c;
            b = this.$inputor;
            if (e.selection) return c = this.getIEOffset(a),
            c.top += d(g).scrollTop() + b.scrollTop(),
            c.left += d(g).scrollLeft() + b.scrollLeft(),
            c;
            c = b.offset();
            a = this.getPosition(a);
            return {
                left: c.left + a.left - b.scrollLeft(),
                top: c.top + a.top - b.scrollTop(),
                height: a.height
            }
        };
        c.prototype.getPosition = function(a) {
            var b, c, d;
            b = this.$inputor;
            c = function(a) {
                a = a.replace(/<|>|`|"|&/g, "?").replace(/\r\n|\r|\n/g, "\x3cbr/\x3e");
                /firefox/i.test(navigator.userAgent) && (a = a.replace(/\s/g, "\x26nbsp;"));
                return a
            };
            void 0 === a && (a = this.getPos());
            d = b.val().slice(0, a);
            a = b.val().slice(a);
            d = "\x3cspan style\x3d'position: relative; display: inline;'\x3e" + c(d) + "\x3c/span\x3e";
            d = d + "\x3cspan id\x3d'caret' style\x3d'position: relative; display: inline;'\x3e|\x3c/span\x3e" + ("\x3cspan style\x3d'position: relative; display: inline;'\x3e" + c(a) + "\x3c/span\x3e");
            return (new r(b)).create(d).rect()
        };
        c.prototype.getIEPosition = function(a) {
            var b;
            b = this.getIEOffset(a);
            a = this.$inputor.offset();
            return {
                left: b.left - a.left,
                top: b.top - a.top,
                height: b.height
            }
        };
        return c
    } ();
    r = function() {
        function c(a) {
            this.$inputor = a
        }
        c.prototype.css_attr = "borderBottomWidth borderLeftWidth borderRightWidth borderTopStyle borderRightStyle borderBottomStyle borderLeftStyle borderTopWidth boxSizing fontFamily fontSize fontWeight height letterSpacing lineHeight marginBottom marginLeft marginRight marginTop outlineWidth overflow overflowX overflowY paddingBottom paddingLeft paddingRight paddingTop textAlign textOverflow textTransform whiteSpace wordBreak wordWrap".split(" ");
        c.prototype.mirrorCss = function() {
            var a, b = this;
            a = {
                position: "absolute",
                left: -9999,
                top: 0,
                zIndex: -2E4
            };
            "TEXTAREA" === this.$inputor.prop("tagName") && this.css_attr.push("width");
            d.each(this.css_attr,
            function(c, d) {
                return a[d] = b.$inputor.css(d)
            });
            return a
        };
        c.prototype.create = function(a) {
            this.$mirror = d("\x3cdiv\x3e\x3c/div\x3e");
            this.$mirror.css(this.mirrorCss());
            this.$mirror.html(a);
            this.$inputor.after(this.$mirror);
            return this
        };
        c.prototype.rect = function() {
            var a, b;
            a = this.$mirror.find("#caret");
            b = a.position();
            a = {
                left: b.left,
                top: b.top,
                height: a.height()
            };
            this.$mirror.remove();
            return a
        };
        return c
    } ();
    p = {
        contentEditable: function(c) {
            return ! (!c[0].contentEditable || "true" !== c[0].contentEditable)
        }
    };
    k = {
        pos: function(c) {
            return c || 0 === c ? this.setPos(c) : this.getPos()
        },
        position: function(c) {
            return e.selection ? this.getIEPosition(c) : this.getPosition(c)
        },
        offset: function(c) {
            return this.getOffset(c)
        }
    };
    g = e = null;
    q = function(c) {
        if (c = null != c ? c.iframe: void 0) return g = c.contentWindow,
        e = c.contentDocument || g.document;
        g = window;
        return e = document
    };
    d.fn.caret = function(c, a, b) {
        return k[c] ? (d.isPlainObject(a) ? (q(a), a = void 0) : q(b), b = p.contentEditable(this) ? new f(this) : new h(this), k[c].apply(b, [a])) : d.error("Method " + c + " does not exist on jQuery.caret")
    };
    d.fn.caret.EditableCaret = f;
    d.fn.caret.InputCaret = h;
    d.fn.caret.Utils = p;
    d.fn.caret.apis = k
}); +
function(b) {
    function h() {
        var b = document.createElement("bootstrap"),
        g = {
            WebkitTransition: "webkitTransitionEnd",
            MozTransition: "transitionend",
            OTransition: "oTransitionEnd otransitionend",
            transition: "transitionend"
        },
        a;
        for (a in g) if (void 0 !== b.style[a]) return {
            end: g[a]
        };
        return ! 1
    }
    b.fn.emulateTransitionEnd = function(c) {
        var g = !1,
        a = this;
        b(this).one("bsTransitionEnd",
        function() {
            g = !0
        });
        setTimeout(function() {
            g || b(a).trigger(b.support.transition.end)
        },
        c);
        return this
    };
    b(function() {
        b.support.transition = h();
        b.support.transition && (b.event.special.bsTransitionEnd = {
            bindType: b.support.transition.end,
            delegateType: b.support.transition.end,
            handle: function(c) {
                if (b(c.target).is(this)) return c.handleObj.handler.apply(this, arguments)
            }
        })
    })
} (jQuery); +
function(b) {
    function h(a, e) {
        return this.each(function() {
            var d = b(this),
            f = d.data("bs.modal"),
            k = b.extend({},
            c.DEFAULTS, d.data(), "object" == typeof a && a);
            f || d.data("bs.modal", f = new c(this, k));
            if ("string" == typeof a) f[a](e);
            else k.show && f.show(e)
        })
    }
    var c = function(a, e) {
        this.options = e;
        this.$body = b(document.body);
        this.$element = b(a);
        this.$backdrop = this.isShown = null;
        this.scrollbarWidth = 0;
        this.options.remote && this.$element.find(".modal-content").load(this.options.remote, b.proxy(function() {
            this.$element.trigger("loaded.bs.modal")
        },
        this))
    };
    c.VERSION = "3.2.0";
    c.DEFAULTS = {
        backdrop: !0,
        keyboard: !0,
        show: !0
    };
    c.prototype.toggle = function(a) {
        return this.isShown ? this.hide() : this.show(a)
    };
    c.prototype.show = function(a) {
        var e = this,
        c = b.Event("show.bs.modal", {
            relatedTarget: a
        });
        this.$element.trigger(c);
        this.isShown || c.isDefaultPrevented() || (this.isShown = !0, this.checkScrollbar(), this.$body.addClass("modal-open"), this.setScrollbar(), this.escape(), this.$element.on("click.dismiss.bs.modal", '[data-dismiss\x3d"modal"]', b.proxy(this.hide, this)), this.backdrop(function() {
            var c = b.support.transition && e.$element.hasClass("fade");
            e.$element.parent().length || e.$element.appendTo(e.$body);
            e.$element.show().scrollTop(0);
            c && e.$element[0].offsetWidth;
            e.$element.addClass("in").attr("aria-hidden", !1);
            e.enforceFocus();
            var d = b.Event("shown.bs.modal", {
                relatedTarget: a
            });
            c ? e.$element.find(".modal-dialog").one("bsTransitionEnd",
            function() {
                e.$element.trigger("focus").trigger(d)
            }).emulateTransitionEnd(300) : e.$element.trigger("focus").trigger(d)
        }))
    };
    c.prototype.hide = function(a) {
        a && a.preventDefault();
        a = b.Event("hide.bs.modal");
        this.$element.trigger(a);
        this.isShown && !a.isDefaultPrevented() && (this.isShown = !1, this.$body.removeClass("modal-open"), this.resetScrollbar(), this.escape(), b(document).off("focusin.bs.modal"), this.$element.removeClass("in").attr("aria-hidden", !0).off("click.dismiss.bs.modal"), b.support.transition && this.$element.hasClass("fade") ? this.$element.one("bsTransitionEnd", b.proxy(this.hideModal, this)).emulateTransitionEnd(300) : this.hideModal())
    };
    c.prototype.enforceFocus = function() {
        b(document).off("focusin.bs.modal").on("focusin.bs.modal", b.proxy(function(a) {
            this.$element[0] === a.target || this.$element.has(a.target).length || this.$element.trigger("focus")
        },
        this))
    };
    c.prototype.escape = function() {
        if (this.isShown && this.options.keyboard) this.$element.on("keyup.dismiss.bs.modal", b.proxy(function(a) {
            27 == a.which && this.hide()
        },
        this));
        else this.isShown || this.$element.off("keyup.dismiss.bs.modal")
    };
    c.prototype.hideModal = function() {
        var a = this;
        this.$element.hide();
        this.backdrop(function() {
            a.$element.trigger("hidden.bs.modal")
        })
    };
    c.prototype.removeBackdrop = function() {
        this.$backdrop && this.$backdrop.remove();
        this.$backdrop = null
    };
    c.prototype.backdrop = function(a) {
        var c = this,
        d = this.$element.hasClass("fade") ? "fade": "";
        if (this.isShown && this.options.backdrop) {
            var f = b.support.transition && d;
            this.$backdrop = b('\x3cdiv class\x3d"modal-backdrop ' + d + '" /\x3e').appendTo(this.$body);
            this.$element.on("click.dismiss.bs.modal", b.proxy(function(a) {
                a.target === a.currentTarget && ("static" == this.options.backdrop ? this.$element[0].focus.call(this.$element[0]) : this.hide.call(this))
            },
            this));
            f && this.$backdrop[0].offsetWidth;
            this.$backdrop.addClass("in");
            a && (f ? this.$backdrop.one("bsTransitionEnd", a).emulateTransitionEnd(150) : a())
        } else ! this.isShown && this.$backdrop ? (this.$backdrop.removeClass("in"), d = function() {
            c.removeBackdrop();
            a && a()
        },
        b.support.transition && this.$element.hasClass("fade") ? this.$backdrop.one("bsTransitionEnd", d).emulateTransitionEnd(150) : d()) : a && a()
    };
    c.prototype.checkScrollbar = function() {
        document.body.clientWidth >= window.innerWidth || (this.scrollbarWidth = this.scrollbarWidth || this.measureScrollbar())
    };
    c.prototype.setScrollbar = function() {
        var a = parseInt(this.$body.css("padding-right") || 0, 10);
        this.scrollbarWidth && this.$body.css("padding-right", a + this.scrollbarWidth)
    };
    c.prototype.resetScrollbar = function() {
        this.$body.css("padding-right", "")
    };
    c.prototype.measureScrollbar = function() {
        var a = document.createElement("div");
        a.className = "modal-scrollbar-measure";
        this.$body.append(a);
        var b = a.offsetWidth - a.clientWidth;
        this.$body[0].removeChild(a);
        return b
    };
    var g = b.fn.modal;
    b.fn.modal = h;
    b.fn.modal.Constructor = c;
    b.fn.modal.noConflict = function() {
        b.fn.modal = g;
        return this
    };
    b(document).on("click.bs.modal.data-api", '[data-toggle\x3d"modal"]',
    function(a) {
        var c = b(this),
        d = c.attr("href"),
        f = b(c.attr("data-target") || d && d.replace(/.*(?=#[^\s]+$)/, "")),
        d = f.data("bs.modal") ? "toggle": b.extend({
            remote: !/#/.test(d) && d
        },
        f.data(), c.data());
        c.is("a") && a.preventDefault();
        f.one("show.bs.modal",
        function(a) {
            if (!a.isDefaultPrevented()) f.one("hidden.bs.modal",
            function() {
                c.is(":visible") && c.trigger("focus")
            })
        });
        h.call(f, d, this)
    })
} (jQuery); (function(b) {
    b.Placeholders = {
        Utils: {
            addEventListener: function(d, l, e) {
                if (d.addEventListener) return d.addEventListener(l, e, !1);
                if (d.attachEvent) return d.attachEvent("on" + l, e)
            },
            inArray: function(d, l) {
                var e, b;
                e = 0;
                for (b = d.length; e < b; e++) if (d[e] === l) return ! 0;
                return ! 1
            },
            moveCaret: function(d, b) {
                var e;
                d.createTextRange ? (e = d.createTextRange(), e.move("character", b), e.select()) : d.selectionStart && (d.focus(), d.setSelectionRange(b, b))
            },
            changeType: function(b, l) {
                try {
                    return b.type = l,
                    !0
                } catch(e) {
                    return ! 1
                }
            }
        }
    }
})(this); (function(b) {
    function d() {}
    function l(a) {
        var b;
        if (a.value === a.getAttribute("data-placeholder-value") && "true" === a.getAttribute("data-placeholder-active")) {
            a.setAttribute("data-placeholder-active", "false");
            a.value = "";
            a.className = a.className.replace(v, "");
            if (b = a.getAttribute("data-placeholder-type")) a.type = b;
            return ! 0
        }
        return ! 1
    }
    function e(a) {
        var b;
        b = a.getAttribute("data-placeholder-value");
        return "" === a.value && b ? (a.setAttribute("data-placeholder-active", "true"), a.value = b, a.className += " ", (b = a.getAttribute("data-placeholder-type")) ? a.type = "text": "password" === a.type && g.changeType(a, "text") && a.setAttribute("data-placeholder-type", "password"), !0) : !1
    }
    function u(a, b) {
        var c, d, e, g, f;
        if (a && a.getAttribute("data-placeholder-value")) b(a);
        else for (c = a ? a.getElementsByTagName("input") : h, d = a ? a.getElementsByTagName("textarea") : n, f = 0, g = c.length + d.length; f < g; f++) e = f < c.length ? c[f] : d[f - c.length],
        b(e)
    }
    function w(a) {
        u(a, l)
    }
    function C(a) {
        u(a, e)
    }
    function D(a) {
        return function() {
            s && a.value === a.getAttribute("data-placeholder-value") && "true" === a.getAttribute("data-placeholder-active") ? g.moveCaret(a, 0) : l(a)
        }
    }
    function E(a) {
        return function() {
            e(a)
        }
    }
    function F(a) {
        return function(b) {
            t = a.value;
            if ("true" === a.getAttribute("data-placeholder-active") && t === a.getAttribute("data-placeholder-value") && g.inArray(G, b.keyCode)) return b.preventDefault && b.preventDefault(),
            !1
        }
    }
    function H(a) {
        return function() {
            var b;
            "true" === a.getAttribute("data-placeholder-active") && a.value !== t && (a.className = a.className.replace(v, ""), a.value = a.value.replace(a.getAttribute("data-placeholder-value"), ""), a.setAttribute("data-placeholder-active", !1), b = a.getAttribute("data-placeholder-type")) && (a.type = b);
            "" === a.value && (a.blur(), g.moveCaret(a, 0))
        }
    }
    function I(a) {
        return function() {
            a === document.activeElement && a.value === a.getAttribute("data-placeholder-value") && "true" === a.getAttribute("data-placeholder-active") && g.moveCaret(a, 0)
        }
    }
    function J(a) {
        return function() {
            w(a)
        }
    }
    function x(a) {
        a.form && (p = a.form, p.getAttribute("data-placeholder-submit") || (g.addEventListener(p, "submit", J(p)), p.setAttribute("data-placeholder-submit", "true")));
        g.addEventListener(a, "focus", D(a));
        g.addEventListener(a, "blur", E(a));
        s && (g.addEventListener(a, "keydown", F(a)), g.addEventListener(a, "keyup", H(a)), g.addEventListener(a, "click", I(a)));
        a.setAttribute("data-placeholder-bound", "true");
        a.setAttribute("data-placeholder-value", k);
        e(a)
    }
    var y = "text search url tel email password number textarea".split(" "),
    G = [27, 33, 34, 35, 36, 37, 38, 39, 40, 8, 46],
    v = /(?:^|\s)(?!\S)/,
    h,
    n,
    m = document.createElement("input"),
    z = document.getElementsByTagName("head")[0],
    q = document.documentElement;
    b = b.Placeholders;
    var g = b.Utils,
    s, A, t, k, B, p, c, r, f;
    b.nativeSupport = void 0 !== m.placeholder;
    if (!b.nativeSupport) {
        h = document.getElementsByTagName("input");
        n = document.getElementsByTagName("textarea");
        s = "false" === q.getAttribute("data-placeholder-focus");
        A = "false" !== q.getAttribute("data-placeholder-live");
        m = document.createElement("style");
        m.type = "text/css";
        q = document.createTextNode(". { color:; }");
        m.styleSheet ? m.styleSheet.cssText = q.nodeValue: m.appendChild(q);
        z.insertBefore(m, z.firstChild);
        f = 0;
        for (r = h.length + n.length; f < r; f++) c = f < h.length ? h[f] : n[f - h.length],
        (k = c.attributes.placeholder) && (k = k.nodeValue) && g.inArray(y, c.type) && x(c);
        B = setInterval(function() {
            f = 0;
            for (r = h.length + n.length; f < r; f++) if (c = f < h.length ? h[f] : n[f - h.length], (k = c.attributes.placeholder) && (k = k.nodeValue) && g.inArray(y, c.type) && (c.getAttribute("data-placeholder-bound") || x(c), k !== c.getAttribute("data-placeholder-value") || "password" === c.type && !c.getAttribute("data-placeholder-type")))"password" === c.type && !c.getAttribute("data-placeholder-type") && g.changeType(c, "text") && c.setAttribute("data-placeholder-type", "password"),
            c.value === c.getAttribute("data-placeholder-value") && (c.value = k),
            c.setAttribute("data-placeholder-value", k);
            A || clearInterval(B)
        },
        100)
    }
    b.disable = b.nativeSupport ? d: w;
    b.enable = b.nativeSupport ? d: C
})(this); !
function(e) {
    function r() {
        return new Date(Date.UTC.apply(Date, arguments))
    }
    var u = function(a, b) {
        var c = this;
        this.element = e(a);
        this.language = b.language || this.element.data("date-language") || "en";
        this.language = this.language in f ? this.language: "en";
        this.isRTL = f[this.language].rtl || !1;
        this.formatType = b.formatType || this.element.data("format-type") || "standard";
        this.format = g.parseFormat(b.format || this.element.data("date-format") || f[this.language].format || g.getDefaultFormat(this.formatType, "input"), this.formatType);
        this.isVisible = this.isInline = !1;
        this.isInput = this.element.is("input");
        this.component = this.element.is(".date") ? this.element.find(".add-on .icon-th, .add-on .icon-time, .add-on .icon-calendar").parent() : !1;
        this.componentReset = this.element.is(".date") ? this.element.find(".add-on .icon-remove").parent() : !1;
        this.hasInput = this.component && this.element.find("input").length;
        this.component && 0 === this.component.length && (this.component = !1);
        this.linkField = b.linkField || this.element.data("link-field") || !1;
        this.linkFormat = g.parseFormat(b.linkFormat || this.element.data("link-format") || g.getDefaultFormat(this.formatType, "link"), this.formatType);
        this.minuteStep = b.minuteStep || this.element.data("minute-step") || 5;
        this.pickerPosition = b.pickerPosition || this.element.data("picker-position") || "bottom-right";
        this.showMeridian = b.showMeridian || this.element.data("show-meridian") || !1;
        this.initialDate = b.initialDate || new Date;
        this.writeValue = b.writeValue || !1;
        this._attachEvents();
        this.formatViewType = "datetime";
        "formatViewType" in b ? this.formatViewType = b.formatViewType: "formatViewType" in this.element.data() && (this.formatViewType = this.element.data("formatViewType"));
        this.minView = 0;
        "minView" in b ? this.minView = b.minView: "minView" in this.element.data() && (this.minView = this.element.data("min-view"));
        this.minView = g.convertViewMode(this.minView);
        this.maxView = g.modes.length - 1;
        "maxView" in b ? this.maxView = b.maxView: "maxView" in this.element.data() && (this.maxView = this.element.data("max-view"));
        this.maxView = g.convertViewMode(this.maxView);
        this.wheelViewModeNavigation = !1;
        "wheelViewModeNavigation" in b ? this.wheelViewModeNavigation = b.wheelViewModeNavigation: "wheelViewModeNavigation" in this.element.data() && (this.wheelViewModeNavigation = this.element.data("view-mode-wheel-navigation"));
        this.wheelViewModeNavigationInverseDirection = !1;
        "wheelViewModeNavigationInverseDirection" in b ? this.wheelViewModeNavigationInverseDirection = b.wheelViewModeNavigationInverseDirection: "wheelViewModeNavigationInverseDirection" in this.element.data() && (this.wheelViewModeNavigationInverseDirection = this.element.data("view-mode-wheel-navigation-inverse-dir"));
        this.wheelViewModeNavigationDelay = 100;
        "wheelViewModeNavigationDelay" in b ? this.wheelViewModeNavigationDelay = b.wheelViewModeNavigationDelay: "wheelViewModeNavigationDelay" in this.element.data() && (this.wheelViewModeNavigationDelay = this.element.data("view-mode-wheel-navigation-delay"));
        this.startViewMode = 2;
        "startView" in b ? this.startViewMode = b.startView: "startView" in this.element.data() && (this.startViewMode = this.element.data("start-view"));
        this.viewMode = this.startViewMode = g.convertViewMode(this.startViewMode);
        this.viewSelect = this.minView;
        "viewSelect" in b ? this.viewSelect = b.viewSelect: "viewSelect" in this.element.data() && (this.viewSelect = this.element.data("view-select"));
        this.viewSelect = g.convertViewMode(this.viewSelect);
        this.forceParse = !0;
        "forceParse" in b ? this.forceParse = b.forceParse: "dateForceParse" in this.element.data() && (this.forceParse = this.element.data("date-force-parse"));
        this.picker = e(g.template).appendTo(this.isInline ? this.element: "body").on({
            click: e.proxy(this.click, this),
            mousedown: e.proxy(this.mousedown, this)
        });
        if (this.wheelViewModeNavigation) if (e.fn.mousewheel) this.picker.on({
            mousewheel: e.proxy(this.mousewheel, this)
        });
        else console.log("Mouse Wheel event is not supported. Please include the jQuery Mouse Wheel plugin before enabling this option");
        this.isInline ? this.picker.addClass("datetimepicker-inline") : this.picker.addClass("datetimepicker-dropdown-" + this.pickerPosition + " dropdown-menu");
        this.isRTL && (this.picker.addClass("datetimepicker-rtl"), this.picker.find(".prev i, .next i").toggleClass("icon-arrow-left icon-arrow-right"));
        e(document).on("mousedown",
        function(a) {
            0 === e(a.target).closest(".datetimepicker").length && c.hide()
        });
        this.autoclose = !1;
        "autoclose" in b ? this.autoclose = b.autoclose: "dateAutoclose" in this.element.data() && (this.autoclose = this.element.data("date-autoclose"));
        this.keyboardNavigation = !0;
        "keyboardNavigation" in b ? this.keyboardNavigation = b.keyboardNavigation: "dateKeyboardNavigation" in this.element.data() && (this.keyboardNavigation = this.element.data("date-keyboard-navigation"));
        this.todayBtn = b.todayBtn || this.element.data("date-today-btn") || !1;
        this.todayHighlight = b.todayHighlight || this.element.data("date-today-highlight") || !1;
        this.weekStart = (b.weekStart || this.element.data("date-weekstart") || f[this.language].weekStart || 0) % 7;
        this.weekEnd = (this.weekStart + 6) % 7;
        this.startDate = -Infinity;
        this.endDate = Infinity;
        this.daysOfWeekDisabled = [];
        this.setStartDate(b.startDate || this.element.data("date-startdate"));
        this.setEndDate(b.endDate || this.element.data("date-enddate"));
        this.setDaysOfWeekDisabled(b.daysOfWeekDisabled || this.element.data("date-days-of-week-disabled"));
        this.fillDow();
        this.fillMonths();
        this.update();
        this.showMode();
        this.isInline && this.show()
    };
    u.prototype = {
        constructor: u,
        _events: [],
        _attachEvents: function() {
            this._detachEvents();
            this.isInput ? this._events = [[this.element, {
                focus: e.proxy(this.show, this),
                keyup: e.proxy(this.update, this),
                keydown: e.proxy(this.keydown, this)
            }]] : this.component && this.hasInput ? (this._events = [[this.element.find("input"), {
                focus: e.proxy(this.show, this),
                keyup: e.proxy(this.update, this),
                keydown: e.proxy(this.keydown, this)
            }], [this.component, {
                click: e.proxy(this.show, this)
            }]], this.componentReset && this._events.push([this.componentReset, {
                click: e.proxy(this.reset, this)
            }])) : this.element.is("div") || this.element.hasClass("inline") ? this.isInline = !0 : this._events = [[this.element, {
                click: e.proxy(this.show, this)
            }]];
            for (var a = 0,
            b, c; a < this._events.length; a++) b = this._events[a][0],
            c = this._events[a][1],
            b.on(c)
        },
        _detachEvents: function() {
            for (var a = 0,
            b, c; a < this._events.length; a++) b = this._events[a][0],
            c = this._events[a][1],
            b.off(c);
            this._events = []
        },
        show: function(a) {
            this.picker.show();
            this.height = this.component ? this.component.outerHeight() : this.element.outerHeight();
            this.forceParse && this.update();
            this.place();
            e(window).on("resize", e.proxy(this.place, this));
            a && (a.stopPropagation(), a.preventDefault());
            this.isVisible = !0;
            this.element.trigger({
                type: "show",
                date: this.date
            })
        },
        hide: function(a) {
            this.isVisible && !this.isInline && (this.picker.hide(), e(window).off("resize", this.place), this.viewMode = this.startViewMode, this.showMode(), this.isInput || e(document).off("mousedown", this.hide), this.forceParse && (this.isInput && this.element.val() || this.hasInput && this.element.find("input").val()) && this.setValue(), this.isVisible = !1, this.element.trigger({
                type: "hide",
                date: this.date
            }))
        },
        remove: function() {
            this._detachEvents();
            this.picker.remove();
            delete this.picker;
            delete this.element.data().datetimepicker
        },
        getDate: function() {
            var a = this.getUTCDate();
            return new Date(a.getTime() + 6E4 * a.getTimezoneOffset())
        },
        getUTCDate: function() {
            return this.date
        },
        setDate: function(a) {
            this.setUTCDate(new Date(a.getTime() - 6E4 * a.getTimezoneOffset()))
        },
        setUTCDate: function(a) {
            a >= this.startDate && a <= this.endDate ? (this.date = a, this.setValue(), this.viewDate = this.date, this.fill()) : this.element.trigger({
                type: "outOfRange",
                date: a,
                startDate: this.startDate,
                endDate: this.endDate
            })
        },
        setFormat: function(a) {
            this.format = g.parseFormat(a, this.formatType);
            var b;
            this.isInput ? b = this.element: this.component && (b = this.element.find("input"));
            b && b.val() && this.setValue()
        },
        setValue: function() {
            var a = this.getFormattedDate();
            this.isInput ? this.writeValue || this.element.val(a) : this.component && !this.writeValue && this.element.find("input").val(a);
            this.element.data("date", a);
            this.linkField && e("#" + this.linkField).val(this.getFormattedDate(this.linkFormat))
        },
        getFormattedDate: function(a) {
            void 0 == a && (a = this.format);
            return g.formatDate(this.date, a, this.language, this.formatType)
        },
        setStartDate: function(a) {
            this.startDate = a || -Infinity; - Infinity !== this.startDate && (this.startDate = g.parseDate(this.startDate, this.format, this.language, this.formatType));
            this.update();
            this.updateNavArrows()
        },
        setEndDate: function(a) {
            this.endDate = a || Infinity;
            Infinity !== this.endDate && (this.endDate = g.parseDate(this.endDate, this.format, this.language, this.formatType));
            this.update();
            this.updateNavArrows()
        },
        setDaysOfWeekDisabled: function(a) {
            this.daysOfWeekDisabled = a || [];
            e.isArray(this.daysOfWeekDisabled) || (this.daysOfWeekDisabled = this.daysOfWeekDisabled.split(/,\s*/));
            this.daysOfWeekDisabled = e.map(this.daysOfWeekDisabled,
            function(a) {
                return parseInt(a, 10)
            });
            this.update();
            this.updateNavArrows()
        },
        place: function() {
            if (!this.isInline) {
                var a = parseInt(this.element.parents().filter(function() {
                    return "auto" != e(this).css("z-index")
                }).first().css("z-index")) + 1051,
                b,
                c,
                d;
                if (this.component) {
                    if (b = this.component.offset(), d = b.left, "bottom-left" == this.pickerPosition || "top-left" == this.pickerPosition) d += this.component.outerWidth() - this.picker.outerWidth()
                } else if (b = this.element.offset(), d = b.left, "bottom-left" == this.pickerPosition || "top-left" == this.pickerPosition) d -= this.picker.outerWidth() - 50;
                if (c = "top-left" == this.pickerPosition || "top-right" == this.pickerPosition ? b.top - this.picker.outerHeight() : b.top + this.height) {
                    var n = "left"; - 1 != this.pickerPosition.indexOf("right") && (n = "right");
                    c + this.picker.outerWidth() > e(window).height() ? (c = b.top - this.picker.outerHeight() - this.height, this.picker.removeClass("datetimepicker-dropdown-" + this.pickerPosition), this.pickerPosition = "top-" + n, this.picker.addClass("datetimepicker-dropdown-" + this.pickerPosition)) : 0 > c && (c = b.top + this.height, this.picker.removeClass("datetimepicker-dropdown-" + this.pickerPosition), this.pickerPosition = "bottom-" + n, this.picker.addClass("datetimepicker-dropdown-" + this.pickerPosition))
                }
                this.picker.css({
                    top: c,
                    left: d,
                    zIndex: a
                })
            }
        },
        update: function() {
            var a, b = !1;
            arguments && arguments.length && ("string" === typeof arguments[0] || arguments[0] instanceof Date) ? (a = arguments[0], b = !0) : a = this.element.data("date") || (this.isInput ? this.element.val() : this.element.find("input").val()) || this.initialDate;
            a || (a = new Date, b = !1);
            this.date = g.parseDate(a, this.format, this.language, this.formatType);
            b && this.setValue();
            this.viewDate = this.date < this.startDate ? new Date(this.startDate) : this.date > this.endDate ? new Date(this.endDate) : new Date(this.date);
            this.fill()
        },
        fillDow: function() {
            for (var a = this.weekStart,
            b = "\x3ctr\x3e"; a < this.weekStart + 7;) b += '\x3cth class\x3d"dow"\x3e' + f[this.language].daysMin[a++%7] + "\x3c/th\x3e";
            b += "\x3c/tr\x3e";
            this.picker.find(".datetimepicker-days thead").append(b)
        },
        fillMonths: function() {
            for (var a = "",
            b = 0; 12 > b;) a += '\x3cspan class\x3d"month"\x3e' + f[this.language].monthsShort[b++] + "\x3c/span\x3e";
            this.picker.find(".datetimepicker-months td").html(a)
        },
        fill: function() {
            if (null != this.date && null != this.viewDate) {
                var a = new Date(this.viewDate),
                b = a.getUTCFullYear(),
                c = a.getUTCMonth(),
                d = a.getUTCDate(),
                n = a.getUTCHours(),
                l = a.getUTCMinutes(),
                a = -Infinity !== this.startDate ? this.startDate.getUTCFullYear() : -Infinity,
                v = -Infinity !== this.startDate ? this.startDate.getUTCMonth() : -Infinity,
                w = Infinity !== this.endDate ? this.endDate.getUTCFullYear() : Infinity,
                s = Infinity !== this.endDate ? this.endDate.getUTCMonth() : Infinity,
                k = (new r(this.date.getUTCFullYear(), this.date.getUTCMonth(), this.date.getUTCDate())).valueOf(),
                t = new Date;
                this.picker.find(".datetimepicker-days thead th:eq(1)").text(f[this.language].months[c] + " " + b);
                if ("time" == this.formatViewType) {
                    var m = n % 12 ? n % 12 : 12,
                    m = (10 > m ? "0": "") + m,
                    p = (10 > l ? "0": "") + l,
                    h = f[this.language].meridiem[12 > n ? 0 : 1];
                    this.picker.find(".datetimepicker-hours thead th:eq(1)").text(m + ":" + p + " " + h.toUpperCase());
                    this.picker.find(".datetimepicker-minutes thead th:eq(1)").text(m + ":" + p + " " + h.toUpperCase())
                } else this.picker.find(".datetimepicker-hours thead th:eq(1)").text(d + " " + f[this.language].months[c] + " " + b),
                this.picker.find(".datetimepicker-minutes thead th:eq(1)").text(d + " " + f[this.language].months[c] + " " + b);
                this.picker.find("tfoot th.dategroup").html('\x3cdiv class\x3d"today"\x3e\u4eca\u5929\x3c/div\x3e\x3cdiv class\x3d"tomorrow"\x3e\u660e\u5929\x3c/div\x3e\x3cdiv class\x3d"upcoming"\x3e\u5373\u5c06\x3c/div\x3e\x3cdiv class\x3d"someday"\x3e\u65e0\u65e5\u671f\x3c/div\x3e').toggle(!1 !== this.todayBtn);
                this.updateNavArrows();
                this.fillMonths();
                h = r(b, c - 1, 28, 0, 0, 0, 0);
                m = g.getDaysInMonth(h.getUTCFullYear(), h.getUTCMonth());
                h.setUTCDate(m);
                h.setUTCDate(m - (h.getUTCDay() - this.weekStart + 7) % 7);
                var q = new Date(h);
                q.setUTCDate(q.getUTCDate() + 42);
                q = q.valueOf();
                for (m = []; h.valueOf() < q;) {
                    h.getUTCDay() == this.weekStart && m.push("\x3ctr\x3e");
                    p = "";
                    if (h.getUTCFullYear() < b || h.getUTCFullYear() == b && h.getUTCMonth() < c) p += " old";
                    else if (h.getUTCFullYear() > b || h.getUTCFullYear() == b && h.getUTCMonth() > c) p += " new";
                    this.todayHighlight && h.getUTCFullYear() == t.getFullYear() && h.getUTCMonth() == t.getMonth() && h.getUTCDate() == t.getDate() && (p += " today");
                    h.valueOf() == k && (p += " active");
                    if (h.valueOf() + 864E5 <= this.startDate || h.valueOf() > this.endDate || -1 !== e.inArray(h.getUTCDay(), this.daysOfWeekDisabled)) p += " disabled";
                    m.push('\x3ctd class\x3d"day' + p + '"\x3e' + h.getUTCDate() + "\x3c/td\x3e");
                    h.getUTCDay() == this.weekEnd && m.push("\x3c/tr\x3e");
                    h.setUTCDate(h.getUTCDate() + 1)
                }
                this.picker.find(".datetimepicker-days tbody").empty().append(m.join(""));
                m = [];
                q = t = h = "";
                for (k = 0; 24 > k; k++) h = r(b, c, d, k),
                p = "",
                q = " halfhour",
                h.valueOf() + 36E5 <= this.startDate || h.valueOf() > this.endDate ? (p += " disabled", q += " disabled") : n == k && (p += " active"),
                !k % 3 && m.push('\x3cfieldset class\x3d"hour"\x3e\x3clegend\x3e' + t.toUpperCase() + "\x3c/legend\x3e"),
                h = k,
                m.push('\x3cspan class\x3d"hour' + p + " hour_" + (12 > k ? "am": "pm") + '" style\x3d"font-size:12px;font-weight: bold;" hour\x3d"' + h + '" minute\x3d"0"\x3e' + h + "\x3c/span\x3e"),
                m.push('\x3cspan class\x3d"hour' + q + " hour_" + (12 > k ? "am": "pm") + '" hour\x3d"' + h + '" minute\x3d"30"\x3e' + h + ":30\x3c/span\x3e"),
                !(k + 1) % 3 && m.push("\x3c/fieldset\x3e");
                this.picker.find(".datetimepicker-hours td").html(m.join(""));
                m = [];
                q = "";
                for (k = 0; 60 > k; k += this.minuteStep) h = r(b, c, d, n, k, 0),
                p = "",
                h.valueOf() < this.startDate || h.valueOf() > this.endDate ? p += " disabled": Math.floor(l / this.minuteStep) == Math.floor(k / this.minuteStep) && (p += " active"),
                this.showMeridian && 2 == f[this.language].meridiem.length ? (t = 12 > n ? f[this.language].meridiem[0] : f[this.language].meridiem[1], t != q && ("" != q && m.push("\x3c/fieldset\x3e"), m.push('\x3cfieldset class\x3d"minute"\x3e\x3clegend\x3e' + t.toUpperCase() + "\x3c/legend\x3e")), q = t, h = n % 12 ? n % 12 : 12, m.push('\x3cspan class\x3d"minute' + p + '"\x3e' + h + ":" + (10 > k ? "0" + k: k) + "\x3c/span\x3e"), 59 == k && m.push("\x3c/fieldset\x3e")) : m.push('\x3cspan class\x3d"minute' + p + '"\x3e' + n + ":" + (10 > k ? "0" + k: k) + "\x3c/span\x3e");
                this.picker.find(".datetimepicker-minutes td").html(m.join(""));
                c = this.date.getUTCFullYear();
                d = this.picker.find(".datetimepicker-months").find("th:eq(1)").text(b).end().find("span").removeClass("active");
                c == b && d.eq(this.date.getUTCMonth()).addClass("active"); (b < a || b > w) && d.addClass("disabled");
                b == a && d.slice(0, v).addClass("disabled");
                b == w && d.slice(s + 1).addClass("disabled");
                m = "";
                b = 10 * parseInt(b / 10, 10);
                v = this.picker.find(".datetimepicker-years").find("th:eq(1)").text(b + "-" + (b + 9)).end().find("td");
                b -= 1;
                for (k = -1; 11 > k; k++) m += '\x3cspan class\x3d"year' + ( - 1 == k || 10 == k ? " old": "") + (c == b ? " active": "") + (b < a || b > w ? " disabled": "") + '"\x3e' + b + "\x3c/span\x3e",
                b += 1;
                v.html(m);
                this.place()
            }
        },
        updateNavArrows: function() {
            var a = new Date(this.viewDate),
            b = a.getUTCFullYear(),
            c = a.getUTCMonth(),
            d = a.getUTCDate(),
            a = a.getUTCHours();
            switch (this.viewMode) {
            case 0:
                -Infinity !== this.startDate && b <= this.startDate.getUTCFullYear() && c <= this.startDate.getUTCMonth() && d <= this.startDate.getUTCDate() && a <= this.startDate.getUTCHours() ? this.picker.find(".prev").css({
                    visibility: "hidden"
                }) : this.picker.find(".prev").css({
                    visibility: "visible"
                });
                Infinity !== this.endDate && b >= this.endDate.getUTCFullYear() && c >= this.endDate.getUTCMonth() && d >= this.endDate.getUTCDate() && a >= this.endDate.getUTCHours() ? this.picker.find(".next").css({
                    visibility: "hidden"
                }) : this.picker.find(".next").css({
                    visibility: "visible"
                });
                break;
            case 1:
                -Infinity !== this.startDate && b <= this.startDate.getUTCFullYear() && c <= this.startDate.getUTCMonth() && d <= this.startDate.getUTCDate() ? this.picker.find(".prev").css({
                    visibility: "hidden"
                }) : this.picker.find(".prev").css({
                    visibility: "visible"
                });
                Infinity !== this.endDate && b >= this.endDate.getUTCFullYear() && c >= this.endDate.getUTCMonth() && d >= this.endDate.getUTCDate() ? this.picker.find(".next").css({
                    visibility: "hidden"
                }) : this.picker.find(".next").css({
                    visibility: "visible"
                });
                break;
            case 2:
                -Infinity !== this.startDate && b <= this.startDate.getUTCFullYear() && c <= this.startDate.getUTCMonth() ? this.picker.find(".prev").css({
                    visibility: "hidden"
                }) : this.picker.find(".prev").css({
                    visibility: "visible"
                });
                Infinity !== this.endDate && b >= this.endDate.getUTCFullYear() && c >= this.endDate.getUTCMonth() ? this.picker.find(".next").css({
                    visibility: "hidden"
                }) : this.picker.find(".next").css({
                    visibility: "visible"
                });
                break;
            case 3:
            case 4:
                -Infinity !== this.startDate && b <= this.startDate.getUTCFullYear() ? this.picker.find(".prev").css({
                    visibility: "hidden"
                }) : this.picker.find(".prev").css({
                    visibility: "visible"
                }),
                Infinity !== this.endDate && b >= this.endDate.getUTCFullYear() ? this.picker.find(".next").css({
                    visibility: "hidden"
                }) : this.picker.find(".next").css({
                    visibility: "visible"
                })
            }
        },
        mousewheel: function(a) {
            a.preventDefault();
            a.stopPropagation();
            this.wheelPause || (this.wheelPause = !0, a = a.originalEvent.wheelDelta, a = 0 < a ? 1 : 0 === a ? 0 : -1, this.wheelViewModeNavigationInverseDirection && (a = -a), this.showMode(a), setTimeout(e.proxy(function() {
                this.wheelPause = !1
            },
            this), this.wheelViewModeNavigationDelay))
        },
        click: function(a) {
            a.stopPropagation();
            a.preventDefault();
            var b = e(a.target).closest("span, td, th, legend");
            if (1 == b.length) if (b.is(".disabled")) this.element.trigger({
                type: "outOfRange",
                date: this.viewDate,
                startDate: this.startDate,
                endDate: this.endDate
            });
            else switch (b[0].nodeName.toLowerCase()) {
            case "th":
                switch (b[0].className) {
                case "switch":
                    this.showMode(1);
                    break;
                case "prev":
                case "next":
                    b = g.modes[this.viewMode].navStep * ("prev" == b[0].className ? -1 : 1);
                    switch (this.viewMode) {
                    case 0:
                        this.viewDate = this.moveHour(this.viewDate, b);
                        break;
                    case 1:
                        this.viewDate = this.moveDate(this.viewDate, b);
                        break;
                    case 2:
                        this.viewDate = this.moveMonth(this.viewDate, b);
                        break;
                    case 3:
                    case 4:
                        this.viewDate = this.moveYear(this.viewDate, b)
                    }
                    this.fill();
                    break;
                case "today":
                    b = new Date;
                    b = r(b.getFullYear(), b.getMonth(), b.getDate(), b.getHours(), b.getMinutes(), b.getSeconds(), 0);
                    this.viewMode = this.startViewMode;
                    this.showMode(0);
                    this._setDate(b);
                    this.fill();
                    this.autoclose && this.hide();
                    break;
                case "dategroup":
                    switch (a = e(a.target).attr("class"), b = Date.create(TEAMS.nowTime), a) {
                    case "today":
                        this.viewMode = this.startViewMode;
                        this.showMode(0);
                        this._setDate(b);
                        this.fill();
                        this.autoclose && this.hide();
                        break;
                    case "tomorrow":
                        b.addDays(1);
                        this.viewMode = this.startViewMode;
                        this.showMode(0);
                        this._setDate(b);
                        this.fill();
                        this.autoclose && this.hide();
                        break;
                    case "upcoming":
                        b.addDays(7);
                        this.viewMode = this.startViewMode;
                        this.showMode(0);
                        this._setDate(b);
                        this.fill();
                        this.autoclose && this.hide();
                        break;
                    case "someday":
                        this.viewMode = this.startViewMode,
                        this.showMode(0),
                        this._setDate(null),
                        this.fill(),
                        this.autoclose && this.hide()
                    }
                }
                break;
            case "span":
                if (!b.is(".disabled")) {
                    a = this.viewDate.getUTCFullYear();
                    var c = this.viewDate.getUTCMonth(),
                    d = this.viewDate.getUTCDate(),
                    n = this.viewDate.getUTCHours(),
                    l = this.viewDate.getUTCMinutes(),
                    f = this.viewDate.getUTCSeconds();
                    b.is(".month") ? (this.viewDate.setUTCDate(1), c = b.parent().find("span").index(b), d = this.viewDate.getUTCDate(), this.viewDate.setUTCMonth(c), this.element.trigger({
                        type: "changeMonth",
                        date: this.viewDate
                    }), 3 <= this.viewSelect && this._setDate(r(a, c, d, n, l, f, 0))) : b.is(".year") ? (this.viewDate.setUTCDate(1), a = parseInt(b.text(), 10) || 0, this.viewDate.setUTCFullYear(a), this.element.trigger({
                        type: "changeYear",
                        date: this.viewDate
                    }), 4 <= this.viewSelect && this._setDate(r(a, c, d, n, l, f, 0))) : b.is(".hour") ? (n = parseInt(b.attr("hour")) || 0, l = parseInt(b.attr("minute")) || 0, this.viewDate.setUTCHours(n), this.element.trigger({
                        type: "changeHour",
                        date: this.viewDate
                    }), this.viewDate.setUTCMinutes(l), this.element.trigger({
                        type: "changeMinute",
                        date: this.viewDate
                    }), this._setDate(r(a, c, d, n, l, f, 0))) : b.is(".minute") && (l = parseInt(b.text().substr(b.text().indexOf(":") + 1), 10) || 0, this.viewDate.setUTCMinutes(l), this.element.trigger({
                        type: "changeMinute",
                        date: this.viewDate
                    }), 0 <= this.viewSelect && this._setDate(r(a, c, d, n, l, f, 0)));
                    0 != this.viewMode ? (b = this.viewMode, this.showMode( - 1), this.fill(), b == this.viewMode && this.autoclose && this.hide()) : (this.fill(), this.autoclose && this.hide())
                }
                break;
            case "td":
                b.is(".day") && !b.is(".disabled") && (d = parseInt(b.text(), 10) || 1, a = this.viewDate.getUTCFullYear(), c = this.viewDate.getUTCMonth(), n = this.viewDate.getUTCHours(), l = this.viewDate.getUTCMinutes(), f = this.viewDate.getUTCSeconds(), b.is(".old") ? 0 === c ? (c = 11, a -= 1) : c -= 1 : b.is(".new") && (11 == c ? (c = 0, a += 1) : c += 1), this.viewDate.setUTCFullYear(a), this.viewDate.setUTCMonth(c), this.viewDate.setUTCDate(d), this.element.trigger({
                    type: "changeDay",
                    date: this.viewDate
                }), 2 <= this.viewSelect && this._setDate(r(a, c, d, n, l, f, 0))),
                b = this.viewMode,
                this.showMode( - 1),
                this.fill(),
                b == this.viewMode && this.autoclose && this.hide()
            }
        },
        _setDate: function(a, b) {
            b && "date" != b || (this.date = a);
            b && "view" != b || (this.viewDate = a);
            this.fill();
            this.setValue();
            var c;
            this.isInput ? c = this.element: this.component && (c = this.element.find("input"));
            c && c.change();
            this.element.trigger({
                type: "changeDate",
                date: this.date
            })
        },
        moveMinute: function(a, b) {
            if (!b) return a;
            var c = new Date(a.valueOf());
            c.setUTCMinutes(c.getUTCMinutes() + b * this.minuteStep);
            return c
        },
        moveHour: function(a, b) {
            if (!b) return a;
            var c = new Date(a.valueOf());
            c.setUTCHours(c.getUTCHours() + b);
            return c
        },
        moveDate: function(a, b) {
            if (!b) return a;
            var c = new Date(a.valueOf());
            c.setUTCDate(c.getUTCDate() + b);
            return c
        },
        moveMonth: function(a, b) {
            if (!b) return a;
            var c = new Date(a.valueOf()),
            d = c.getUTCDate(),
            n = c.getUTCMonth(),
            e = Math.abs(b),
            f;
            b = 0 < b ? 1 : -1;
            if (1 == e) {
                if (e = -1 == b ?
                function() {
                    return c.getUTCMonth() == n
                }: function() {
                    return c.getUTCMonth() != f
                },
                f = n + b, c.setUTCMonth(f), 0 > f || 11 < f) f = (f + 12) % 12
            } else {
                for (var g = 0; g < e; g++) c = this.moveMonth(c, b);
                f = c.getUTCMonth();
                c.setUTCDate(d);
                e = function() {
                    return f != c.getUTCMonth()
                }
            }
            for (; e();) c.setUTCDate(--d),
            c.setUTCMonth(f);
            return c
        },
        moveYear: function(a, b) {
            return this.moveMonth(a, 12 * b)
        },
        dateWithinRange: function(a) {
            return a >= this.startDate && a <= this.endDate
        },
        keydown: function(a) {
            if (this.picker.is(":not(:visible)")) 27 == a.keyCode && this.show();
            else {
                var b = !1,
                c, d, e;
                switch (a.keyCode) {
                case 27:
                    this.hide();
                    a.preventDefault();
                    break;
                case 37:
                case 39:
                    if (!this.keyboardNavigation) break;
                    c = 37 == a.keyCode ? -1 : 1;
                    viewMode = this.viewMode;
                    a.ctrlKey ? viewMode += 2 : a.shiftKey && (viewMode += 1);
                    4 == viewMode ? (d = this.moveYear(this.date, c), e = this.moveYear(this.viewDate, c)) : 3 == viewMode ? (d = this.moveMonth(this.date, c), e = this.moveMonth(this.viewDate, c)) : 2 == viewMode ? (d = this.moveDate(this.date, c), e = this.moveDate(this.viewDate, c)) : 1 == viewMode ? (d = this.moveHour(this.date, c), e = this.moveHour(this.viewDate, c)) : 0 == viewMode && (d = this.moveMinute(this.date, c), e = this.moveMinute(this.viewDate, c));
                    this.dateWithinRange(d) && (this.date = d, this.viewDate = e, this.setValue(), this.update(), a.preventDefault(), b = !0);
                    break;
                case 38:
                case 40:
                    if (!this.keyboardNavigation) break;
                    c = 38 == a.keyCode ? -1 : 1;
                    viewMode = this.viewMode;
                    a.ctrlKey ? viewMode += 2 : a.shiftKey && (viewMode += 1);
                    4 == viewMode ? (d = this.moveYear(this.date, c), e = this.moveYear(this.viewDate, c)) : 3 == viewMode ? (d = this.moveMonth(this.date, c), e = this.moveMonth(this.viewDate, c)) : 2 == viewMode ? (d = this.moveDate(this.date, 7 * c), e = this.moveDate(this.viewDate, 7 * c)) : 1 == viewMode ? this.showMeridian ? (d = this.moveHour(this.date, 6 * c), e = this.moveHour(this.viewDate, 6 * c)) : (d = this.moveHour(this.date, 4 * c), e = this.moveHour(this.viewDate, 4 * c)) : 0 == viewMode && (d = this.moveMinute(this.date, 4 * c), e = this.moveMinute(this.viewDate, 4 * c));
                    this.dateWithinRange(d) && (this.date = d, this.viewDate = e, this.setValue(), this.update(), a.preventDefault(), b = !0);
                    break;
                case 13:
                    0 != this.viewMode ? (c = this.viewMode, this.showMode( - 1), this.fill(), c == this.viewMode && this.autoclose && this.hide()) : (this.fill(), this.autoclose && this.hide());
                    a.preventDefault();
                    break;
                case 9:
                    this.hide()
                }
                if (b) {
                    var f;
                    this.isInput ? f = this.element: this.component && (f = this.element.find("input"));
                    f && f.change();
                    this.element.trigger({
                        type: "changeDate",
                        date: this.date
                    })
                }
            }
        },
        showMode: function(a) {
            a && (a = Math.max(0, Math.min(g.modes.length - 1, this.viewMode + a)), a >= this.minView && a <= this.maxView && (this.element.trigger({
                type: "changeMode",
                date: this.viewDate,
                oldViewMode: this.viewMode,
                newViewMode: a
            }), this.viewMode = a));
            this.picker.find("\x3ediv").hide().filter(".datetimepicker-" + g.modes[this.viewMode].clsName).css("display", "block");
            this.updateNavArrows()
        },
        reset: function(a) {
            this._setDate(null, "date")
        }
    };
    e.fn.datetimepicker = function(a) {
        var b = Array.apply(null, arguments);
        b.shift();
        return this.each(function() {
            var c = e(this),
            d = c.data("datetimepicker"),
            f = "object" == typeof a && a;
            d || c.data("datetimepicker", d = new u(this, e.extend({},
            e.fn.datetimepicker.defaults, f)));
            "string" == typeof a && "function" == typeof d[a] && d[a].apply(d, b)
        })
    };
    e.fn.datetimepicker.defaults = {};
    e.fn.datetimepicker.Constructor = u;
    var f = e.fn.datetimepicker.dates = {
        en: {
            days: "Sunday Monday Tuesday Wednesday Thursday Friday Saturday Sunday".split(" "),
            daysShort: "Sun Mon Tue Wed Thu Fri Sat Sun".split(" "),
            daysMin: "Su Mo Tu We Th Fr Sa Su".split(" "),
            months: "January February March April May June July August September October November December".split(" "),
            monthsShort: "Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec".split(" "),
            meridiem: ["am", "pm"],
            suffix: ["st", "nd", "rd", "th"],
            today: "Today"
        }
    },
    g = {
        modes: [{
            clsName: "minutes",
            navFnc: "Hours",
            navStep: 1
        },
        {
            clsName: "hours",
            navFnc: "Date",
            navStep: 1
        },
        {
            clsName: "days",
            navFnc: "Month",
            navStep: 1
        },
        {
            clsName: "months",
            navFnc: "FullYear",
            navStep: 1
        },
        {
            clsName: "years",
            navFnc: "FullYear",
            navStep: 10
        }],
        isLeapYear: function(a) {
            return 0 === a % 4 && 0 !== a % 100 || 0 === a % 400
        },
        getDaysInMonth: function(a, b) {
            return [31, g.isLeapYear(a) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][b]
        },
        getDefaultFormat: function(a, b) {
            if ("standard" == a) return "input" == b ? "yyyy-mm-dd hh:ii": "yyyy-mm-dd hh:ii:ss";
            if ("php" == a) return "input" == b ? "Y-m-d H:i": "Y-m-d H:i:s";
            throw Error("Invalid format type.");
        },
        validParts: function(a) {
            if ("standard" == a) return /hh?|HH?|p|P|ii?|ss?|dd?|DD?|mm?|MM?|yy(?:yy)?/g;
            if ("php" == a) return /[dDjlNwzFmMnStyYaABgGhHis]/g;
            throw Error("Invalid format type.");
        },
        nonpunctuation: /[^ -\/:-@\[-`{-~\t\n\rTZ]+/g,
        parseFormat: function(a, b) {
            var c = a.replace(this.validParts(b), "\x00").split("\x00"),
            d = a.match(this.validParts(b));
            if (!c || !c.length || !d || 0 == d.length) throw Error("Invalid date format.");
            return {
                separators: c,
                parts: d
            }
        },
        parseDate: function(a, b, c, d) {
            if (a instanceof Date) return a;
            /^\d{4}\-\d{1,2}\-\d{1,2}$/.test(a) && (b = this.parseFormat("yyyy-mm-dd", d));
            /^\d{4}\-\d{1,2}\-\d{1,2}[T ]\d{1,2}\:\d{1,2}$/.test(a) && (b = this.parseFormat("yyyy-mm-dd hh:ii", d));
            /^\d{4}\-\d{1,2}\-\d{1,2}[T ]\d{1,2}\:\d{1,2}\:\d{1,2}[Z]{0,1}$/.test(a) && (b = this.parseFormat("yyyy-mm-dd hh:ii:ss", d));
            if (/^[-+]\d+[dmwy]([\s,]+[-+]\d+[dmwy])*$/.test(a)) {
                b = /([-+]\d+)([dmwy])/;
                var g = a.match(/([-+]\d+)([dmwy])/g);
                a = new Date;
                for (var l = 0; l < g.length; l++) switch (d = b.exec(g[l]), c = parseInt(d[1]), d[2]) {
                case "d":
                    a.setUTCDate(a.getUTCDate() + c);
                    break;
                case "m":
                    a = u.prototype.moveMonth.call(u.prototype, a, c);
                    break;
                case "w":
                    a.setUTCDate(a.getUTCDate() + 7 * c);
                    break;
                case "y":
                    a = u.prototype.moveYear.call(u.prototype, a, c)
                }
                return r(a.getUTCFullYear(), a.getUTCMonth(), a.getUTCDate(), a.getUTCHours(), a.getUTCMinutes(), a.getUTCSeconds(), 0)
            }
            g = a && a.match(this.nonpunctuation) || [];
            a = new Date(0, 0, 0, 0, 0, 0, 0);
            var v = {},
            w = "hh h ii i ss s yyyy yy M MM m mm D DD d dd H HH p P".split(" "),
            s = {
                hh: function(a, b) {
                    return a.setUTCHours(b)
                },
                h: function(a, b) {
                    return a.setUTCHours(b)
                },
                HH: function(a, b) {
                    return a.setUTCHours(12 == b ? 0 : b)
                },
                H: function(a, b) {
                    return a.setUTCHours(12 == b ? 0 : b)
                },
                ii: function(a, b) {
                    return a.setUTCMinutes(b)
                },
                i: function(a, b) {
                    return a.setUTCMinutes(b)
                },
                ss: function(a, b) {
                    return a.setUTCSeconds(b)
                },
                s: function(a, b) {
                    return a.setUTCSeconds(b)
                },
                yyyy: function(a, b) {
                    return a.setUTCFullYear(b)
                },
                yy: function(a, b) {
                    return a.setUTCFullYear(2E3 + b)
                },
                m: function(a, b) {
                    for (b -= 1; 0 > b;) b += 12;
                    b %= 12;
                    for (a.setUTCMonth(b); a.getUTCMonth() != b;) a.setUTCDate(a.getUTCDate() - 1);
                    return a
                },
                d: function(a, b) {
                    return a.setUTCDate(b)
                },
                p: function(a, b) {
                    return a.setUTCHours(1 == b ? a.getUTCHours() + 12 : a.getUTCHours())
                }
            },
            k;
            s.M = s.MM = s.mm = s.m;
            s.dd = s.d;
            s.P = s.p;
            a = r(a.getFullYear(), a.getMonth(), a.getDate(), a.getHours(), a.getMinutes(), a.getSeconds());
            if (g.length == b.parts.length) {
                for (var l = 0,
                t = b.parts.length; l < t; l++) {
                    k = parseInt(g[l], 10);
                    d = b.parts[l];
                    if (isNaN(k)) switch (d) {
                    case "MM":
                        k = e(f[c].months).filter(function() {
                            var a = this.slice(0, g[l].length),
                            b = g[l].slice(0, a.length);
                            return a == b
                        });
                        k = e.inArray(k[0], f[c].months) + 1;
                        break;
                    case "M":
                        k = e(f[c].monthsShort).filter(function() {
                            var a = this.slice(0, g[l].length),
                            b = g[l].slice(0, a.length);
                            return a == b
                        });
                        k = e.inArray(k[0], f[c].monthsShort) + 1;
                        break;
                    case "p":
                    case "P":
                        k = e.inArray(g[l].toLowerCase(), f[c].meridiem)
                    }
                    v[d] = k
                }
                for (l = 0; l < w.length; l++) if (d = w[l], d in v && !isNaN(v[d])) s[d](a, v[d])
            }
            return a
        },
        formatDate: function(a, b, c, d) {
            if (null == a) return "";
            if ("standard" == d) c = {
                yy: a.getUTCFullYear().toString().substring(2),
                yyyy: a.getUTCFullYear(),
                m: a.getUTCMonth() + 1,
                M: f[c].monthsShort[a.getUTCMonth()],
                MM: f[c].months[a.getUTCMonth()],
                d: a.getUTCDate(),
                D: f[c].daysShort[a.getUTCDay()],
                DD: f[c].days[a.getUTCDay()],
                p: 2 == f[c].meridiem.length ? f[c].meridiem[12 > a.getUTCHours() ? 0 : 1] : "",
                h: a.getUTCHours(),
                i: a.getUTCMinutes(),
                s: a.getUTCSeconds()
            },
            c.H = 0 == c.h % 12 ? 12 : c.h % 12,
            c.HH = (10 > c.H ? "0": "") + c.H,
            c.P = c.p.toUpperCase(),
            c.hh = (10 > c.h ? "0": "") + c.h,
            c.ii = (10 > c.i ? "0": "") + c.i,
            c.ss = (10 > c.s ? "0": "") + c.s,
            c.dd = (10 > c.d ? "0": "") + c.d,
            c.mm = (10 > c.m ? "0": "") + c.m;
            else if ("php" == d) c = {
                y: a.getUTCFullYear().toString().substring(2),
                Y: a.getUTCFullYear(),
                F: f[c].months[a.getUTCMonth()],
                M: f[c].monthsShort[a.getUTCMonth()],
                n: a.getUTCMonth() + 1,
                t: g.getDaysInMonth(a.getUTCFullYear(), a.getUTCMonth()),
                j: a.getUTCDate(),
                l: f[c].days[a.getUTCDay()],
                D: f[c].daysShort[a.getUTCDay()],
                w: a.getUTCDay(),
                N: 0 == a.getUTCDay() ? 7 : a.getUTCDay(),
                S: a.getUTCDate() % 10 <= f[c].suffix.length ? f[c].suffix[a.getUTCDate() % 10 - 1] : "",
                a: 2 == f[c].meridiem.length ? f[c].meridiem[12 > a.getUTCHours() ? 0 : 1] : "",
                g: 0 == a.getUTCHours() % 12 ? 12 : a.getUTCHours() % 12,
                G: a.getUTCHours(),
                i: a.getUTCMinutes(),
                s: a.getUTCSeconds()
            },
            c.m = (10 > c.n ? "0": "") + c.n,
            c.d = (10 > c.j ? "0": "") + c.j,
            c.A = c.a.toString().toUpperCase(),
            c.h = (10 > c.g ? "0": "") + c.g,
            c.H = (10 > c.G ? "0": "") + c.G,
            c.i = (10 > c.i ? "0": "") + c.i,
            c.s = (10 > c.s ? "0": "") + c.s;
            else throw Error("Invalid format type.");
            a = [];
            d = e.extend([], b.separators);
            for (var n = 0,
            l = b.parts.length; n < l; n++) d.length && a.push(d.shift()),
            a.push(c[b.parts[n]]);
            return a.join("")
        },
        convertViewMode: function(a) {
            switch (a) {
            case 4:
            case "decade":
                a = 4;
                break;
            case 3:
            case "year":
                a = 3;
                break;
            case 2:
            case "month":
                a = 2;
                break;
            case 1:
            case "day":
                a = 1;
                break;
            case 0:
            case "hour":
                a = 0
            }
            return a
        },
        headTemplate: '\x3cthead\x3e\x3ctr\x3e\x3cth class\x3d"prev"\x3e\x3ci class\x3d"icon-arrow-left"/\x3e\x3c/th\x3e\x3cth colspan\x3d"5" class\x3d"switch"\x3e\x3c/th\x3e\x3cth class\x3d"next"\x3e\x3ci class\x3d"icon-arrow-right"/\x3e\x3c/th\x3e\x3c/tr\x3e\x3c/thead\x3e',
        contTemplate: '\x3ctbody\x3e\x3ctr\x3e\x3ctd colspan\x3d"7"\x3e\x3c/td\x3e\x3c/tr\x3e\x3c/tbody\x3e',
        footTemplate: '\x3ctfoot\x3e\x3ctr\x3e\x3cth colspan\x3d"7" class\x3d"dategroup"\x3e\x3c/th\x3e\x3c/tr\x3e\x3c/tfoot\x3e'
    };
    g.template = '\x3cdiv class\x3d"datetimepicker"\x3e\x3cdiv class\x3d"datetimepicker-minutes"\x3e\x3ctable class\x3d" table-condensed"\x3e' + g.headTemplate + g.contTemplate + g.footTemplate + '\x3c/table\x3e\x3c/div\x3e\x3cdiv class\x3d"datetimepicker-hours"\x3e\x3ctable class\x3d" table-condensed"\x3e' + g.headTemplate + g.contTemplate + g.footTemplate + '\x3c/table\x3e\x3c/div\x3e\x3cdiv class\x3d"datetimepicker-days"\x3e\x3ctable class\x3d" table-condensed"\x3e' + g.headTemplate + "\x3ctbody\x3e\x3c/tbody\x3e" + g.footTemplate + '\x3c/table\x3e\x3c/div\x3e\x3cdiv class\x3d"datetimepicker-months"\x3e\x3ctable class\x3d"table-condensed"\x3e' + g.headTemplate + g.contTemplate + g.footTemplate + '\x3c/table\x3e\x3c/div\x3e\x3cdiv class\x3d"datetimepicker-years"\x3e\x3ctable class\x3d"table-condensed"\x3e' + g.headTemplate + g.contTemplate + g.footTemplate + "\x3c/table\x3e\x3c/div\x3e\x3c/div\x3e";
    e.fn.datetimepicker.DPGlobal = g;
    e.fn.datetimepicker.noConflict = function() {
        e.fn.datetimepicker = old;
        return this
    };
    e(document).on("focus.datetimepicker.data-api click.datetimepicker.data-api", '[data-provide\x3d"datetimepicker"]',
    function(a) {
        var b = e(this);
        b.data("datetimepicker") || (a.preventDefault(), b.datetimepicker("show"))
    });
    e(function() {
        e('[data-provide\x3d"datetimepicker-inline"]').datetimepicker()
    })
} (window.jQuery); (function(e) {
    e.fn.datetimepicker.dates["zh-CN"] = {
        days: "\u661f\u671f\u65e5 \u661f\u671f\u4e00 \u661f\u671f\u4e8c \u661f\u671f\u4e09 \u661f\u671f\u56db \u661f\u671f\u4e94 \u661f\u671f\u516d \u661f\u671f\u65e5".split(" "),
        daysShort: "\u5468\u65e5 \u5468\u4e00 \u5468\u4e8c \u5468\u4e09 \u5468\u56db \u5468\u4e94 \u5468\u516d \u5468\u65e5".split(" "),
        daysMin: "\u65e5\u4e00\u4e8c\u4e09\u56db\u4e94\u516d\u65e5".split(""),
        months: "\u4e00\u6708 \u4e8c\u6708 \u4e09\u6708 \u56db\u6708 \u4e94\u6708 \u516d\u6708 \u4e03\u6708 \u516b\u6708 \u4e5d\u6708 \u5341\u6708 \u5341\u4e00\u6708 \u5341\u4e8c\u6708".split(" "),
        monthsShort: "\u4e00\u6708 \u4e8c\u6708 \u4e09\u6708 \u56db\u6708 \u4e94\u6708 \u516d\u6708 \u4e03\u6708 \u516b\u6708 \u4e5d\u6708 \u5341\u6708 \u5341\u4e00\u6708 \u5341\u4e8c\u6708".split(" "),
        today: "\u4eca\u5929",
        suffix: [],
        meridiem: ["\u4e0a\u5348", "\u4e0b\u5348"]
    }
})(jQuery); (function(d) {
    var t, l, u, k = d(window),
    x = {
        bootstrap: {
            container: "alert alert-warning",
            notice: "",
            notice_icon: "icon-exclamation-sign",
            info: "alert-info",
            info_icon: "icon-info-sign",
            success: "alert-success",
            success_icon: "icon-ok-sign",
            error: "alert-error",
            error_icon: "icon-warning-sign",
            closer: "icon-remove",
            pin_up: "icon-pause",
            pin_down: "icon-play",
            hi_menu: "well",
            hi_btn: "btn",
            hi_btnhov: "",
            hi_hnd: "icon-chevron-down"
        }
    },
    v = function() {
        u = d("body");
        k = d(window);
        k.bind("resize",
        function() {
            l && clearTimeout(l);
            l = setTimeout(d.pnotify_position_all, 10)
        })
    };
    document.body ? v() : d(v);
    d.extend({
        pnotify_remove_all: function() {
            var e = k.data("pnotify");
            e && e.length && d.each(e,
            function() {
                this.pnotify_remove && this.pnotify_remove()
            })
        },
        pnotify_position_all: function() {
            l && clearTimeout(l);
            l = null;
            var e = k.data("pnotify");
            e && e.length && (d.each(e,
            function() {
                var d = this.opts.stack;
                d && (d.nextpos1 = d.firstpos1, d.nextpos2 = d.firstpos2, d.addpos2 = 0, d.animation = !0)
            }), d.each(e,
            function() {
                this.pnotify_position()
            }))
        },
        pnotify: function(e) {
            var g, a;
            "object" != typeof e ? (a = d.extend({},
            d.pnotify.defaults), a.text = e) : a = d.extend({},
            d.pnotify.defaults, e);
            for (var s in a)"string" == typeof s && s.match(/^pnotify_/) && (a[s.replace(/^pnotify_/, "")] = a[s]);
            if (a.before_init && !1 === a.before_init(a)) return null;
            var m, r = function(a, c) {
                b.css("display", "none");
                var f = document.elementFromPoint(a.clientX, a.clientY);
                b.css("display", "block");
                var e = d(f),
                g = e.css("cursor");
                b.css("cursor", "auto" != g ? g: "default");
                m && m.get(0) == f || (m && (q.call(m.get(0), "mouseleave", a.originalEvent), q.call(m.get(0), "mouseout", a.originalEvent)), q.call(f, "mouseenter", a.originalEvent), q.call(f, "mouseover", a.originalEvent));
                q.call(f, c, a.originalEvent);
                m = e
            },
            f = x[a.styling],
            b = d("\x3cdiv /\x3e", {
                "class": "ui-pnotify " + a.addclass,
                css: {
                    display: "none"
                },
                mouseenter: function(n) {
                    a.nonblock && n.stopPropagation();
                    a.mouse_reset && "out" == g && (b.stop(!0), g = "in", b.css("height", "auto").animate({
                        width: a.width,
                        opacity: a.nonblock ? a.nonblock_opacity: a.opacity
                    },
                    "fast"));
                    a.nonblock && b.animate({
                        opacity: a.nonblock_opacity
                    },
                    "fast");
                    a.hide && a.mouse_reset && b.pnotify_cancel_remove();
                    a.sticker && !a.nonblock && b.sticker.trigger("pnotify_icon").css("visibility", "visible");
                    a.closer && !a.nonblock && b.closer.css("visibility", "visible")
                },
                mouseleave: function(n) {
                    a.nonblock && n.stopPropagation();
                    m = null;
                    b.css("cursor", "auto");
                    a.nonblock && "out" != g && b.animate({
                        opacity: a.opacity
                    },
                    "fast");
                    a.hide && a.mouse_reset && b.pnotify_queue_remove();
                    a.sticker_hover && b.sticker.css("visibility", "hidden");
                    a.closer_hover && b.closer.css("visibility", "hidden");
                    d.pnotify_position_all()
                },
                mouseover: function(b) {
                    a.nonblock && b.stopPropagation()
                },
                mouseout: function(b) {
                    a.nonblock && b.stopPropagation()
                },
                mousemove: function(b) {
                    a.nonblock && (b.stopPropagation(), r(b, "onmousemove"))
                },
                mousedown: function(b) {
                    a.nonblock && (b.stopPropagation(), b.preventDefault(), r(b, "onmousedown"))
                },
                mouseup: function(b) {
                    a.nonblock && (b.stopPropagation(), b.preventDefault(), r(b, "onmouseup"))
                },
                click: function(b) {
                    a.nonblock && (b.stopPropagation(), r(b, "onclick"))
                },
                dblclick: function(b) {
                    a.nonblock && (b.stopPropagation(), r(b, "ondblclick"))
                }
            });
            b.opts = a;
            b.container = d("\x3cdiv /\x3e", {
                "class": f.container + " ui-pnotify-container " + ("error" == a.type ? f.error: "info" == a.type ? f.info: "success" == a.type ? f.success: f.notice)
            }).appendTo(b);
            "" != a.cornerclass && b.container.removeClass("ui-corner-all").addClass(a.cornerclass);
            a.shadow && b.container.addClass("ui-pnotify-shadow");
            b.pnotify_version = "1.2.0";
            b.pnotify = function(n) {
                var c = a;
                "string" == typeof n ? a.text = n: a = d.extend({},
                a, n);
                for (var e in a)"string" == typeof e && e.match(/^pnotify_/) && (a[e.replace(/^pnotify_/, "")] = a[e]);
                b.opts = a;
                a.cornerclass != c.cornerclass && b.container.removeClass("ui-corner-all").addClass(a.cornerclass);
                a.shadow != c.shadow && (a.shadow ? b.container.addClass("ui-pnotify-shadow") : b.container.removeClass("ui-pnotify-shadow")); ! 1 === a.addclass ? b.removeClass(c.addclass) : a.addclass !== c.addclass && b.removeClass(c.addclass).addClass(a.addclass); ! 1 === a.title ? b.title_container.slideUp("fast") : a.title !== c.title && (a.title_escape ? b.title_container.text(a.title).slideDown(200) : b.title_container.html(a.title).slideDown(200)); ! 1 === a.text ? b.text_container.slideUp("fast") : a.text !== c.text && (a.text_escape ? b.text_container.text(a.text).slideDown(200) : b.text_container.html(a.insert_brs ? String(a.text).replace(/\n/g, "\x3cbr /\x3e") : a.text).slideDown(200));
                b.pnotify_history = a.history;
                b.pnotify_hide = a.hide;
                a.type != c.type && b.container.removeClass(f.error + " " + f.notice + " " + f.success + " " + f.info).addClass("error" == a.type ? f.error: "info" == a.type ? f.info: "success" == a.type ? f.success: f.notice);
                if (a.icon !== c.icon || !0 === a.icon && a.type != c.type) b.container.find("div.ui-pnotify-icon").remove(),
                !1 !== a.icon && d("\x3cdiv /\x3e", {
                    "class": "ui-pnotify-icon"
                }).append(d("\x3cspan /\x3e", {
                    "class": !0 === a.icon ? "error" == a.type ? f.error_icon: "info" == a.type ? f.info_icon: "success" == a.type ? f.success_icon: f.notice_icon: a.icon
                })).prependTo(b.container);
                a.width !== c.width && b.animate({
                    width: a.width
                });
                a.min_height !== c.min_height && b.container.animate({
                    minHeight: a.min_height
                });
                a.opacity !== c.opacity && b.fadeTo(a.animate_speed, a.opacity); ! a.closer || a.nonblock ? b.closer.css("display", "none") : b.closer.css("display", "block"); ! a.sticker || a.nonblock ? b.sticker.css("display", "none") : b.sticker.css("display", "block");
                b.sticker.trigger("pnotify_icon");
                a.sticker_hover ? b.sticker.css("visibility", "hidden") : a.nonblock || b.sticker.css("visibility", "visible");
                a.closer_hover ? b.closer.css("visibility", "hidden") : a.nonblock || b.closer.css("visibility", "visible");
                a.hide ? c.hide || b.pnotify_queue_remove() : b.pnotify_cancel_remove();
                b.pnotify_queue_position();
                return b
            };
            b.pnotify_position = function(a) {
                var c = b.opts.stack;
                if (c) {
                    c.nextpos1 || (c.nextpos1 = c.firstpos1);
                    c.nextpos2 || (c.nextpos2 = c.firstpos2);
                    c.addpos2 || (c.addpos2 = 0);
                    var d = "none" == b.css("display");
                    if (!d || a) {
                        var f, e = {},
                        g;
                        switch (c.dir1) {
                        case "down":
                            g = "top";
                            break;
                        case "up":
                            g = "bottom";
                            break;
                        case "left":
                            g = "right";
                            break;
                        case "right":
                            g = "left"
                        }
                        a = parseInt(b.css(g));
                        isNaN(a) && (a = 0);
                        "undefined" != typeof c.firstpos1 || d || (c.firstpos1 = a, c.nextpos1 = c.firstpos1);
                        var h;
                        switch (c.dir2) {
                        case "down":
                            h = "top";
                            break;
                        case "up":
                            h = "bottom";
                            break;
                        case "left":
                            h = "right";
                            break;
                        case "right":
                            h = "left"
                        }
                        f = parseInt(b.css(h));
                        isNaN(f) && (f = 0);
                        "undefined" != typeof c.firstpos2 || d || (c.firstpos2 = f, c.nextpos2 = c.firstpos2);
                        if ("down" == c.dir1 && c.nextpos1 + b.height() > k.height() || "up" == c.dir1 && c.nextpos1 + b.height() > k.height() || "left" == c.dir1 && c.nextpos1 + b.width() > k.width() || "right" == c.dir1 && c.nextpos1 + b.width() > k.width()) c.nextpos1 = c.firstpos1,
                        c.nextpos2 += c.addpos2 + ("undefined" == typeof c.spacing2 ? 25 : c.spacing2),
                        c.addpos2 = 0;
                        if (c.animation && c.nextpos2 < f) switch (c.dir2) {
                        case "down":
                            e.top = c.nextpos2 + "px";
                            break;
                        case "up":
                            e.bottom = c.nextpos2 + "px";
                            break;
                        case "left":
                            e.right = c.nextpos2 + "px";
                            break;
                        case "right":
                            e.left = c.nextpos2 + "px"
                        } else b.css(h, c.nextpos2 + "px");
                        switch (c.dir2) {
                        case "down":
                        case "up":
                            b.outerHeight(!0) > c.addpos2 && (c.addpos2 = b.height());
                            break;
                        case "left":
                        case "right":
                            b.outerWidth(!0) > c.addpos2 && (c.addpos2 = b.width())
                        }
                        if (c.nextpos1) if (c.animation && (a > c.nextpos1 || e.top || e.bottom || e.right || e.left)) switch (c.dir1) {
                        case "down":
                            e.top = c.nextpos1 + "px";
                            break;
                        case "up":
                            e.bottom = c.nextpos1 + "px";
                            break;
                        case "left":
                            e.right = c.nextpos1 + "px";
                            break;
                        case "right":
                            e.left = c.nextpos1 + "px"
                        } else b.css(g, c.nextpos1 + "px"); (e.top || e.bottom || e.right || e.left) && b.animate(e, {
                            duration: 500,
                            queue: !1
                        });
                        switch (c.dir1) {
                        case "down":
                        case "up":
                            c.nextpos1 += b.height() + ("undefined" == typeof c.spacing1 ? 25 : c.spacing1);
                            break;
                        case "left":
                        case "right":
                            c.nextpos1 += b.width() + ("undefined" == typeof c.spacing1 ? 25 : c.spacing1)
                        }
                    }
                }
            };
            b.pnotify_queue_position = function(a) {
                l && clearTimeout(l);
                a || (a = 10);
                l = setTimeout(d.pnotify_position_all, a)
            };
            b.pnotify_display = function() {
                b.parent().length || b.appendTo(u);
                a.before_open && !1 === a.before_open(b) || ("top" != a.stack.push && b.pnotify_position(!0), "fade" == a.animation || "fade" == a.animation.effect_in ? b.show().fadeTo(0, 0).hide() : 1 != a.opacity && b.show().fadeTo(0, a.opacity).hide(), b.animate_in(function() {
                    a.after_open && a.after_open(b);
                    b.pnotify_queue_position();
                    a.hide && b.pnotify_queue_remove()
                }))
            };
            b.pnotify_remove = function() {
                b.timer && (window.clearTimeout(b.timer), b.timer = null);
                a.before_close && !1 === a.before_close(b) || b.animate_out(function() {
                    a.after_close && !1 === a.after_close(b) || (b.pnotify_queue_position(), a.remove && b.detach())
                })
            };
            b.animate_in = function(d) {
                g = "in";
                var c;
                c = "undefined" != typeof a.animation.effect_in ? a.animation.effect_in: a.animation;
                "none" == c ? (b.show(), d()) : "show" == c ? b.show(a.animate_speed, d) : "fade" == c ? b.show().fadeTo(a.animate_speed, a.opacity, d) : "slide" == c ? b.slideDown(a.animate_speed, d) : "function" == typeof c ? c("in", d, b) : b.show(c, "object" == typeof a.animation.options_in ? a.animation.options_in: {},
                a.animate_speed, d)
            };
            b.animate_out = function(d) {
                g = "out";
                var c;
                c = "undefined" != typeof a.animation.effect_out ? a.animation.effect_out: a.animation;
                "none" == c ? (b.hide(), d()) : "show" == c ? b.hide(a.animate_speed, d) : "fade" == c ? b.fadeOut(a.animate_speed, d) : "slide" == c ? b.slideUp(a.animate_speed, d) : "function" == typeof c ? c("out", d, b) : b.hide(c, "object" == typeof a.animation.options_out ? a.animation.options_out: {},
                a.animate_speed, d)
            };
            b.pnotify_cancel_remove = function() {
                b.timer && window.clearTimeout(b.timer)
            };
            b.pnotify_queue_remove = function() {
                b.pnotify_cancel_remove();
                b.timer = window.setTimeout(function() {
                    b.pnotify_remove()
                },
                isNaN(a.delay) ? 0 : a.delay)
            };
            b.closer = d("\x3cdiv /\x3e", {
                "class": "ui-pnotify-closer",
                css: {
                    cursor: "pointer",
                    visibility: a.closer_hover ? "hidden": "visible"
                },
                click: function() {
                    b.pnotify_remove();
                    b.sticker.css("visibility", "hidden");
                    b.closer.css("visibility", "hidden")
                }
            }).append(d("\x3cspan /\x3e", {
                "class": f.closer
            })).appendTo(b.container);
            a.closer && !a.nonblock || b.closer.css("display", "none");
            b.sticker = d("\x3cdiv /\x3e", {
                "class": "ui-pnotify-sticker",
                css: {
                    cursor: "pointer",
                    visibility: a.sticker_hover ? "hidden": "visible"
                },
                click: function() {
                    a.hide = !a.hide;
                    a.hide ? b.pnotify_queue_remove() : b.pnotify_cancel_remove();
                    d(this).trigger("pnotify_icon")
                }
            }).bind("pnotify_icon",
            function() {
                d(this).children().removeClass(f.pin_up + " " + f.pin_down).addClass(a.hide ? f.pin_up: f.pin_down)
            }).append(d("\x3cspan /\x3e", {
                "class": f.pin_up
            })).appendTo(b.container);
            a.sticker && !a.nonblock || b.sticker.css("display", "none"); ! 1 !== a.icon && d("\x3cdiv /\x3e", {
                "class": "ui-pnotify-icon"
            }).append(d("\x3cspan /\x3e", {
                "class": !0 === a.icon ? "error" == a.type ? f.error_icon: "info" == a.type ? f.info_icon: "success" == a.type ? f.success_icon: f.notice_icon: a.icon
            })).prependTo(b.container);
            b.title_container = d("\x3ch4 /\x3e", {
                "class": "ui-pnotify-title"
            }).appendTo(b.container); ! 1 === a.title ? b.title_container.hide() : a.title_escape ? b.title_container.text(a.title) : b.title_container.html(a.title);
            b.text_container = d("\x3cdiv /\x3e", {
                "class": "ui-pnotify-text"
            }).appendTo(b.container); ! 1 === a.text ? b.text_container.hide() : a.text_escape ? b.text_container.text(a.text) : b.text_container.html(a.insert_brs ? String(a.text).replace(/\n/g, "\x3cbr /\x3e") : a.text);
            "string" == typeof a.width && b.css("width", a.width);
            "string" == typeof a.min_height && b.container.css("min-height", a.min_height);
            b.pnotify_history = a.history;
            b.pnotify_hide = a.hide;
            var h = k.data("pnotify");
            if (null == h || "object" != typeof h) h = [];
            h = "top" == a.stack.push ? d.merge([b], h) : d.merge(h, [b]);
            k.data("pnotify", h);
            "top" == a.stack.push && b.pnotify_queue_position(1);
            a.after_init && a.after_init(b);
            if (a.history) {
                var p = k.data("pnotify_history");
                "undefined" == typeof p && (p = d("\x3cdiv /\x3e", {
                    "class": "ui-pnotify-history-container " + f.hi_menu,
                    mouseleave: function() {
                        p.animate({
                            top: "-" + t + "px"
                        },
                        {
                            duration: 100,
                            queue: !1
                        })
                    }
                }).append(d("\x3cdiv /\x3e", {
                    "class": "ui-pnotify-history-header",
                    text: "Redisplay"
                })).append(d("\x3cbutton /\x3e", {
                    "class": "ui-pnotify-history-all " + f.hi_btn,
                    text: "All",
                    mouseenter: function() {
                        d(this).addClass(f.hi_btnhov)
                    },
                    mouseleave: function() {
                        d(this).removeClass(f.hi_btnhov)
                    },
                    click: function() {
                        d.each(h,
                        function() {
                            this.pnotify_history && (this.is(":visible") ? this.pnotify_hide && this.pnotify_queue_remove() : this.pnotify_display && this.pnotify_display())
                        });
                        return ! 1
                    }
                })).append(d("\x3cbutton /\x3e", {
                    "class": "ui-pnotify-history-last " + f.hi_btn,
                    text: "Last",
                    mouseenter: function() {
                        d(this).addClass(f.hi_btnhov)
                    },
                    mouseleave: function() {
                        d(this).removeClass(f.hi_btnhov)
                    },
                    click: function() {
                        var a = -1,
                        b;
                        do {
                            b = -1 == a ? h.slice(a) : h.slice(a, a + 1);
                            if (!b[0]) break;
                            a--
                        } while (! b [ 0 ].pnotify_history || b[0].is(":visible"));
                        if (!b[0]) return ! 1;
                        b[0].pnotify_display && b[0].pnotify_display();
                        return ! 1
                    }
                })).appendTo(u), t = d("\x3cspan /\x3e", {
                    "class": "ui-pnotify-history-pulldown " + f.hi_hnd,
                    mouseenter: function() {
                        p.animate({
                            top: "0"
                        },
                        {
                            duration: 100,
                            queue: !1
                        })
                    }
                }).appendTo(p).offset().top + 2, p.css({
                    top: "-" + t + "px"
                }), k.data("pnotify_history", p))
            }
            a.stack.animation = !1;
            b.pnotify_display();
            return b
        }
    });
    var w = /^on/,
    y = /^(dbl)?click$|^mouse(move|down|up|over|out|enter|leave)$|^contextmenu$/,
    z = /^(focus|blur|select|change|reset)$|^key(press|down|up)$/,
    A = /^(scroll|resize|(un)?load|abort|error)$/,
    q = function(e, g) {
        var a;
        e = e.toLowerCase();
        document.createEvent && this.dispatchEvent ? (e = e.replace(w, ""), e.match(y) ? (d(this).offset(), a = document.createEvent("MouseEvents"), a.initMouseEvent(e, g.bubbles, g.cancelable, g.view, g.detail, g.screenX, g.screenY, g.clientX, g.clientY, g.ctrlKey, g.altKey, g.shiftKey, g.metaKey, g.button, g.relatedTarget)) : e.match(z) ? (a = document.createEvent("UIEvents"), a.initUIEvent(e, g.bubbles, g.cancelable, g.view, g.detail)) : e.match(A) && (a = document.createEvent("HTMLEvents"), a.initEvent(e, g.bubbles, g.cancelable)), a && this.dispatchEvent(a)) : (e.match(w) || (e = "on" + e), a = document.createEventObject(g), this.fireEvent(e, a))
    };
    d.pnotify.defaults = {
        title: !1,
        title_escape: !1,
        text: !1,
        text_escape: !1,
        styling: "bootstrap",
        addclass: "",
        cornerclass: "",
        nonblock: !1,
        nonblock_opacity: .2,
        history: !0,
        width: "300px",
        min_height: "16px",
        type: "notice",
        icon: !0,
        animation: "fade",
        animate_speed: "slow",
        opacity: 1,
        shadow: !0,
        closer: !0,
        closer_hover: !0,
        sticker: !0,
        sticker_hover: !0,
        hide: !0,
        delay: 3E3,
        mouse_reset: !0,
        remove: !0,
        insert_brs: !0,
        stack: {
            dir1: "down",
            dir2: "left",
            push: "bottom",
            spacing1: 25,
            spacing2: 25
        }
    }
})(jQuery);
window.bootbox = window.bootbox ||
function(w, r) {
    function m(b, a) {
        "undefined" === typeof a && (a = t);
        return "string" === typeof p[a][b] ? p[a][b] : "en" != a ? m(b, "en") : b
    }
    var t = "en",
    u = !0,
    s = "static",
    v = "",
    h = {},
    k = {},
    n = {
        setLocale: function(b) {
            for (var a in p) if (a == b) {
                t = b;
                return
            }
            throw Error("Invalid locale: " + b);
        },
        addLocale: function(b, a) {
            "undefined" === typeof p[b] && (p[b] = {});
            for (var c in a) p[b][c] = a[c]
        },
        setIcons: function(b) {
            k = b;
            if ("object" !== typeof k || null === k) k = {}
        },
        setBtnClasses: function(b) {
            h = b;
            if ("object" !== typeof h || null === h) h = {}
        },
        alert: function() {
            var b = "",
            a = m("OK"),
            c = null;
            switch (arguments.length) {
            case 1:
                b = arguments[0];
                break;
            case 2:
                b = arguments[0];
                "function" == typeof arguments[1] ? c = arguments[1] : a = arguments[1];
                break;
            case 3:
                b = arguments[0];
                a = arguments[1];
                c = arguments[2];
                break;
            default:
                throw Error("Incorrect number of arguments: expected 1-3");
            }
            var e = n.dialog(b, {
                label: a,
                icon: k.OK,
                "class": h.OK,
                callback: c
            },
            {
                header: '\x3ci class\x3d"icon-ok-sign"\x3e\x3c/i\x3e \u6d88\u606f',
                onEscape: c || !0
            });
            e.off("keyup.alert").on("keyup.alert",
            function(a) {
                13 == a.which && e.modal("hide")
            });
            return e
        },
        confirm: function() {
            var b = "",
            a = m("CANCEL"),
            c = m("CONFIRM"),
            e = null;
            switch (arguments.length) {
            case 1:
                b = arguments[0];
                break;
            case 2:
                b = arguments[0];
                "function" == typeof arguments[1] ? e = arguments[1] : a = arguments[1];
                break;
            case 3:
                b = arguments[0];
                a = arguments[1];
                "function" == typeof arguments[2] ? e = arguments[2] : c = arguments[2];
                break;
            case 4:
                b = arguments[0];
                a = arguments[1];
                c = arguments[2];
                e = arguments[3];
                break;
            default:
                throw Error("Incorrect number of arguments: expected 1-4");
            }
            var g = function() {
                if ("function" === typeof e) return e(!1)
            },
            l = function() {
                if ("function" === typeof e) return e(!0)
            },
            d = n.dialog(b, [{
                label: c,
                icon: k.CONFIRM,
                "class": h.CONFIRM,
                callback: l
            },
            {
                label: a,
                icon: k.CANCEL,
                "class": h.CANCEL,
                callback: g
            }], {
                header: '\x3ci class\x3d"icon-info-sign"\x3e\x3c/i\x3e \u786e\u8ba4',
                onEscape: g
            });
            d.off("keyup.confirm").on("keyup.confirm",
            function(a) {
                13 == a.which && (d.modal("hide"), l())
            });
            return d
        },
        prompt: function() {
            var b = "",
            a = m("CANCEL"),
            c = m("CONFIRM"),
            e = null,
            g = "";
            switch (arguments.length) {
            case 1:
                b = arguments[0];
                break;
            case 2:
                b = arguments[0];
                "function" == typeof arguments[1] ? e = arguments[1] : a = arguments[1];
                break;
            case 3:
                b = arguments[0];
                a = arguments[1];
                "function" == typeof arguments[2] ? e = arguments[2] : c = arguments[2];
                break;
            case 4:
                b = arguments[0];
                a = arguments[1];
                c = arguments[2];
                e = arguments[3];
                break;
            case 5:
                b = arguments[0];
                a = arguments[1];
                c = arguments[2];
                e = arguments[3];
                g = arguments[4];
                break;
            default:
                throw Error("Incorrect number of arguments: expected 1-5");
            }
            var l = r("\x3cform\x3e\x3c/form\x3e");
            l.append("\x3cinput class\x3d'input-block-level' autocomplete\x3doff type\x3dtext value\x3d'" + g + "' /\x3e");
            var g = function() {
                if ("function" === typeof e) return e(null)
            },
            d = n.dialog(l, [{
                label: c,
                icon: k.CONFIRM,
                "class": h.CONFIRM,
                callback: function() {
                    if ("function" === typeof e) return e(l.find("input[type\x3dtext]").val())
                }
            },
            {
                label: a,
                icon: k.CANCEL,
                "class": h.CANCEL,
                callback: g
            }], {
                header: b,
                show: !1,
                onEscape: g
            });
            d.on("shown",
            function() {
                l.find("input[type\x3dtext]").focus();
                l.on("submit",
                function(a) {
                    a.preventDefault();
                    d.find(".btn-primary").click()
                })
            });
            d.modal("show");
            return d
        },
        dialog: function(b, a, c) {
            function e(a) {
                a = null;
                "function" === typeof c.onEscape && (a = c.onEscape()); ! 1 !== a && f.modal("hide")
            }
            var g = "",
            l = [];
            c || (c = {});
            "undefined" === typeof a ? a = [] : "undefined" == typeof a.length && (a = [a]);
            for (var d = a.length; d--;) {
                var h = null,
                k = null,
                q = null,
                m = "",
                p = null;
                if ("undefined" == typeof a[d].label && "undefined" == typeof a[d]["class"] && "undefined" == typeof a[d].callback) {
                    var h = 0,
                    k = null,
                    n;
                    for (n in a[d]) if (k = n, 1 < ++h) break;
                    1 == h && "function" == typeof a[d][n] && (a[d].label = k, a[d].callback = a[d][n])
                }
                "function" == typeof a[d].callback && (p = a[d].callback);
                a[d]["class"] ? q = a[d]["class"] : 0 == d && 2 >= a.length && (q = "btn-primary"); ! 0 !== a[d].link && (q = "btn btn-sm " + q);
                h = a[d].label ? a[d].label: "Option " + (d + 1);
                a[d].icon && (m = "\x3ci class\x3d'" + a[d].icon + "'\x3e\x3c/i\x3e ");
                a[d].href ? (k = a[d].href, g = "\x3ca data-handler\x3d'" + d + "' class\x3d'" + q + "' href\x3d'" + k + "'\x3e" + m + "" + h + "\x3c/a\x3e" + g) : g = "\x3ca data-handler\x3d'" + d + "' class\x3d'" + q + "'\x3e" + m + "" + h + "\x3c/a\x3e" + g;
                l[d] = p
            }
            d = ["\x3cdiv class\x3d'bootbox modal' tabindex\x3d'-1' style\x3d'overflow:hidden;'\x3e\x3cdiv class\x3d'modal-dialog'\x3e\x3cdiv class\x3d'modal-content'\x3e"];
            if (c.header) {
                q = "";
                if ("undefined" == typeof c.headerCloseButton || c.headerCloseButton) q = "\x3ca href\x3d'javascript:;' class\x3d'close'\x3e\x26times;\x3c/a\x3e";
                d.push("\x3cdiv class\x3d'modal-header'\x3e" + q + "\x3ch5 class\x3d'modal-title'\x3e" + c.header + "\x3c/h5\x3e\x3c/div\x3e")
            }
            d.push("\x3cdiv class\x3d'modal-body'\x3e\x3c/div\x3e");
            g && d.push("\x3cdiv class\x3d'modal-footer'\x3e" + g + "\x3c/div\x3e");
            d.push("\x3c/div\x3e\x3c/div\x3e\x3c/div\x3e");
            var f = r(d.join("\n")); ("undefined" === typeof c.animate ? u: c.animate) && f.addClass("fade"); (g = "undefined" === typeof c.classes ? v: c.classes) && f.addClass(g);
            f.find(".modal-body").html(b);
            f.on("keyup.dismiss.modal",
            function(a) {
                27 === a.which && c.onEscape && e("escape")
            });
            f.on("click", "a.close",
            function(a) {
                a.preventDefault();
                e("close")
            });
            f.on("shown",
            function() {
                f.find("a.btn-primary:first").focus()
            });
            f.on("hidden.bs.modal",
            function(a) {
                a.target === this && f.remove()
            });
            f.off("keyup").on("keyup",
            function(a) {
                27 == a.which && f.modal("hide")
            });
            f.on("click", ".modal-footer a",
            function(b) {
                var c = r(this).data("handler"),
                d = l[c],
                e = null;
                if ("undefined" === typeof c || "undefined" === typeof a[c].href) b.preventDefault(),
                "function" === typeof d && (e = d(b)),
                !1 !== e && f.modal("hide")
            });
            r("body").append(f);
            f.modal({
                backdrop: "undefined" === typeof c.backdrop ? s: c.backdrop,
                keyboard: !1,
                show: !1
            });
            f.on("show",
            function(a) {
                r(w).off("focusin.modal")
            });
            "undefined" !== typeof c.show && !0 !== c.show || f.modal("show");
            return f
        },
        modal: function() {
            var b, a, c, e = {
                onEscape: null,
                keyboard: !0,
                backdrop: s
            };
            switch (arguments.length) {
            case 1:
                b = arguments[0];
                break;
            case 2:
                b = arguments[0];
                "object" == typeof arguments[1] ? c = arguments[1] : a = arguments[1];
                break;
            case 3:
                b = arguments[0];
                a = arguments[1];
                c = arguments[2];
                break;
            default:
                throw Error("Incorrect number of arguments: expected 1-3");
            }
            e.header = a;
            c = "object" == typeof c ? r.extend(e, c) : e;
            return n.dialog(b, [], c)
        },
        hideAll: function() {
            r(".bootbox").modal("hide")
        },
        animate: function(b) {
            u = b
        },
        backdrop: function(b) {
            s = b
        },
        classes: function(b) {
            v = b
        }
    },
    p = {
        en: {
            OK: "OK",
            CANCEL: "Cancel",
            CONFIRM: "OK"
        },
        zh_CN: {
            OK: "OK",
            CANCEL: "\u53d6\u6d88",
            CONFIRM: "\u786e\u8ba4"
        },
        zh_TW: {
            OK: "OK",
            CANCEL: "\u53d6\u6d88",
            CONFIRM: "\u78ba\u8a8d"
        }
    };
    return n
} (document, window.jQuery); (function(t) {
    "use strict";
    function e(t) {
        return RegExp("(^|\\s+)" + t + "(\\s+|$)")
    }
    function i(t, e) {
        var i = n(t, e) ? r: o;
        i(t, e)
    }
    var n, o, r;
    "classList" in document.documentElement ? (n = function(t, e) {
        return t.classList.contains(e)
    },
    o = function(t, e) {
        t.classList.add(e)
    },
    r = function(t, e) {
        t.classList.remove(e)
    }) : (n = function(t, i) {
        return e(i).test(t.className)
    },
    o = function(t, e) {
        n(t, e) || (t.className = t.className + " " + e)
    },
    r = function(t, i) {
        t.className = t.className.replace(e(i), " ")
    });
    var s = {
        hasClass: n,
        addClass: o,
        removeClass: r,
        toggleClass: i,
        has: n,
        add: o,
        remove: r,
        toggle: i
    };
    "function" == typeof define && define.amd ? define(s) : t.classie = s
})(window),
function(t) {
    "use strict";
    function e(e) {
        var i = t.event;
        return i.target = i.target || i.srcElement || e,
        i
    }
    var i = document.documentElement,
    n = function() {};
    i.addEventListener ? n = function(t, e, i) {
        t.addEventListener(e, i, !1)
    }: i.attachEvent && (n = function(t, i, n) {
        t[i + n] = n.handleEvent ?
        function() {
            var i = e(t);
            n.handleEvent.call(n, i)
        }: function() {
            var i = e(t);
            n.call(t, i)
        },
        t.attachEvent("on" + i, t[i + n])
    });
    var o = function() {};
    i.removeEventListener ? o = function(t, e, i) {
        t.removeEventListener(e, i, !1)
    }: i.detachEvent && (o = function(t, e, i) {
        t.detachEvent("on" + e, t[e + i]);
        try {
            delete t[e + i]
        } catch(n) {
            t[e + i] = void 0
        }
    });
    var r = {
        bind: n,
        unbind: o
    };
    "function" == typeof define && define.amd ? define(r) : "object" == typeof exports ? module.exports = r: t.eventie = r
} (this),
function(t) {
    "use strict";
    function e(t) {
        "function" == typeof t && (e.isReady ? t() : r.push(t))
    }
    function i(t) {
        var i = "readystatechange" === t.type && "complete" !== o.readyState;
        if (!e.isReady && !i) {
            e.isReady = !0;
            for (var n = 0,
            s = r.length; s > n; n++) {
                var a = r[n];
                a()
            }
        }
    }
    function n(n) {
        return n.bind(o, "DOMContentLoaded", i),
        n.bind(o, "readystatechange", i),
        n.bind(t, "load", i),
        e
    }
    var o = t.document,
    r = [];
    e.isReady = !1,
    "function" == typeof define && define.amd ? (e.isReady = "function" == typeof requirejs, define(["eventie/eventie"], n)) : t.docReady = n(t.eventie)
} (this),
function(t) {
    "use strict";
    function e(t) {
        if (t) {
            if ("string" == typeof n[t]) return t;
            t = t.charAt(0).toUpperCase() + t.slice(1);
            for (var e, o = 0,
            r = i.length; r > o; o++) if (e = i[o] + t, "string" == typeof n[e]) return e
        }
    }
    var i = "Webkit Moz ms Ms O".split(" "),
    n = document.documentElement.style;
    "function" == typeof define && define.amd ? define(function() {
        return e
    }) : "object" == typeof exports ? module.exports = e: t.getStyleProperty = e
} (window),
function() {
    "use strict";
    function t() {}
    function e(t, e) {
        for (var i = t.length; i--;) if (t[i].listener === e) return i;
        return - 1
    }
    function i(t) {
        return function() {
            return this[t].apply(this, arguments)
        }
    }
    var n = t.prototype,
    o = this,
    r = o.EventEmitter;
    n.getListeners = function(t) {
        var e, i, n = this._getEvents();
        if (t instanceof RegExp) {
            e = {};
            for (i in n) n.hasOwnProperty(i) && t.test(i) && (e[i] = n[i])
        } else e = n[t] || (n[t] = []);
        return e
    },
    n.flattenListeners = function(t) {
        var e, i = [];
        for (e = 0; t.length > e; e += 1) i.push(t[e].listener);
        return i
    },
    n.getListenersAsObject = function(t) {
        var e, i = this.getListeners(t);
        return i instanceof Array && (e = {},
        e[t] = i),
        e || i
    },
    n.addListener = function(t, i) {
        var n, o = this.getListenersAsObject(t),
        r = "object" == typeof i;
        for (n in o) o.hasOwnProperty(n) && -1 === e(o[n], i) && o[n].push(r ? i: {
            listener: i,
            once: !1
        });
        return this
    },
    n.on = i("addListener"),
    n.addOnceListener = function(t, e) {
        return this.addListener(t, {
            listener: e,
            once: !0
        })
    },
    n.once = i("addOnceListener"),
    n.defineEvent = function(t) {
        return this.getListeners(t),
        this
    },
    n.defineEvents = function(t) {
        for (var e = 0; t.length > e; e += 1) this.defineEvent(t[e]);
        return this
    },
    n.removeListener = function(t, i) {
        var n, o, r = this.getListenersAsObject(t);
        for (o in r) r.hasOwnProperty(o) && (n = e(r[o], i), -1 !== n && r[o].splice(n, 1));
        return this
    },
    n.off = i("removeListener"),
    n.addListeners = function(t, e) {
        return this.manipulateListeners(!1, t, e)
    },
    n.removeListeners = function(t, e) {
        return this.manipulateListeners(!0, t, e)
    },
    n.manipulateListeners = function(t, e, i) {
        var n, o, r = t ? this.removeListener: this.addListener,
        s = t ? this.removeListeners: this.addListeners;
        if ("object" != typeof e || e instanceof RegExp) for (n = i.length; n--;) r.call(this, e, i[n]);
        else for (n in e) e.hasOwnProperty(n) && (o = e[n]) && ("function" == typeof o ? r.call(this, n, o) : s.call(this, n, o));
        return this
    },
    n.removeEvent = function(t) {
        var e, i = typeof t,
        n = this._getEvents();
        if ("string" === i) delete n[t];
        else if (t instanceof RegExp) for (e in n) n.hasOwnProperty(e) && t.test(e) && delete n[e];
        else delete this._events;
        return this
    },
    n.removeAllListeners = i("removeEvent"),
    n.emitEvent = function(t, e) {
        var i, n, o, r, s = this.getListenersAsObject(t);
        for (o in s) if (s.hasOwnProperty(o)) for (n = s[o].length; n--;) i = s[o][n],
        i.once === !0 && this.removeListener(t, i.listener),
        r = i.listener.apply(this, e || []),
        r === this._getOnceReturnValue() && this.removeListener(t, i.listener);
        return this
    },
    n.trigger = i("emitEvent"),
    n.emit = function(t) {
        var e = Array.prototype.slice.call(arguments, 1);
        return this.emitEvent(t, e)
    },
    n.setOnceReturnValue = function(t) {
        return this._onceReturnValue = t,
        this
    },
    n._getOnceReturnValue = function() {
        return this.hasOwnProperty("_onceReturnValue") ? this._onceReturnValue: !0
    },
    n._getEvents = function() {
        return this._events || (this._events = {})
    },
    t.noConflict = function() {
        return o.EventEmitter = r,
        t
    },
    "function" == typeof define && define.amd ? define(function() {
        return t
    }) : "object" == typeof module && module.exports ? module.exports = t: this.EventEmitter = t
}.call(this),
function(t, e) {
    "use strict";
    "function" == typeof define && define.amd ? define(["eventEmitter/EventEmitter", "eventie/eventie"],
    function(i, n) {
        return e(t, i, n)
    }) : "object" == typeof exports ? module.exports = e(t, require("eventEmitter"), require("eventie")) : t.imagesLoaded = e(t, t.EventEmitter, t.eventie)
} (this,
function(t, e, i) {
    "use strict";
    function n(t, e) {
        for (var i in e) t[i] = e[i];
        return t
    }
    function o(t) {
        return "[object Array]" === d.call(t)
    }
    function r(t) {
        var e = [];
        if (o(t)) e = t;
        else if ("number" == typeof t.length) for (var i = 0,
        n = t.length; n > i; i++) e.push(t[i]);
        else e.push(t);
        return e
    }
    function s(t, e, i) {
        if (! (this instanceof s)) return new s(t, e);
        "string" == typeof t && (t = document.querySelectorAll(t)),
        this.elements = r(t),
        this.options = n({},
        this.options),
        "function" == typeof e ? i = e: n(this.options, e),
        i && this.on("always", i),
        this.getImages(),
        h && (this.jqDeferred = new h.Deferred);
        var o = this;
        setTimeout(function() {
            o.check()
        })
    }
    function a(t) {
        this.img = t
    }
    function u(t) {
        this.src = t,
        l[t] = this
    }
    var h = t.jQuery,
    c = t.console,
    p = c !== void 0,
    d = Object.prototype.toString;
    s.prototype = new e,
    s.prototype.options = {},
    s.prototype.getImages = function() {
        this.images = [];
        for (var t = 0,
        e = this.elements.length; e > t; t++) {
            var i = this.elements[t];
            "IMG" === i.nodeName && this.addImage(i);
            for (var n = i.querySelectorAll("img"), o = 0, r = n.length; r > o; o++) {
                var s = n[o];
                this.addImage(s)
            }
        }
    },
    s.prototype.addImage = function(t) {
        var e = new a(t);
        this.images.push(e)
    },
    s.prototype.check = function() {
        function t(t, o) {
            return e.options.debug && p && c.log("confirm", t, o),
            e.progress(t),
            i++,
            i === n && e.complete(),
            !0
        }
        var e = this,
        i = 0,
        n = this.images.length;
        if (this.hasAnyBroken = !1, !n) return this.complete(),
        void 0;
        for (var o = 0; n > o; o++) {
            var r = this.images[o];
            r.on("confirm", t),
            r.check()
        }
    },
    s.prototype.progress = function(t) {
        this.hasAnyBroken = this.hasAnyBroken || !t.isLoaded;
        var e = this;
        setTimeout(function() {
            e.emit("progress", e, t),
            e.jqDeferred && e.jqDeferred.notify && e.jqDeferred.notify(e, t)
        })
    },
    s.prototype.complete = function() {
        var t = this.hasAnyBroken ? "fail": "done";
        this.isComplete = !0;
        var e = this;
        setTimeout(function() {
            if (e.emit(t, e), e.emit("always", e), e.jqDeferred) {
                var i = e.hasAnyBroken ? "reject": "resolve";
                e.jqDeferred[i](e)
            }
        })
    },
    h && (h.fn.imagesLoaded = function(t, e) {
        var i = new s(this, t, e);
        return i.jqDeferred.promise(h(this))
    }),
    a.prototype = new e,
    a.prototype.check = function() {
        var t = l[this.img.src] || new u(this.img.src);
        if (t.isConfirmed) return this.confirm(t.isLoaded, "cached was confirmed"),
        void 0;
        if (this.img.complete && void 0 !== this.img.naturalWidth) return this.confirm(0 !== this.img.naturalWidth, "naturalWidth"),
        void 0;
        var e = this;
        t.on("confirm",
        function(t, i) {
            return e.confirm(t.isLoaded, i),
            !0
        }),
        t.check()
    },
    a.prototype.confirm = function(t, e) {
        this.isLoaded = t,
        this.emit("confirm", this, e)
    };
    var l = {};
    return u.prototype = new e,
    u.prototype.check = function() {
        if (!this.isChecked) {
            var t = new Image;
            i.bind(t, "load", this),
            i.bind(t, "error", this),
            t.src = this.src,
            this.isChecked = !0
        }
    },
    u.prototype.handleEvent = function(t) {
        var e = "on" + t.type;
        this[e] && this[e](t)
    },
    u.prototype.onload = function(t) {
        this.confirm(!0, "onload"),
        this.unbindProxyEvents(t)
    },
    u.prototype.onerror = function(t) {
        this.confirm(!1, "onerror"),
        this.unbindProxyEvents(t)
    },
    u.prototype.confirm = function(t, e) {
        this.isConfirmed = !0,
        this.isLoaded = t,
        this.emit("confirm", this, e)
    },
    u.prototype.unbindProxyEvents = function(t) {
        i.unbind(t.target, "load", this),
        i.unbind(t.target, "error", this)
    },
    s
}),
function(t) {
    "use strict";
    function e() {}
    function i(t) {
        function i(e) {
            e.prototype.option || (e.prototype.option = function(e) {
                t.isPlainObject(e) && (this.options = t.extend(!0, this.options, e))
            })
        }
        function o(e, i) {
            t.fn[e] = function(o) {
                if ("string" == typeof o) {
                    for (var s = n.call(arguments, 1), a = 0, u = this.length; u > a; a++) {
                        var h = this[a],
                        c = t.data(h, e);
                        if (c) if (t.isFunction(c[o]) && "_" !== o.charAt(0)) {
                            var p = c[o].apply(c, s);
                            if (void 0 !== p) return p
                        } else r("no such method '" + o + "' for " + e + " instance");
                        else r("cannot call methods on " + e + " prior to initialization; " + "attempted to call '" + o + "'")
                    }
                    return this
                }
                return this.each(function() {
                    var n = t.data(this, e);
                    n ? (n.option(o), n._init()) : (n = new i(this, o), t.data(this, e, n))
                })
            }
        }
        if (t) {
            var r = "undefined" == typeof console ? e: function(t) {
                console.error(t)
            };
            return t.bridget = function(t, e) {
                i(e),
                o(t, e)
            },
            t.bridget
        }
    }
    var n = Array.prototype.slice;
    "function" == typeof define && define.amd ? define(["jquery"], i) : i(t.jQuery)
} (window),
function(t) {
    "use strict";
    function e(t) {
        var e = parseFloat(t),
        i = -1 === t.indexOf("%") && !isNaN(e);
        return i && e
    }
    function i() {
        for (var t = {
            width: 0,
            height: 0,
            innerWidth: 0,
            innerHeight: 0,
            outerWidth: 0,
            outerHeight: 0
        },
        e = 0, i = s.length; i > e; e++) {
            var n = s[e];
            t[n] = 0
        }
        return t
    }
    function n(t) {
        function n(t) {
            if ("string" == typeof t && (t = document.querySelector(t)), t && "object" == typeof t && t.nodeType) {
                var n = r(t);
                if ("none" === n.display) return i();
                var o = {};
                o.width = t.offsetWidth,
                o.height = t.offsetHeight;
                for (var c = o.isBorderBox = !(!h || !n[h] || "border-box" !== n[h]), p = 0, d = s.length; d > p; p++) {
                    var l = s[p],
                    f = n[l];
                    f = a(t, f);
                    var m = parseFloat(f);
                    o[l] = isNaN(m) ? 0 : m
                }
                var y = o.paddingLeft + o.paddingRight,
                g = o.paddingTop + o.paddingBottom,
                v = o.marginLeft + o.marginRight,
                b = o.marginTop + o.marginBottom,
                E = o.borderLeftWidth + o.borderRightWidth,
                w = o.borderTopWidth + o.borderBottomWidth,
                L = c && u,
                S = e(n.width);
                S !== !1 && (o.width = S + (L ? 0 : y + E));
                var x = e(n.height);
                return x !== !1 && (o.height = x + (L ? 0 : g + w)),
                o.innerWidth = o.width - (y + E),
                o.innerHeight = o.height - (g + w),
                o.outerWidth = o.width + v,
                o.outerHeight = o.height + b,
                o
            }
        }
        function a(t, e) {
            if (o || -1 === e.indexOf("%")) return e;
            var i = t.style,
            n = i.left,
            r = t.runtimeStyle,
            s = r && r.left;
            return s && (r.left = t.currentStyle.left),
            i.left = e,
            e = i.pixelLeft,
            i.left = n,
            s && (r.left = s),
            e
        }
        var u, h = t("boxSizing");
        return function() {
            if (h) {
                var t = document.createElement("div");
                t.style.width = "200px",
                t.style.padding = "1px 2px 3px 4px",
                t.style.borderStyle = "solid",
                t.style.borderWidth = "1px 2px 3px 4px",
                t.style[h] = "border-box";
                var i = document.body || document.documentElement;
                i.appendChild(t);
                var n = r(t);
                u = 200 === e(n.width),
                i.removeChild(t)
            }
        } (),
        n
    }
    var o = t.getComputedStyle,
    r = o ?
    function(t) {
        return o(t, null)
    }: function(t) {
        return t.currentStyle
    },
    s = ["paddingLeft", "paddingRight", "paddingTop", "paddingBottom", "marginLeft", "marginRight", "marginTop", "marginBottom", "borderLeftWidth", "borderRightWidth", "borderTopWidth", "borderBottomWidth"];
    "function" == typeof define && define.amd ? define(["get-style-property/get-style-property"], n) : "object" == typeof exports ? module.exports = n(require("get-style-property")) : t.getSize = n(t.getStyleProperty)
} (window),
function(t, e) {
    "use strict";
    function i(t, e) {
        return t[a](e)
    }
    function n(t) {
        if (!t.parentNode) {
            var e = document.createDocumentFragment();
            e.appendChild(t)
        }
    }
    function o(t, e) {
        n(t);
        for (var i = t.parentNode.querySelectorAll(e), o = 0, r = i.length; r > o; o++) if (i[o] === t) return ! 0;
        return ! 1
    }
    function r(t, e) {
        return n(t),
        i(t, e)
    }
    var s, a = function() {
        if (e.matchesSelector) return "matchesSelector";
        for (var t = ["webkit", "moz", "ms", "o"], i = 0, n = t.length; n > i; i++) {
            var o = t[i],
            r = o + "MatchesSelector";
            if (e[r]) return r
        }
    } ();
    if (a) {
        var u = document.createElement("div"),
        h = i(u, "div");
        s = h ? i: r
    } else s = o;
    "function" == typeof define && define.amd ? define(function() {
        return s
    }) : window.matchesSelector = s
} (this, Element.prototype),
function(t) {
    "use strict";
    function e(t, e) {
        for (var i in e) t[i] = e[i];
        return t
    }
    function i(t) {
        for (var e in t) return ! 1;
        return e = null,
        !0
    }
    function n(t) {
        return t.replace(/([A-Z])/g,
        function(t) {
            return "-" + t.toLowerCase()
        })
    }
    function o(t, o, r) {
        function a(t, e) {
            t && (this.element = t, this.layout = e, this.position = {
                x: 0,
                y: 0
            },
            this._create())
        }
        var u = r("transition"),
        h = r("transform"),
        c = u && h,
        p = !!r("perspective"),
        d = {
            WebkitTransition: "webkitTransitionEnd",
            MozTransition: "transitionend",
            OTransition: "otransitionend",
            transition: "transitionend"
        } [u],
        l = ["transform", "transition", "transitionDuration", "transitionProperty"],
        f = function() {
            for (var t = {},
            e = 0,
            i = l.length; i > e; e++) {
                var n = l[e],
                o = r(n);
                o && o !== n && (t[n] = o)
            }
            return t
        } ();
        e(a.prototype, t.prototype),
        a.prototype._create = function() {
            this._transn = {
                ingProperties: {},
                clean: {},
                onEnd: {}
            },
            this.css({
                position: "absolute"
            })
        },
        a.prototype.handleEvent = function(t) {
            var e = "on" + t.type;
            this[e] && this[e](t)
        },
        a.prototype.getSize = function() {
            this.size = o(this.element)
        },
        a.prototype.css = function(t) {
            var e = this.element.style;
            for (var i in t) {
                var n = f[i] || i;
                e[n] = t[i]
            }
        },
        a.prototype.getPosition = function() {
            var t = s(this.element),
            e = this.layout.options,
            i = e.isOriginLeft,
            n = e.isOriginTop,
            o = parseInt(t[i ? "left": "right"], 10),
            r = parseInt(t[n ? "top": "bottom"], 10);
            o = isNaN(o) ? 0 : o,
            r = isNaN(r) ? 0 : r;
            var a = this.layout.size;
            o -= i ? a.paddingLeft: a.paddingRight,
            r -= n ? a.paddingTop: a.paddingBottom,
            this.position.x = o,
            this.position.y = r
        },
        a.prototype.layoutPosition = function() {
            var t = this.layout.size,
            e = this.layout.options,
            i = {};
            e.isOriginLeft ? (i.left = this.position.x + t.paddingLeft + "px", i.right = "") : (i.right = this.position.x + t.paddingRight + "px", i.left = ""),
            e.isOriginTop ? (i.top = this.position.y + t.paddingTop + "px", i.bottom = "") : (i.bottom = this.position.y + t.paddingBottom + "px", i.top = ""),
            this.css(i),
            this.emitEvent("layout", [this])
        };
        var m = p ?
        function(t, e) {
            return "translate3d(" + t + "px, " + e + "px, 0)"
        }: function(t, e) {
            return "translate(" + t + "px, " + e + "px)"
        };
        a.prototype._transitionTo = function(t, e) {
            this.getPosition();
            var i = this.position.x,
            n = this.position.y,
            o = parseInt(t, 10),
            r = parseInt(e, 10),
            s = o === this.position.x && r === this.position.y;
            if (this.setPosition(t, e), s && !this.isTransitioning) return this.layoutPosition(),
            void 0;
            var a = t - i,
            u = e - n,
            h = {},
            c = this.layout.options;
            a = c.isOriginLeft ? a: -a,
            u = c.isOriginTop ? u: -u,
            h.transform = m(a, u),
            this.transition({
                to: h,
                onTransitionEnd: {
                    transform: this.layoutPosition
                },
                isCleaning: !0
            })
        },
        a.prototype.goTo = function(t, e) {
            this.setPosition(t, e),
            this.layoutPosition()
        },
        a.prototype.moveTo = c ? a.prototype._transitionTo: a.prototype.goTo,
        a.prototype.setPosition = function(t, e) {
            this.position.x = parseInt(t, 10),
            this.position.y = parseInt(e, 10)
        },
        a.prototype._nonTransition = function(t) {
            this.css(t.to),
            t.isCleaning && this._removeStyles(t.to);
            for (var e in t.onTransitionEnd) t.onTransitionEnd[e].call(this)
        },
        a.prototype._transition = function(t) {
            if (!parseFloat(this.layout.options.transitionDuration)) return this._nonTransition(t),
            void 0;
            var e = this._transn;
            for (var i in t.onTransitionEnd) e.onEnd[i] = t.onTransitionEnd[i];
            for (i in t.to) e.ingProperties[i] = !0,
            t.isCleaning && (e.clean[i] = !0);
            if (t.from) {
                this.css(t.from);
                var n = this.element.offsetHeight;
                n = null
            }
            this.enableTransition(t.to),
            this.css(t.to),
            this.isTransitioning = !0
        };
        var y = h && n(h) + ",opacity";
        a.prototype.enableTransition = function() {
            this.isTransitioning || (this.css({
                transitionProperty: y,
                transitionDuration: this.layout.options.transitionDuration
            }), this.element.addEventListener(d, this, !1))
        },
        a.prototype.transition = a.prototype[u ? "_transition": "_nonTransition"],
        a.prototype.onwebkitTransitionEnd = function(t) {
            this.ontransitionend(t)
        },
        a.prototype.onotransitionend = function(t) {
            this.ontransitionend(t)
        };
        var g = {
            "-webkit-transform": "transform",
            "-moz-transform": "transform",
            "-o-transform": "transform"
        };
        a.prototype.ontransitionend = function(t) {
            if (t.target === this.element) {
                var e = this._transn,
                n = g[t.propertyName] || t.propertyName;
                if (delete e.ingProperties[n], i(e.ingProperties) && this.disableTransition(), n in e.clean && (this.element.style[t.propertyName] = "", delete e.clean[n]), n in e.onEnd) {
                    var o = e.onEnd[n];
                    o.call(this),
                    delete e.onEnd[n]
                }
                this.emitEvent("transitionEnd", [this])
            }
        },
        a.prototype.disableTransition = function() {
            this.removeTransitionStyles(),
            this.element.removeEventListener(d, this, !1),
            this.isTransitioning = !1
        },
        a.prototype._removeStyles = function(t) {
            var e = {};
            for (var i in t) e[i] = "";
            this.css(e)
        };
        var v = {
            transitionProperty: "",
            transitionDuration: ""
        };
        return a.prototype.removeTransitionStyles = function() {
            this.css(v)
        },
        a.prototype.removeElem = function() {
            this.element.parentNode.removeChild(this.element),
            this.emitEvent("remove", [this])
        },
        a.prototype.remove = function() {
            if (!u || !parseFloat(this.layout.options.transitionDuration)) return this.removeElem(),
            void 0;
            var t = this;
            this.on("transitionEnd",
            function() {
                return t.removeElem(),
                !0
            }),
            this.hide()
        },
        a.prototype.reveal = function() {
            delete this.isHidden,
            this.css({
                display: ""
            });
            var t = this.layout.options;
            this.transition({
                from: t.hiddenStyle,
                to: t.visibleStyle,
                isCleaning: !0
            })
        },
        a.prototype.hide = function() {
            this.isHidden = !0,
            this.css({
                display: ""
            });
            var t = this.layout.options;
            this.transition({
                from: t.visibleStyle,
                to: t.hiddenStyle,
                isCleaning: !0,
                onTransitionEnd: {
                    opacity: function() {
                        this.isHidden && this.css({
                            display: "none"
                        })
                    }
                }
            })
        },
        a.prototype.destroy = function() {
            this.css({
                position: "",
                left: "",
                right: "",
                top: "",
                bottom: "",
                transition: "",
                transform: ""
            })
        },
        a
    }
    var r = t.getComputedStyle,
    s = r ?
    function(t) {
        return r(t, null)
    }: function(t) {
        return t.currentStyle
    };
    "function" == typeof define && define.amd ? define(["eventEmitter/EventEmitter", "get-size/get-size", "get-style-property/get-style-property"], o) : (t.Outlayer = {},
    t.Outlayer.Item = o(t.EventEmitter, t.getSize, t.getStyleProperty))
} (window),
function(t) {
    "use strict";
    function e(t, e) {
        for (var i in e) t[i] = e[i];
        return t
    }
    function i(t) {
        return "[object Array]" === p.call(t)
    }
    function n(t) {
        var e = [];
        if (i(t)) e = t;
        else if (t && "number" == typeof t.length) for (var n = 0,
        o = t.length; o > n; n++) e.push(t[n]);
        else e.push(t);
        return e
    }
    function o(t, e) {
        var i = l(e, t); - 1 !== i && e.splice(i, 1)
    }
    function r(t) {
        return t.replace(/(.)([A-Z])/g,
        function(t, e, i) {
            return e + "-" + i
        }).toLowerCase()
    }
    function s(i, s, p, l, f, m) {
        function y(t, i) {
            if ("string" == typeof t && (t = a.querySelector(t)), !t || !d(t)) return u && u.error("Bad " + this.constructor.namespace + " element: " + t),
            void 0;
            this.element = t,
            this.options = e({},
            this.constructor.defaults),
            this.option(i);
            var n = ++g;
            this.element.outlayerGUID = n,
            v[n] = this,
            this._create(),
            this.options.isInitLayout && this.layout()
        }
        var g = 0,
        v = {};
        return y.namespace = "outlayer",
        y.Item = m,
        y.defaults = {
            containerStyle: {
                position: "relative"
            },
            isInitLayout: !0,
            isOriginLeft: !0,
            isOriginTop: !0,
            isResizeBound: !0,
            isResizingContainer: !0,
            transitionDuration: "0.4s",
            hiddenStyle: {
                opacity: 0,
                transform: "scale(0.001)"
            },
            visibleStyle: {
                opacity: 1,
                transform: "scale(1)"
            }
        },
        e(y.prototype, p.prototype),
        y.prototype.option = function(t) {
            e(this.options, t)
        },
        y.prototype._create = function() {
            this.reloadItems(),
            this.stamps = [],
            this.stamp(this.options.stamp),
            e(this.element.style, this.options.containerStyle),
            this.options.isResizeBound && this.bindResize()
        },
        y.prototype.reloadItems = function() {
            this.items = this._itemize(this.element.children)
        },
        y.prototype._itemize = function(t) {
            for (var e = this._filterFindItemElements(t), i = this.constructor.Item, n = [], o = 0, r = e.length; r > o; o++) {
                var s = e[o],
                a = new i(s, this);
                n.push(a)
            }
            return n
        },
        y.prototype._filterFindItemElements = function(t) {
            t = n(t);
            for (var e = this.options.itemSelector,
            i = [], o = 0, r = t.length; r > o; o++) {
                var s = t[o];
                if (d(s)) if (e) {
                    f(s, e) && i.push(s);
                    for (var a = s.querySelectorAll(e), u = 0, h = a.length; h > u; u++) i.push(a[u])
                } else i.push(s)
            }
            return i
        },
        y.prototype.getItemElements = function() {
            for (var t = [], e = 0, i = this.items.length; i > e; e++) t.push(this.items[e].element);
            return t
        },
        y.prototype.layout = function() {
            this._resetLayout(),
            this._manageStamps();
            var t = void 0 !== this.options.isLayoutInstant ? this.options.isLayoutInstant: !this._isLayoutInited;
            this.layoutItems(this.items, t),
            this._isLayoutInited = !0
        },
        y.prototype._init = y.prototype.layout,
        y.prototype._resetLayout = function() {
            this.getSize()
        },
        y.prototype.getSize = function() {
            this.size = l(this.element)
        },
        y.prototype._getMeasurement = function(t, e) {
            var i, n = this.options[t];
            n ? ("string" == typeof n ? i = this.element.querySelector(n) : d(n) && (i = n), this[t] = i ? l(i)[e] : n) : this[t] = 0
        },
        y.prototype.layoutItems = function(t, e) {
            t = this._getItemsForLayout(t),
            this._layoutItems(t, e),
            this._postLayout()
        },
        y.prototype._getItemsForLayout = function(t) {
            for (var e = [], i = 0, n = t.length; n > i; i++) {
                var o = t[i];
                o.isIgnored || e.push(o)
            }
            return e
        },
        y.prototype._layoutItems = function(t, e) {
            function i() {
                n.emitEvent("layoutComplete", [n, t])
            }
            var n = this;
            if (!t || !t.length) return i(),
            void 0;
            this._itemsOn(t, "layout", i);
            for (var o = [], r = 0, s = t.length; s > r; r++) {
                var a = t[r],
                u = this._getItemLayoutPosition(a);
                u.item = a,
                u.isInstant = e || a.isLayoutInstant,
                o.push(u)
            }
            this._processLayoutQueue(o)
        },
        y.prototype._getItemLayoutPosition = function() {
            return {
                x: 0,
                y: 0
            }
        },
        y.prototype._processLayoutQueue = function(t) {
            for (var e = 0,
            i = t.length; i > e; e++) {
                var n = t[e];
                this._positionItem(n.item, n.x, n.y, n.isInstant)
            }
        },
        y.prototype._positionItem = function(t, e, i, n) {
            n ? t.goTo(e, i) : t.moveTo(e, i)
        },
        y.prototype._postLayout = function() {
            this.resizeContainer()
        },
        y.prototype.resizeContainer = function() {
            if (this.options.isResizingContainer) {
                var t = this._getContainerSize();
                t && (this._setContainerMeasure(t.width, !0), this._setContainerMeasure(t.height, !1))
            }
        },
        y.prototype._getContainerSize = c,
        y.prototype._setContainerMeasure = function(t, e) {
            if (void 0 !== t) {
                var i = this.size;
                i.isBorderBox && (t += e ? i.paddingLeft + i.paddingRight + i.borderLeftWidth + i.borderRightWidth: i.paddingBottom + i.paddingTop + i.borderTopWidth + i.borderBottomWidth),
                t = Math.max(t, 0),
                this.element.style[e ? "width": "height"] = t + "px"
            }
        },
        y.prototype._itemsOn = function(t, e, i) {
            function n() {
                return o++,
                o === r && i.call(s),
                !0
            }
            for (var o = 0,
            r = t.length,
            s = this,
            a = 0,
            u = t.length; u > a; a++) {
                var h = t[a];
                h.on(e, n)
            }
        },
        y.prototype.ignore = function(t) {
            var e = this.getItem(t);
            e && (e.isIgnored = !0)
        },
        y.prototype.unignore = function(t) {
            var e = this.getItem(t);
            e && delete e.isIgnored
        },
        y.prototype.stamp = function(t) {
            if (t = this._find(t)) {
                this.stamps = this.stamps.concat(t);
                for (var e = 0,
                i = t.length; i > e; e++) {
                    var n = t[e];
                    this.ignore(n)
                }
            }
        },
        y.prototype.unstamp = function(t) {
            if (t = this._find(t)) for (var e = 0,
            i = t.length; i > e; e++) {
                var n = t[e];
                o(n, this.stamps),
                this.unignore(n)
            }
        },
        y.prototype._find = function(t) {
            return t ? ("string" == typeof t && (t = this.element.querySelectorAll(t)), t = n(t)) : void 0
        },
        y.prototype._manageStamps = function() {
            if (this.stamps && this.stamps.length) {
                this._getBoundingRect();
                for (var t = 0,
                e = this.stamps.length; e > t; t++) {
                    var i = this.stamps[t];
                    this._manageStamp(i)
                }
            }
        },
        y.prototype._getBoundingRect = function() {
            var t = this.element.getBoundingClientRect(),
            e = this.size;
            this._boundingRect = {
                left: t.left + e.paddingLeft + e.borderLeftWidth,
                top: t.top + e.paddingTop + e.borderTopWidth,
                right: t.right - (e.paddingRight + e.borderRightWidth),
                bottom: t.bottom - (e.paddingBottom + e.borderBottomWidth)
            }
        },
        y.prototype._manageStamp = c,
        y.prototype._getElementOffset = function(t) {
            var e = t.getBoundingClientRect(),
            i = this._boundingRect,
            n = l(t),
            o = {
                left: e.left - i.left - n.marginLeft,
                top: e.top - i.top - n.marginTop,
                right: i.right - e.right - n.marginRight,
                bottom: i.bottom - e.bottom - n.marginBottom
            };
            return o
        },
        y.prototype.handleEvent = function(t) {
            var e = "on" + t.type;
            this[e] && this[e](t)
        },
        y.prototype.bindResize = function() {
            this.isResizeBound || (i.bind(t, "resize", this), this.isResizeBound = !0)
        },
        y.prototype.unbindResize = function() {
            this.isResizeBound && i.unbind(t, "resize", this),
            this.isResizeBound = !1
        },
        y.prototype.onresize = function() {
            function t() {
                e.resize(),
                delete e.resizeTimeout
            }
            this.resizeTimeout && clearTimeout(this.resizeTimeout);
            var e = this;
            this.resizeTimeout = setTimeout(t, 100)
        },
        y.prototype.resize = function() {
            this.isResizeBound && this.needsResizeLayout() && this.layout()
        },
        y.prototype.needsResizeLayout = function() {
            var t = l(this.element),
            e = this.size && t;
            return e && t.innerWidth !== this.size.innerWidth
        },
        y.prototype.addItems = function(t) {
            var e = this._itemize(t);
            return e.length && (this.items = this.items.concat(e)),
            e
        },
        y.prototype.appended = function(t) {
            var e = this.addItems(t);
            e.length && (this.layoutItems(e, !0), this.reveal(e))
        },
        y.prototype.prepended = function(t) {
            var e = this._itemize(t);
            if (e.length) {
                var i = this.items.slice(0);
                this.items = e.concat(i),
                this._resetLayout(),
                this._manageStamps(),
                this.layoutItems(e, !0),
                this.reveal(e),
                this.layoutItems(i)
            }
        },
        y.prototype.reveal = function(t) {
            var e = t && t.length;
            if (e) for (var i = 0; e > i; i++) {
                var n = t[i];
                n.reveal()
            }
        },
        y.prototype.hide = function(t) {
            var e = t && t.length;
            if (e) for (var i = 0; e > i; i++) {
                var n = t[i];
                n.hide()
            }
        },
        y.prototype.getItem = function(t) {
            for (var e = 0,
            i = this.items.length; i > e; e++) {
                var n = this.items[e];
                if (n.element === t) return n
            }
        },
        y.prototype.getItems = function(t) {
            if (t && t.length) {
                for (var e = [], i = 0, n = t.length; n > i; i++) {
                    var o = t[i],
                    r = this.getItem(o);
                    r && e.push(r)
                }
                return e
            }
        },
        y.prototype.remove = function(t) {
            t = n(t);
            var e = this.getItems(t);
            if (e && e.length) {
                this._itemsOn(e, "remove",
                function() {
                    this.emitEvent("removeComplete", [this, e])
                });
                for (var i = 0,
                r = e.length; r > i; i++) {
                    var s = e[i];
                    s.remove(),
                    o(s, this.items)
                }
            }
        },
        y.prototype.destroy = function() {
            var t = this.element.style;
            t.height = "",
            t.position = "",
            t.width = "";
            for (var e = 0,
            i = this.items.length; i > e; e++) {
                var n = this.items[e];
                n.destroy()
            }
            this.unbindResize(),
            delete this.element.outlayerGUID,
            h && h.removeData(this.element, this.constructor.namespace)
        },
        y.data = function(t) {
            var e = t && t.outlayerGUID;
            return e && v[e]
        },
        y.create = function(t, i) {
            function n() {
                y.apply(this, arguments)
            }
            return Object.create ? n.prototype = Object.create(y.prototype) : e(n.prototype, y.prototype),
            n.prototype.constructor = n,
            n.defaults = e({},
            y.defaults),
            e(n.defaults, i),
            n.prototype.settings = {},
            n.namespace = t,
            n.data = y.data,
            n.Item = function() {
                m.apply(this, arguments)
            },
            n.Item.prototype = new m,
            s(function() {
                for (var e = r(t), i = a.querySelectorAll(".js-" + e), o = "data-" + e + "-options", s = 0, c = i.length; c > s; s++) {
                    var p, d = i[s],
                    l = d.getAttribute(o);
                    try {
                        p = l && JSON.parse(l)
                    } catch(f) {
                        u && u.error("Error parsing " + o + " on " + d.nodeName.toLowerCase() + (d.id ? "#" + d.id: "") + ": " + f);
                        continue
                    }
                    var m = new n(d, p);
                    h && h.data(d, t, m)
                }
            }),
            h && h.bridget && h.bridget(t, n),
            n
        },
        y.Item = m,
        y
    }
    var a = t.document,
    u = t.console,
    h = t.jQuery,
    c = function() {},
    p = Object.prototype.toString,
    d = "object" == typeof HTMLElement ?
    function(t) {
        return t instanceof HTMLElement
    }: function(t) {
        return t && "object" == typeof t && 1 === t.nodeType && "string" == typeof t.nodeName
    },
    l = Array.prototype.indexOf ?
    function(t, e) {
        return t.indexOf(e)
    }: function(t, e) {
        for (var i = 0,
        n = t.length; n > i; i++) if (t[i] === e) return i;
        return - 1
    };
    "function" == typeof define && define.amd ? define(["eventie/eventie", "doc-ready/doc-ready", "eventEmitter/EventEmitter", "get-size/get-size", "matches-selector/matches-selector", "./item"], s) : t.Outlayer = s(t.eventie, t.docReady, t.EventEmitter, t.getSize, t.matchesSelector, t.Outlayer.Item)
} (window),
function(t) {
    "use strict";
    function e(t, e) {
        var n = t.create("masonry");
        return n.prototype._resetLayout = function() {
            this.getSize(),
            this._getMeasurement("columnWidth", "outerWidth"),
            this._getMeasurement("gutter", "outerWidth"),
            this.measureColumns();
            var t = this.cols;
            for (this.colYs = []; t--;) this.colYs.push(0);
            this.maxY = 0
        },
        n.prototype.measureColumns = function() {
            if (this.getContainerWidth(), !this.columnWidth) {
                var t = this.items[0],
                i = t && t.element;
                this.columnWidth = i && e(i).outerWidth || this.containerWidth
            }
            this.columnWidth += this.gutter,
            this.cols = Math.floor((this.containerWidth + this.gutter) / this.columnWidth),
            this.cols = Math.max(this.cols, 1)
        },
        n.prototype.getContainerWidth = function() {
            var t = this.options.isFitWidth ? this.element.parentNode: this.element,
            i = e(t);
            this.containerWidth = i && i.innerWidth
        },
        n.prototype._getItemLayoutPosition = function(t) {
            t.getSize();
            var e = t.size.outerWidth % this.columnWidth,
            n = e && 1 > e ? "round": "ceil",
            o = Math[n](t.size.outerWidth / this.columnWidth);
            o = Math.min(o, this.cols);
            for (var r = this._getColGroup(o), s = Math.min.apply(Math, r), a = i(r, s), u = {
                x: this.columnWidth * a,
                y: s
            },
            h = s + t.size.outerHeight, c = this.cols + 1 - r.length, p = 0; c > p; p++) this.colYs[a + p] = h;
            return u
        },
        n.prototype._getColGroup = function(t) {
            if (2 > t) return this.colYs;
            for (var e = [], i = this.cols + 1 - t, n = 0; i > n; n++) {
                var o = this.colYs.slice(n, n + t);
                e[n] = Math.max.apply(Math, o)
            }
            return e
        },
        n.prototype._manageStamp = function(t) {
            var i = e(t),
            n = this._getElementOffset(t),
            o = this.options.isOriginLeft ? n.left: n.right,
            r = o + i.outerWidth,
            s = Math.floor(o / this.columnWidth);
            s = Math.max(0, s);
            var a = Math.floor(r / this.columnWidth);
            a -= r % this.columnWidth ? 0 : 1,
            a = Math.min(this.cols - 1, a);
            for (var u = (this.options.isOriginTop ? n.top: n.bottom) + i.outerHeight, h = s; a >= h; h++) this.colYs[h] = Math.max(u, this.colYs[h])
        },
        n.prototype._getContainerSize = function() {
            this.maxY = Math.max.apply(Math, this.colYs);
            var t = {
                height: this.maxY
            };
            return this.options.isFitWidth && (t.width = this._getContainerFitWidth()),
            t
        },
        n.prototype._getContainerFitWidth = function() {
            for (var t = 0,
            e = this.cols; --e && 0 === this.colYs[e];) t++;
            return (this.cols - t) * this.columnWidth - this.gutter
        },
        n.prototype.needsResizeLayout = function() {
            var t = this.containerWidth;
            return this.getContainerWidth(),
            t !== this.containerWidth
        },
        n
    }
    var i = Array.prototype.indexOf ?
    function(t, e) {
        return t.indexOf(e)
    }: function(t, e) {
        for (var i = 0,
        n = t.length; n > i; i++) {
            var o = t[i];
            if (o === e) return i
        }
        return - 1
    };
    "function" == typeof define && define.amd ? define(["outlayer/outlayer", "get-size/get-size"], e) : t.Masonry = e(t.Outlayer, t.getSize)
} (window),
function(t) {
    "use strict";
    function e(t, e) {
        t[s] = e
    }
    var i = t.MD = {};
    i.pages = {};
    var n;
    docReady(function() {
        n = document.querySelector("#notification");
        var t = document.body.getAttribute("data-page");
        t && "function" == typeof i[t] && i[t]()
    }),
    i.getSomeItemElements = function() {
        for (var t = document.createDocumentFragment(), e = [], i = 0; 3 > i; i++) {
            var n = document.createElement("div"),
            o = Math.random(),
            r = o > .85 ? "w4": o > .7 ? "w2": "",
            s = Math.random(),
            a = s > .85 ? "h4": s > .7 ? "h2": "";
            n.className = "item " + r + " " + a,
            t.appendChild(n),
            e.push(n)
        }
    };
    var o, r = document.documentElement,
    s = void 0 !== r.textContent ? "textContent": "innerText",
    a = getStyleProperty("transition"),
    u = a ? 1e3: 1500;
    i.notify = function(t, r) {
        e(n, t),
        a && (n.style[a] = "none"),
        n.style.display = "block",
        n.style.opacity = "1",
        r && (o && clearTimeout(o), o = setTimeout(i.hideNotify, u))
    },
    i.hideNotify = function() {
        a ? (n.style[a] = "opacity 1.0s", n.style.opacity = "0") : n.style.display = "none"
    }
} (window),
function(t) {
    "use strict";
    function e() {
        var t = new Date,
        e = t.getMinutes();
        e = 10 > e ? "0" + e: e;
        var i = t.getSeconds();
        return i = 10 > i ? "0" + i: i,
        [t.getHours(), e, i].join(":")
    }
    function i(t) {
        n.notify(t + " at " + e(), !0)
    }
    var n = t.MD;
    n.events = function() { (function() {
            var t = document.querySelector("#layout-complete-demo .masonry"),
            e = new Masonry(t, {
                columnWidth: 60
            });
            e.on("layoutComplete",
            function(t, e) {
                i("Masonry layout completed on " + e.length + " items")
            }),
            eventie.bind(t, "click",
            function(t) {
                classie.has(t.target, "item") && (classie.toggle(t.target, "gigante"), e.layout())
            })
        })(),
        function() {
            var t = document.querySelector("#remove-complete-demo .masonry"),
            e = new Masonry(t, {
                columnWidth: 60
            });
            e.on("removeComplete",
            function(t, e) {
                i("Removed " + e.length + " items")
            }),
            eventie.bind(t, "click",
            function(t) {
                classie.has(t.target, "item") && e.remove(t.target)
            })
        } ()
    }
} (window),
function(t) {
    "use strict";
    var e = t.MD,
    i = getStyleProperty("transition"),
    n = {
        WebkitTransition: "webkitTransitionEnd",
        MozTransition: "transitionend",
        OTransition: "otransitionend",
        transition: "transitionend"
    } [i];
    e.faq = function() { (function() {
            var t = document.querySelector("#animate-item-size .masonry"),
            e = new Masonry(t, {
                columnWidth: 60
            });
            eventie.bind(t, "click",
            function(t) {
                var i = t.target;
                if (classie.has(i, "item-content")) {
                    var n = i.parentNode;
                    classie.toggleClass(n, "is-expanded"),
                    e.layout()
                }
            })
        })(),
        function() {
            var t = document.querySelector("#animate-item-size-responsive .masonry"),
            e = new Masonry(t, {
                columnWidth: ".grid-sizer",
                itemSelector: ".item"
            });
            eventie.bind(t, "click",
            function(t) {
                var o = t.target;
                if (classie.has(o, "item-content")) {
                    var r = getSize(o);
                    o.style[i] = "none",
                    o.style.width = r.width + "px",
                    o.style.height = r.height + "px";
                    var s = o.parentNode;
                    classie.toggleClass(s, "is-expanded");
                    var a = o.offsetWidth;
                    if (o.style[i] = "", i) {
                        var u = function() {
                            o.style.width = "",
                            o.style.height = "",
                            o.removeEventListener(n, u, !1)
                        };
                        o.addEventListener(n, u, !1)
                    }
                    var h = getSize(s);
                    o.style.width = h.width + "px",
                    o.style.height = h.height + "px",
                    a = null,
                    e.layout()
                }
            })
        } ()
    }
} (window),
function(t) {
    "use strict";
    function e() {
        for (var t = [], e = document.createDocumentFragment(), r = a, s = 0, u = r.length; u > s; s++) {
            var h = i(r[s]);
            t.push(h),
            e.appendChild(h)
        }
        imagesLoaded(e).on("progress",
        function(t, e) {
            var i = e.img.parentNode.parentNode;
            n.appendChild(i),
            o.appended(i)
        })
    }
    function i(t) {
        var e = document.createElement("div");
        e.className = "hero-item has-example is-hidden";
        var i = document.createElement("a");
        i.href = t.url;
        var n = document.createElement("img");
        n.src = t.image;
        var o = document.createElement("p");
        return o.className = "example-title",
        o.textContent = t.title,
        i.appendChild(n),
        i.appendChild(o),
        e.appendChild(i),
        e
    }
    var n, o, r, s = t.MD;
    s.index = function() { (function() {
            var t = document.querySelector("#hero");
            n = t.querySelector(".hero-masonry"),
            o = new Masonry(n, {
                itemSelector: ".hero-item",
                columnWidth: ".grid-sizer"
            }),
            e()
        })(),
        r = document.querySelector("#load-more-examples")
    };
    var a = [{
        title: "Erik Johansson",
        url: "http://erikjohanssonphoto.com/work/imagecats/personal/",
        image: "http://i.imgur.com/6Lo8oun.jpg"
    },
    {
        title: "Tumblr Staff: Archive",
        url: "http://staff.tumblr.com/archive",
        image: "http://i.imgur.com/igjvRa3.jpg"
    },
    {
        title: "Halcyon theme",
        url: "http://halcyon-theme.tumblr.com/",
        image: "http://i.imgur.com/A1RSOhg.jpg"
    },
    {
        title: "RESIZE.THATSH.IT",
        url: "http://resize.thatsh.it/",
        image: "http://i.imgur.com/00xWxLG.png"
    },
    {
        title: "Vox Media",
        url: "http://www.voxmedia.com",
        image: "http://i.imgur.com/xSiTFij.jpg"
    },
    {
        title: "Kristian Hammerstad",
        url: "http://www.kristianhammerstad.com/",
        image: "http://i.imgur.com/Zwd7Sch.jpg"
    },
    {
        title: "Loading Effects for Grid Items | Demo 2",
        url: "http://tympanus.net/Development/GridLoadingEffects/index2.html",
        image: "http://i.imgur.com/iFBSB1t.jpg"
    }]
} (window),
function(t) {
    "use strict";
    function e() {
        var t = document.createElement("div"),
        e = Math.random(),
        i = Math.random(),
        n = e > .92 ? "w4": e > .8 ? "w3": e > .6 ? "w2": "",
        o = i > .85 ? "h4": i > .6 ? "h3": i > .35 ? "h2": "";
        return t.className = "item " + n + " " + o,
        t
    }
    var i = t.MD;
    i.methods = function() { (function() {
            var t = document.querySelector("#appended-demo"),
            i = t.querySelector(".masonry"),
            n = t.querySelector("button"),
            o = new Masonry(i, {
                columnWidth: 60
            });
            eventie.bind(n, "click",
            function() {
                for (var t = [], n = document.createDocumentFragment(), r = 0; 3 > r; r++) {
                    var s = e();
                    n.appendChild(s),
                    t.push(s)
                }
                i.appendChild(n),
                o.appended(t)
            })
        })(),
        function() {
            var t = document.querySelector("#destroy-demo"),
            e = t.querySelector(".masonry"),
            i = t.querySelector("button"),
            n = new Masonry(e, {
                columnWidth: 60
            }),
            o = !0;
            eventie.bind(i, "click",
            function() {
                o ? n.destroy() : n = new Masonry(e),
                o = !o
            })
        } (),
        function() {
            var t = document.querySelector("#layout-demo .masonry"),
            e = new Masonry(t, {
                columnWidth: 60
            });
            eventie.bind(t, "click",
            function(t) {
                classie.has(t.target, "item") && (classie.toggle(t.target, "gigante"), e.layout())
            })
        } (),
        function() {
            var t = document.querySelector("#prepended-demo"),
            i = t.querySelector(".masonry"),
            n = t.querySelector("button"),
            o = new Masonry(i, {
                columnWidth: 60
            });
            eventie.bind(n, "click",
            function() {
                for (var t = [], n = document.createDocumentFragment(), r = 0; 3 > r; r++) {
                    var s = e();
                    n.appendChild(s),
                    t.push(s)
                }
                i.insertBefore(n, i.firstChild),
                o.prepended(t)
            })
        } (),
        function() {
            var t = document.querySelector("#stamp-demo"),
            e = t.querySelector(".stamp"),
            i = t.querySelector("button"),
            n = new Masonry(t.querySelector(".masonry"), {
                columnWidth: 60,
                itemSelector: ".item"
            }),
            o = !1;
            eventie.bind(i, "click",
            function() {
                o ? n.unstamp(e) : n.stamp(e),
                n.layout(),
                o = !o
            })
        } (),
        function() {
            var t = document.querySelector("#remove-demo .masonry"),
            e = new Masonry(t, {
                columnWidth: 60
            });
            eventie.bind(t, "click",
            function(t) {
                classie.has(t.target, "item") && (e.remove(t.target), e.layout())
            })
        } ()
    }
} (window); (function(p, h) {
    "object" === typeof exports ? h(exports) : "function" === typeof define && define.amd ? define(["exports"], h) : h(p)
})(this,
function(p) {
    function h(a) {
        this._targetElement = a;
        this._options = {
            nextLabel: "\u4e0b\u4e00\u6b65 ",
            prevLabel: "\u4e0a\u4e00\u6b65",
            skipLabel: "\u4e0d\u63d0\u793a",
            doneLabel: "\u5b8c\u6210",
            tooltipPosition: "bottom",
            tooltipClass: "",
            exitOnEsc: !0,
            exitOnOverlayClick: !1,
            showStepNumbers: !0
        }
    }
    function x(a) {
        var b = [],
        c = this;
        if (this._options.steps) for (var e = [], g = 0, e = this._options.steps.length; g < e; g++) {
            var h = this._options.steps[g];
            h.step = g + 1;
            "string" === typeof h.element && (h.element = document.querySelector(h.element));
            b.push(h)
        } else {
            e = a.querySelectorAll("*[data-intro]");
            if (1 > e.length) return ! 1;
            g = 0;
            for (h = e.length; g < h; g++) {
                var l = e[g];
                b.push({
                    element: l,
                    intro: l.getAttribute("data-intro"),
                    step: parseInt(l.getAttribute("data-step"), 10),
                    tooltipClass: l.getAttribute("data-tooltipClass"),
                    position: l.getAttribute("data-position") || this._options.tooltipPosition
                })
            }
        }
        b.sort(function(a, b) {
            return a.step - b.step
        });
        c._introItems = b;
        y.call(c, a) && (q.call(c), a.querySelector(".introjs-skipbutton"), a.querySelector(".introjs-nextbutton"), c._onKeyDown = function(b) {
            if (27 === b.keyCode && 1 == c._options.exitOnEsc) r.call(c, a),
            void 0 != c._introExitCallback && c._introExitCallback.call(c);
            else if (37 === b.keyCode) t.call(c);
            else if (39 === b.keyCode || 13 === b.keyCode) q.call(c),
            b.preventDefault ? b.preventDefault() : b.returnValue = !1
        },
        c._onResize = function(a) {
            s.call(c, document.querySelector(".introjs-helperLayer"))
        },
        window.addEventListener ? (window.addEventListener("keydown", c._onKeyDown, !0), window.addEventListener("resize", c._onResize, !0)) : document.attachEvent && (document.attachEvent("onkeydown", c._onKeyDown), document.attachEvent("onresize", c._onResize)));
        return ! 1
    }
    function q() {
        "undefined" === typeof this._currentStep ? this._currentStep = 0 : ++this._currentStep;
        if (this._introItems.length <= this._currentStep)"function" === typeof this._introCompleteCallback && this._introCompleteCallback.call(this),
        r.call(this, this._targetElement);
        else {
            var a = this._introItems[this._currentStep];
            "undefined" !== typeof this._introBeforeChangeCallback && this._introBeforeChangeCallback.call(this, a.element);
            u.call(this, a)
        }
    }
    function t() {
        if (0 === this._currentStep) return ! 1;
        var a = this._introItems[--this._currentStep];
        "undefined" !== typeof this._introBeforeChangeCallback && this._introBeforeChangeCallback.call(this, a.element);
        u.call(this, a)
    }
    function r(a) {
        var b = a.querySelector(".introjs-overlay");
        b.style.opacity = 0;
        setTimeout(function() {
            b.parentNode && b.parentNode.removeChild(b)
        },
        500); (a = a.querySelector(".introjs-helperLayer")) && a.parentNode.removeChild(a);
        if (a = document.querySelector(".introjs-showElement")) a.className = a.className.replace(/introjs-[a-zA-Z]+/g, "").replace(/^\s+|\s+$/g, "");
        if ((a = document.querySelectorAll(".introjs-fixParent")) && 0 < a.length) for (var c = a.length - 1; 0 <= c; c--) a[c].className = a[c].className.replace(/introjs-fixParent/g, "").replace(/^\s+|\s+$/g, "");
        window.removeEventListener ? window.removeEventListener("keydown", this._onKeyDown, !0) : document.detachEvent && document.detachEvent("onkeydown", this._onKeyDown);
        this._currentStep = void 0
    }
    function v(a, b, c) {
        b.style.top = null;
        b.style.right = null;
        b.style.bottom = null;
        b.style.left = null;
        b.style.quickBuild = null;
        b.style.message = null;
        b.style.systemSetup = null;
        b.style.userPanel = null;
        if (this._introItems[this._currentStep]) {
            var e = "",
            e = this._introItems[this._currentStep],
            e = "string" === typeof e.tooltipClass ? e.tooltipClass: this._options.tooltipClass;
            b.className = ("introjs-tooltip " + e).replace(/^\s+|\s+$/g, "");
            switch (this._introItems[this._currentStep].position) {
            case "top":
                b.style.left = "15px";
                b.style.top = "-" + (g(b).height + 10) + "px";
                c.className = "introjs-arrow bottom";
                break;
            case "right":
                b.style.left = g(a).width + 20 + "px";
                c.className = "introjs-arrow left";
                break;
            case "left":
                b.style.top = "15px";
                b.style.right = g(a).width + 20 + "px";
                c.className = "introjs-arrow right";
                break;
            case "quickBuild":
                b.style.top = "38px";
                b.style.right = g(a).width + 20 + "px";
                c.className = "introjs-arrow right";
                break;
            case "message":
                b.style.top = "36px";
                b.style.left = g(a).width + 20 + "px";
                c.className = "introjs-arrow left";
                break;
            case "systemSetup":
                b.style.top = "35px";
                b.style.right = g(a).width + 105 + "px";
                c.className = "introjs-arrow right";
                break;
            case "userPanel":
                b.style.top = "25px";
                b.style.left = g(a).width + 14 + "px";
                c.className = "introjs-arrow left";
                break;
            default:
                b.style.bottom = "-" + (g(b).height + 10) + "px",
                c.className = "introjs-arrow top"
            }
        }
    }
    function s(a) {
        if (a && this._introItems[this._currentStep]) {
            var b = g(this._introItems[this._currentStep].element);
            a.setAttribute("style", "width: " + (b.width + 10) + "px; height:" + (b.height + 10) + "px; top:" + (b.top - 5) + "px;left: " + (b.left - 5) + "px;")
        }
    }
    function u(a) {
        "undefined" !== typeof this._introChangeCallback && this._introChangeCallback.call(this, a.element);
        var b = this,
        c = document.querySelector(".introjs-helperLayer");
        g(a.element);
        if (null != c) {
            var e = c.querySelector(".introjs-helperNumberLayer"),
            h = c.querySelector(".introjs-tooltiptext"),
            p = c.querySelector(".introjs-arrow"),
            l = c.querySelector(".introjs-tooltip"),
            d = c.querySelector(".introjs-skipbutton"),
            f = c.querySelector(".introjs-prevbutton"),
            m = c.querySelector(".introjs-nextbutton");
            l.style.opacity = 0;
            s.call(b, c);
            if ((c = document.querySelectorAll(".introjs-fixParent")) && 0 < c.length) for (var k = c.length - 1; 0 <= k; k--) c[k].className = c[k].className.replace(/introjs-fixParent/g, "").replace(/^\s+|\s+$/g, "");
            c = document.querySelector(".introjs-showElement");
            c.className = c.className.replace(/introjs-[a-zA-Z]+/g, "").replace(/^\s+|\s+$/g, "");
            b._lastShowElementTimer && clearTimeout(b._lastShowElementTimer);
            b._lastShowElementTimer = setTimeout(function() {
                null != e && (e.innerHTML = a.step);
                h.innerHTML = a.intro;
                v.call(b, a.element, l, p);
                l.style.opacity = 1
            },
            350)
        } else {
            d = document.createElement("div");
            c = document.createElement("div");
            k = document.createElement("div");
            d.className = "introjs-helperLayer";
            s.call(b, d);
            this._targetElement.appendChild(d);
            c.className = "introjs-arrow";
            k.innerHTML = '\x3cdiv class\x3d"introjs-tooltiptext"\x3e' + a.intro + '\x3c/div\x3e\x3cdiv class\x3d"introjs-tooltipbuttons"\x3e\x3c/div\x3e';
            this._options.showStepNumbers && (f = document.createElement("span"), f.className = "introjs-helperNumberLayer", f.innerHTML = a.step, d.appendChild(f));
            k.appendChild(c);
            d.appendChild(k);
            m = document.createElement("a");
            m.onclick = function() {
                b._introItems.length - 1 != b._currentStep && q.call(b)
            };
            m.href = "javascript:void(0);";
            m.innerHTML = this._options.nextLabel;
            f = document.createElement("a");
            f.onclick = function() {
                0 != b._currentStep && t.call(b)
            };
            f.href = "javascript:void(0);";
            f.innerHTML = this._options.prevLabel;
            d = document.createElement("a");
            d.className = "btn btn-sm introjs-button introjs-skipbutton";
            d.href = "javascript:void(0);";
            d.innerHTML = this._options.skipLabel;
            d.onclick = function() {
                b._introItems.length - 1 == b._currentStep && "function" === typeof b._introCompleteCallback && b._introCompleteCallback.call(b);
                b._introItems.length - 1 != b._currentStep && "function" === typeof b._introExitCallback && b._introExitCallback.call(b);
                r.call(b, b._targetElement)
            };
            var n = k.querySelector(".introjs-tooltipbuttons");
            n.appendChild(d);
            1 < this._introItems.length && (n.appendChild(f), n.appendChild(m));
            v.call(b, a.element, k, c)
        }
        0 == this._currentStep ? (f.className = "btn btn-sm introjs-button introjs-prevbutton introjs-disabled", m.className = "btn btn-sm btn-success introjs-button introjs-nextbutton", d.className = "btn btn-sm introjs-button introjs-skipbutton", d.innerHTML = this._options.skipLabel, $(".introjs-skipbutton").after($(".introjs-prevbutton"))) : this._introItems.length - 1 == this._currentStep ? (d.innerHTML = this._options.doneLabel, f.className = "btn btn-sm introjs-button introjs-prevbutton", m.className = "btn btn-sm btn-success introjs-button introjs-nextbutton introjs-disabled", d.className = " btn-success btn-done " + d.className, $(".introjs-prevbutton").after($(".introjs-skipbutton"))) : (f.className = "btn btn-sm introjs-button introjs-prevbutton", m.className = "btn btn-sm btn-success introjs-button introjs-nextbutton", d.className = "btn btn-sm introjs-button introjs-skipbutton", d.innerHTML = this._options.skipLabel, $(".introjs-skipbutton").after($(".introjs-prevbutton")));
        m.focus();
        a.element.className += " introjs-showElement";
        d = w(a.element, "position");
        "absolute" !== d && "relative" !== d && (a.element.className += " introjs-relativePosition");
        for (d = a.element.parentNode; null != d && "body" !== d.tagName.toLowerCase();) f = w(d, "z-index"),
        /[0-9]+/.test(f) && (d.className += " introjs-fixParent"),
        d = d.parentNode;
        z(a.element) || (f = a.element.getBoundingClientRect(), d = f.bottom - (f.bottom - f.top), f = f.bottom - A().height, 0 > d ? window.scrollBy(0, d - 30) : window.scrollBy(0, f + 100))
    }
    function w(a, b) {
        var c = "";
        a.currentStyle ? c = a.currentStyle[b] : document.defaultView && document.defaultView.getComputedStyle && (c = document.defaultView.getComputedStyle(a, null).getPropertyValue(b));
        return c && c.toLowerCase ? c.toLowerCase() : c
    }
    function A() {
        if (void 0 != window.innerWidth) return {
            width: window.innerWidth,
            height: window.innerHeight
        };
        var a = document.documentElement;
        return {
            width: a.clientWidth,
            height: a.clientHeight
        }
    }
    function z(a) {
        a = a.getBoundingClientRect();
        return 0 <= a.top && 0 <= a.left && a.bottom + 80 <= window.innerHeight && a.right <= window.innerWidth
    }
    function y(a) {
        var b = document.createElement("div"),
        c = "";
        b.className = "introjs-overlay";
        if ("body" === a.tagName.toLowerCase()) c += "top: 0;bottom: 0; left: 0;right: 0;position: fixed;",
        b.setAttribute("style", c);
        else {
            var e = g(a);
            e && (c += "width: " + e.width + "px; height:" + e.height + "px; top:" + e.top + "px;left: " + e.left + "px;", b.setAttribute("style", c))
        }
        a.appendChild(b);
        setTimeout(function() {
            c += "opacity: .8;";
            b.setAttribute("style", c)
        },
        10);
        return ! 0
    }
    function g(a) {
        var b = {};
        b.width = a.offsetWidth;
        b.height = a.offsetHeight;
        for (var c = 0,
        e = 0; a && !isNaN(a.offsetLeft) && !isNaN(a.offsetTop);) c += a.offsetLeft,
        e += a.offsetTop,
        a = a.offsetParent;
        b.top = e;
        b.left = c;
        return b
    }
    var n = function(a) {
        if ("object" === typeof a) return new h(a);
        if ("string" === typeof a) {
            if (a = document.querySelector(a)) return new h(a);
            throw Error("There is no element with given selector.");
        }
        return new h(document.body)
    };
    n.version = "0.5.0";
    n.fn = h.prototype = {
        clone: function() {
            return new h(this)
        },
        setOption: function(a, b) {
            this._options[a] = b;
            return this
        },
        setOptions: function(a) {
            var b = this._options,
            c = {},
            e;
            for (e in b) c[e] = b[e];
            for (e in a) c[e] = a[e];
            this._options = c;
            return this
        },
        start: function() {
            x.call(this, this._targetElement);
            return this
        },
        goToStep: function(a) {
            this._currentStep = a - 2;
            "undefined" !== typeof this._introItems && q.call(this);
            return this
        },
        exit: function() {
            r.call(this, this._targetElement)
        },
        refresh: function() {
            s.call(this, document.querySelector(".introjs-helperLayer"));
            return this
        },
        onbeforechange: function(a) {
            if ("function" === typeof a) this._introBeforeChangeCallback = a;
            else throw Error("Provided callback for onbeforechange was not a function");
            return this
        },
        onchange: function(a) {
            if ("function" === typeof a) this._introChangeCallback = a;
            else throw Error("Provided callback for onchange was not a function.");
            return this
        },
        oncomplete: function(a) {
            if ("function" === typeof a) this._introCompleteCallback = a;
            else throw Error("Provided callback for oncomplete was not a function.");
            return this
        },
        onexit: function(a) {
            if ("function" === typeof a) this._introExitCallback = a;
            else throw Error("Provided callback for onexit was not a function.");
            return this
        }
    };
    return p.introJs = n
}); (function(r) {
    function n() {
        this.returnValue = !1
    }
    function g() {
        this.cancelBubble = !0
    }
    var t = 0,
    D = [],
    E = {},
    v = {},
    k = {
        "\x3c": "lt",
        "\x3e": "gt",
        "\x26": "amp",
        '"': "quot",
        "'": "#39"
    },
    H = /[<>&\"\']/g,
    B = window.setTimeout,
    s = {},
    l; (function(a) {
        a = a.split(/,/);
        var e, f, c;
        for (e = 0; e < a.length; e += 2) for (c = a[e + 1].split(/ /), f = 0; f < c.length; f++) v[c[f]] = a[e]
    })("application/msword,doc dot,application/pdf,pdf,application/pgp-signature,pgp,application/postscript,ps ai eps,application/rtf,rtf,application/vnd.ms-excel,xls xlb,application/vnd.ms-powerpoint,ppt pps pot,application/zip,zip,application/x-shockwave-flash,swf swfl,application/vnd.openxmlformats-officedocument.wordprocessingml.document,docx,application/vnd.openxmlformats-officedocument.wordprocessingml.template,dotx,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,xlsx,application/vnd.openxmlformats-officedocument.presentationml.presentation,pptx,application/vnd.openxmlformats-officedocument.presentationml.template,potx,application/vnd.openxmlformats-officedocument.presentationml.slideshow,ppsx,application/x-javascript,js,application/json,json,audio/mpeg,mpga mpega mp2 mp3,audio/x-wav,wav,audio/mp4,m4a,image/bmp,bmp,image/gif,gif,image/jpeg,jpeg jpg jpe,image/photoshop,psd,image/png,png,image/svg+xml,svg svgz,image/tiff,tiff tif,text/plain,asc txt text diff log,text/html,htm html xhtml,text/css,css,text/csv,csv,text/rtf,rtf,video/mpeg,mpeg mpg mpe m2v,video/quicktime,qt mov,video/mp4,mp4,video/x-m4v,m4v,video/x-flv,flv,video/x-ms-wmv,wmv,video/avi,avi,video/webm,webm,video/3gpp,3gp,video/3gpp2,3g2,video/vnd.rn-realvideo,rv,application/vnd.oasis.opendocument.formula-template,otf,application/octet-stream,exe");
    var h = {
        VERSION: "1.5.7",
        STOPPED: 1,
        STARTED: 2,
        QUEUED: 1,
        UPLOADING: 2,
        FAILED: 4,
        DONE: 5,
        GENERIC_ERROR: -100,
        HTTP_ERROR: -200,
        IO_ERROR: -300,
        SECURITY_ERROR: -400,
        INIT_ERROR: -500,
        FILE_SIZE_ERROR: -600,
        FILE_EXTENSION_ERROR: -601,
        IMAGE_FORMAT_ERROR: -700,
        IMAGE_MEMORY_ERROR: -701,
        IMAGE_DIMENSIONS_ERROR: -702,
        mimeTypes: v,
        ua: function() {
            var a = navigator,
            e = a.userAgent,
            f = a.vendor,
            c, b;
            b = (c = /WebKit/.test(e)) && -1 !== f.indexOf("Apple");
            f = window.opera && window.opera.buildNumber;
            return {
                windows: -1 !== navigator.platform.indexOf("Win"),
                android: /Android/.test(e),
                ie: !c && !f && /MSIE/gi.test(e) && /Explorer/gi.test(a.appName),
                webkit: c,
                gecko: !c && /Gecko/.test(e),
                safari: b,
                opera: !!f
            }
        } (),
        typeOf: function(a) {
            return {}.toString.call(a).match(/\s([a-z|A-Z]+)/)[1].toLowerCase()
        },
        extend: function(a) {
            h.each(arguments,
            function(e, f) {
                0 < f && h.each(e,
                function(c, b) {
                    a[b] = c
                })
            });
            return a
        },
        cleanName: function(a) {
            var e, f;
            f = [/[\300-\306]/g, "A", /[\340-\346]/g, "a", /\307/g, "C", /\347/g, "c", /[\310-\313]/g, "E", /[\350-\353]/g, "e", /[\314-\317]/g, "I", /[\354-\357]/g, "i", /\321/g, "N", /\361/g, "n", /[\322-\330]/g, "O", /[\362-\370]/g, "o", /[\331-\334]/g, "U", /[\371-\374]/g, "u"];
            for (e = 0; e < f.length; e += 2) a = a.replace(f[e], f[e + 1]);
            a = a.replace(/\s+/g, "_");
            return a = a.replace(/[^a-z0-9_\-\.]+/gi, "")
        },
        addRuntime: function(a, e) {
            e.name = a;
            D[a] = e;
            D.push(e);
            return e
        },
        guid: function() {
            var a = (new Date).getTime().toString(32),
            e;
            for (e = 0; 5 > e; e++) a += Math.floor(65535 * Math.random()).toString(32);
            return (h.guidPrefix || "p") + a + (t++).toString(32)
        },
        buildUrl: function(a, e) {
            var f = "";
            h.each(e,
            function(a, b) {
                f += (f ? "\x26": "") + encodeURIComponent(b) + "\x3d" + encodeURIComponent(a)
            });
            f && (a += (0 < a.indexOf("?") ? "\x26": "?") + f);
            return a
        },
        each: function(a, e) {
            var f, c;
            if (a) if (f = a.length, void 0 === f) for (c in a) {
                if (a.hasOwnProperty(c) && !1 === e(a[c], c)) break
            } else for (c = 0; c < f && !1 !== e(a[c], c); c++);
        },
        formatSize: function(a) {
            return void 0 === a || /\D/.test(a) ? h.translate("N/A") : 1073741824 < a ? Math.round(a / 1073741824, 1) + " GB": 1048576 < a ? Math.round(a / 1048576, 1) + " MB": 1024 < a ? Math.round(a / 1024, 1) + " KB": a + " b"
        },
        getPos: function(a, e) {
            function f(b) {
                var a, d = 0;
                a = 0;
                b && (a = b.getBoundingClientRect(), b = "CSS1Compat" === q.compatMode ? q.documentElement: q.body, d = a.left + b.scrollLeft, a = a.top + b.scrollTop);
                return {
                    x: d,
                    y: a
                }
            }
            var c = 0,
            b = 0,
            d, q = document;
            e = e || q.body;
            if (a && a.getBoundingClientRect && h.ua.ie && (!q.documentMode || 8 > q.documentMode)) return c = f(a),
            b = f(e),
            {
                x: c.x - b.x,
                y: c.y - b.y
            };
            for (d = a; d && d != e && d.nodeType;) c += d.offsetLeft || 0,
            b += d.offsetTop || 0,
            d = d.offsetParent;
            for (d = a.parentNode; d && d != e && d.nodeType;) c -= d.scrollLeft || 0,
            b -= d.scrollTop || 0,
            d = d.parentNode;
            return {
                x: c,
                y: b
            }
        },
        getSize: function(a) {
            return {
                w: a.offsetWidth || a.clientWidth,
                h: a.offsetHeight || a.clientHeight
            }
        },
        parseSize: function(a) {
            var e;
            "string" == typeof a && (a = /^([0-9]+)([mgk]?)$/.exec(a.toLowerCase().replace(/[^0-9mkg]/g, "")), e = a[2], a = +a[1], "g" == e && (a *= 1073741824), "m" == e && (a *= 1048576), "k" == e && (a *= 1024));
            return a
        },
        xmlEncode: function(a) {
            return a ? ("" + a).replace(H,
            function(a) {
                return k[a] ? "\x26" + k[a] + ";": a
            }) : a
        },
        toArray: function(a) {
            var e, f = [];
            for (e = 0; e < a.length; e++) f[e] = a[e];
            return f
        },
        inArray: function(a, e) {
            if (e) {
                if (Array.prototype.indexOf) return Array.prototype.indexOf.call(e, a);
                for (var f = 0,
                c = e.length; f < c; f++) if (e[f] === a) return f
            }
            return - 1
        },
        addI18n: function(a) {
            return h.extend(E, a)
        },
        translate: function(a) {
            return E[a] || a
        },
        isEmptyObj: function(a) {
            if (void 0 === a) return ! 0;
            for (var e in a) return ! 1;
            return ! 0
        },
        hasClass: function(a, e) {
            return "" == a.className ? !1 : (new RegExp("(^|\\s+)" + e + "(\\s+|$)")).test(a.className)
        },
        addClass: function(a, e) {
            h.hasClass(a, e) || (a.className = "" == a.className ? e: a.className.replace(/\s+$/, "") + " " + e)
        },
        removeClass: function(a, e) {
            a.className = a.className.replace(new RegExp("(^|\\s+)" + e + "(\\s+|$)"),
            function(a, c, b) {
                return " " === c && " " === b ? " ": ""
            })
        },
        getStyle: function(a, e) {
            if (a.currentStyle) return a.currentStyle[e];
            if (window.getComputedStyle) return window.getComputedStyle(a, null)[e]
        },
        addEvent: function(a, e, f, c) {
            var b;
            e = e.toLowerCase();
            void 0 === l && (l = "Plupload_" + h.guid());
            a.addEventListener ? (b = f, a.addEventListener(e, b, !1)) : a.attachEvent && (b = function() {
                var b = window.event;
                b.target || (b.target = b.srcElement);
                b.preventDefault = n;
                b.stopPropagation = g;
                f(b)
            },
            a.attachEvent("on" + e, b));
            void 0 === a[l] && (a[l] = h.guid());
            s.hasOwnProperty(a[l]) || (s[a[l]] = {});
            a = s[a[l]];
            a.hasOwnProperty(e) || (a[e] = []);
            a[e].push({
                func: b,
                orig: f,
                key: c
            })
        },
        removeEvent: function(a, e, f) {
            var c, b;
            "function" == typeof f ? c = f: b = f;
            e = e.toLowerCase();
            if (a[l] && s[a[l]] && s[a[l]][e]) {
                f = s[a[l]][e];
                for (var d = f.length - 1; 0 <= d; d--) if (f[d].key === b || f[d].orig === c) if (a.removeEventListener ? a.removeEventListener(e, f[d].func, !1) : a.detachEvent && a.detachEvent("on" + e, f[d].func), f[d].orig = null, f[d].func = null, f.splice(d, 1), void 0 !== c) break;
                f.length || delete s[a[l]][e];
                if (h.isEmptyObj(s[a[l]])) {
                    delete s[a[l]];
                    try {
                        delete a[l]
                    } catch(q) {
                        a[l] = void 0
                    }
                }
            }
        },
        removeAllEvents: function(a, e) {
            void 0 !== a[l] && a[l] && h.each(s[a[l]],
            function(f, c) {
                h.removeEvent(a, c, e)
            })
        },
        Uploader: function(a) {
            function e() {
                var b, a = 0,
                c;
                if (this.state == h.STARTED) {
                    for (c = 0; c < d.length; c++) b || d[c].status != h.QUEUED ? a++:(b = d[c], b.status = h.UPLOADING, this.trigger("BeforeUpload", b) && this.trigger("UploadFile", b));
                    a == d.length && (this.stop(), this.trigger("UploadComplete", d))
                }
            }
            function f() {
                var a, c;
                b.reset();
                for (a = 0; a < d.length; a++) c = d[a],
                void 0 !== c.size ? (b.size += c.size, b.loaded += c.loaded) : b.size = void 0,
                c.status == h.DONE ? b.uploaded++:c.status == h.FAILED ? b.failed++:b.queued++;
                void 0 === b.size ? b.percent = 0 < d.length ? Math.ceil(b.uploaded / d.length * 100) : 0 : (b.bytesPerSec = Math.ceil(b.loaded / (( + new Date - q || 1) / 1E3)), b.percent = 0 < b.size ? Math.ceil(b.loaded / b.size * 100) : 0)
            }
            var c = {},
            b, d = [],
            q,
            g = !1;
            b = new h.QueueProgress;
            a = h.extend({
                chunk_size: 0,
                multipart: !0,
                multi_selection: !0,
                file_data_name: "file",
                filters: []
            },
            a);
            h.extend(this, {
                state: h.STOPPED,
                runtime: "",
                features: {},
                files: d,
                settings: a,
                total: b,
                id: h.guid(),
                init: function() {
                    function b() {
                        var a = u[k++],
                        d,
                        e,
                        f;
                        if (a) {
                            d = a.getFeatures();
                            if (e = c.settings.required_features) for (e = e.split(","), f = 0; f < e.length; f++) if (!d[e[f]]) {
                                b();
                                return
                            }
                            a.init(c,
                            function(e) {
                                e && e.success ? (c.features = d, c.runtime = a.name, c.trigger("Init", {
                                    runtime: a.name
                                }), c.trigger("PostInit"), c.refresh()) : b()
                            })
                        } else c.trigger("Error", {
                            code: h.INIT_ERROR,
                            message: h.translate("Init error.")
                        })
                    }
                    var c = this,
                    g, u, k = 0,
                    l;
                    "function" == typeof a.preinit ? a.preinit(c) : h.each(a.preinit,
                    function(b, a) {
                        c.bind(a, b)
                    });
                    a.page_url = a.page_url || document.location.pathname.replace(/\/[^\/]+$/g, "/");
                    /^(\w+:\/\/|\/)/.test(a.url) || (a.url = a.page_url + a.url);
                    a.chunk_size = h.parseSize(a.chunk_size);
                    a.max_file_size = h.parseSize(a.max_file_size);
                    c.bind("FilesAdded",
                    function(b, e) {
                        var f, g, y = 0,
                        q; (f = a.filters) && f.length && (q = [], h.each(f,
                        function(b) {
                            h.each(b.extensions.split(/,/),
                            function(b) { / ^\s * \ * \s * $ / .test(b) ? q.push("\\.*") : q.push("\\." + b.replace(new RegExp("[" + "/^$.*+?|()[]{}\\".replace(/./g, "\\$\x26") + "]", "g"), "\\$\x26"))
                            })
                        }), q = new RegExp(q.join("|") + "$", "i"));
                        for (f = 0; f < e.length; f++) g = e[f],
                        g.loaded = 0,
                        g.percent = 0,
                        g.status = h.QUEUED,
                        q && !q.test(g.name) ? b.trigger("Error", {
                            code: h.FILE_EXTENSION_ERROR,
                            message: h.translate("File extension error."),
                            file: g
                        }) : void 0 !== g.size && g.size > a.max_file_size ? b.trigger("Error", {
                            code: h.FILE_SIZE_ERROR,
                            message: h.translate("File size error."),
                            file: g
                        }) : (d.push(g), y++);
                        if (y) B(function() {
                            c.trigger("QueueChanged");
                            c.refresh()
                        },
                        1);
                        else return ! 1
                    });
                    a.unique_names && c.bind("UploadFile",
                    function(b, a) {
                        var c = a.name.match(/\.([^.]+)$/),
                        d = "tmp";
                        c && (d = c[1]);
                        a.target_name = a.id + "." + d
                    });
                    c.bind("UploadProgress",
                    function(b, a) {
                        a.percent = 0 < a.size ? Math.ceil(a.loaded / a.size * 100) : 100;
                        f()
                    });
                    c.bind("StateChanged",
                    function(b) {
                        if (b.state == h.STARTED) q = +new Date;
                        else if (b.state == h.STOPPED) for (g = b.files.length - 1; 0 <= g; g--) b.files[g].status == h.UPLOADING && (b.files[g].status = h.QUEUED, f())
                    });
                    c.bind("QueueChanged", f);
                    c.bind("Error",
                    function(b, a) {
                        a.file && (a.file.status = h.FAILED, f(), b.state == h.STARTED && B(function() {
                            e.call(c)
                        },
                        1))
                    });
                    c.bind("FileUploaded",
                    function(b, a) {
                        a.status = h.DONE;
                        a.loaded = a.size;
                        b.trigger("UploadProgress", a);
                        B(function() {
                            e.call(c)
                        },
                        1)
                    });
                    if (a.runtimes) for (u = [], l = a.runtimes.split(/\s?,\s?/), g = 0; g < l.length; g++) D[l[g]] && u.push(D[l[g]]);
                    else u = D;
                    b();
                    "function" == typeof a.init ? a.init(c) : h.each(a.init,
                    function(b, a) {
                        c.bind(a, b)
                    })
                },
                refresh: function() {
                    this.trigger("Refresh")
                },
                start: function() {
                    d.length && this.state != h.STARTED && (this.state = h.STARTED, this.trigger("StateChanged"), e.call(this))
                },
                stop: function() {
                    this.state != h.STOPPED && (this.state = h.STOPPED, this.trigger("CancelUpload"), this.trigger("StateChanged"))
                },
                disableBrowse: function(b) {
                    g = void 0 !== b ? b: !0;
                    this.trigger("DisableBrowse", g)
                },
                getFile: function(b) {
                    var a;
                    for (a = d.length - 1; 0 <= a; a--) if (d[a].id === b) return d[a]
                },
                removeFile: function(b) {
                    var a;
                    for (a = d.length - 1; 0 <= a; a--) if (d[a].id === b.id) return this.splice(a, 1)[0]
                },
                splice: function(b, a) {
                    var c;
                    c = d.splice(void 0 === b ? 0 : b, void 0 === a ? d.length: a);
                    this.trigger("FilesRemoved", c);
                    this.trigger("QueueChanged");
                    return c
                },
                trigger: function(b) {
                    var a = c[b.toLowerCase()],
                    d,
                    e;
                    if (a) for (e = Array.prototype.slice.call(arguments), e[0] = this, d = 0; d < a.length; d++) if (!1 === a[d].func.apply(a[d].scope, e)) return ! 1;
                    return ! 0
                },
                hasEventListener: function(b) {
                    return !! c[b.toLowerCase()]
                },
                bind: function(b, a, d) {
                    var e;
                    b = b.toLowerCase();
                    e = c[b] || [];
                    e.push({
                        func: a,
                        scope: d || this
                    });
                    c[b] = e
                },
                unbind: function(b, a) {
                    b = b.toLowerCase();
                    var d = c[b],
                    e;
                    if (d) {
                        if (void 0 !== a) for (e = d.length - 1; 0 <= e; e--) {
                            if (d[e].func === a) {
                                d.splice(e, 1);
                                break
                            }
                        } else d = [];
                        d.length || delete c[b]
                    }
                },
                unbindAll: function() {
                    var b = this;
                    h.each(c,
                    function(a, c) {
                        b.unbind(c)
                    })
                },
                destroy: function() {
                    this.stop();
                    this.trigger("Destroy");
                    this.unbindAll()
                }
            })
        },
        File: function(a, e, f) {
            this.id = a;
            this.name = e;
            this.size = f;
            this.status = this.percent = this.loaded = 0
        },
        Runtime: function() {
            this.getFeatures = function() {};
            this.init = function(a, e) {}
        },
        QueueProgress: function() {
            var a = this;
            a.size = 0;
            a.loaded = 0;
            a.uploaded = 0;
            a.failed = 0;
            a.queued = 0;
            a.percent = 0;
            a.bytesPerSec = 0;
            a.reset = function() {
                a.size = a.loaded = a.uploaded = a.failed = a.queued = a.percent = a.bytesPerSec = 0
            }
        },
        runtimes: {}
    };
    window.plupload = h
})(jQuery); (function(r, n, g, t) {
    function D() {
        var g;
        try {
            g = navigator.plugins["Shockwave Flash"],
            g = g.description
        } catch(n) {
            try {
                g = (new ActiveXObject("ShockwaveFlash.ShockwaveFlash")).GetVariable("$version")
            } catch(r) {
                g = "0.0"
            }
        }
        g = g.match(/\d+/g);
        return parseFloat(g[0] + "." + g[1])
    }
    var E = {},
    v = {};
    g.flash = {
        trigger: function(g, n, r) {
            setTimeout(function() {
                var s = E[g];
                s && s.trigger("Flash:" + n, r)
            },
            0)
        }
    };
    g.runtimes.Flash = g.addRuntime("flash", {
        getFeatures: function() {
            return {
                jpgresize: !0,
                pngresize: !0,
                maxWidth: 8091,
                maxHeight: 8091,
                chunks: !0,
                progress: !0,
                multipart: !0,
                multi_selection: !0
            }
        },
        init: function(k, r) {
            function B() {
                return n.getElementById(k.id + "_flash")
            }
            function s() {
                5E3 < h++?r({
                    success: !1
                }) : !1 === v[k.id] && setTimeout(s, 1)
            }
            var l, h = 0,
            a = n.body;
            10 > D() ? r({
                success: !1
            }) : (v[k.id] = !1, E[k.id] = k, $(k.settings.browse_button).get(0), l = n.createElement("div"), l.id = k.id + "_flash_container", g.extend(l.style, {
                position: "absolute",
                top: "0px",
                background: k.settings.shim_bgcolor || "transparent",
                zIndex: 99999,
                width: "100%",
                height: "100%"
            }), l.className = "plupload flash", k.settings.container && (a = $(k.settings.container).get(0), "static" === g.getStyle(a, "position") && (a.style.position = "relative")), a.appendChild(l),
            function() {
                var a, f;
                a = '\x3cobject id\x3d"' + k.id + '_flash" type\x3d"application/x-shockwave-flash" data\x3d"' + k.settings.flash_swf_url + '" ';
                g.ua.ie && (a += 'classid\x3d"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ');
                a += 'width\x3d"100%" height\x3d"100%" style\x3d"outline:0"\x3e\x3cparam name\x3d"movie" value\x3d"' + k.settings.flash_swf_url + '" /\x3e\x3cparam name\x3d"flashvars" value\x3d"id\x3d' + escape(k.id) + '" /\x3e\x3cparam name\x3d"wmode" value\x3d"transparent" /\x3e\x3cparam name\x3d"allowscriptaccess" value\x3d"always" /\x3e\x3c/object\x3e';
                g.ua.ie ? (f = n.createElement("div"), l.appendChild(f), f.outerHTML = a) : l.innerHTML = a
            } (), s(), l = null, k.bind("Destroy",
            function(a) {
                g.removeAllEvents(n.body, a.id);
                delete v[a.id];
                delete E[a.id]; (a = n.getElementById(a.id + "_flash_container")) && a.parentNode.removeChild(a)
            }), k.bind("Flash:Init",
            function() {
                var a = {};
                try {
                    B().setFileFilters(k.settings.filters, k.settings.multi_selection)
                } catch(f) {
                    r({
                        success: !1
                    });
                    return
                }
                v[k.id] || (v[k.id] = !0, k.bind("UploadFile",
                function(c, b) {
                    var d = c.settings,
                    f = k.settings.resize || {};
                    B().uploadFile(a[b.id], d.url, {
                        name: b.target_name || b.name,
                        mime: g.mimeTypes[b.name.replace(/^.+\.([^.]+)/, "$1").toLowerCase()] || "application/octet-stream",
                        chunk_size: d.chunk_size,
                        width: f.width,
                        height: f.height,
                        quality: f.quality,
                        multipart: d.multipart,
                        multipart_params: d.multipart_params || {},
                        file_data_name: d.file_data_name,
                        format: /\.(jpg|jpeg)$/i.test(b.name) ? "jpg": "png",
                        headers: d.headers,
                        urlstream_upload: d.urlstream_upload
                    })
                }), k.bind("CancelUpload",
                function() {
                    B().cancelUpload()
                }), k.bind("Flash:UploadProcess",
                function(c, b) {
                    var d = c.getFile(a[b.id]);
                    d.status != g.FAILED && (d.loaded = b.loaded, d.size = b.size, c.trigger("UploadProgress", d))
                }), k.bind("Flash:UploadChunkComplete",
                function(c, b) {
                    var d = c.getFile(a[b.id]);
                    c.trigger("ChunkUploaded", d, {
                        chunk: b.chunk,
                        chunks: b.chunks,
                        response: b.text
                    });
                    d.status !== g.FAILED && c.state !== g.STOPPED && B().uploadNextChunk();
                    b.chunk == b.chunks - 1 && (d.status = g.DONE, c.trigger("FileUploaded", d, {
                        response: b.text
                    }))
                }), k.bind("Flash:SelectFiles",
                function(c, b) {
                    var d, f, h = [],
                    y;
                    for (f = 0; f < b.length; f++) d = b[f],
                    y = g.guid(),
                    a[y] = d.id,
                    a[d.id] = y,
                    h.push(new g.File(y, d.name, d.size));
                    h.length && k.trigger("FilesAdded", h)
                }), k.bind("Flash:SecurityError",
                function(c, b) {
                    k.trigger("Error", {
                        code: g.SECURITY_ERROR,
                        message: g.translate("Security error."),
                        details: b.message,
                        file: k.getFile(a[b.id])
                    })
                }), k.bind("Flash:GenericError",
                function(c, b) {
                    k.trigger("Error", {
                        code: g.GENERIC_ERROR,
                        message: g.translate("Generic error."),
                        details: b.message,
                        file: k.getFile(a[b.id])
                    })
                }), k.bind("Flash:IOError",
                function(c, b) {
                    k.trigger("Error", {
                        code: g.IO_ERROR,
                        message: g.translate("IO error."),
                        details: b.message,
                        file: k.getFile(a[b.id])
                    })
                }), k.bind("Flash:ImageError",
                function(c, b) {
                    k.trigger("Error", {
                        code: parseInt(b.code, 10),
                        message: g.translate("Image error."),
                        file: k.getFile(a[b.id])
                    })
                }), k.bind("Flash:StageEvent:rollOver",
                function(a) {
                    var b;
                    b = $(k.settings.browse_button).get(0);
                    a = a.settings.browse_button_hover;
                    b && a && g.addClass(b, a)
                }), k.bind("Flash:StageEvent:rollOut",
                function(a) {
                    var b;
                    b = $(k.settings.browse_button).get(0);
                    a = a.settings.browse_button_hover;
                    b && a && g.removeClass(b, a)
                }), k.bind("Flash:StageEvent:mouseDown",
                function(a) {
                    var b, d;
                    b = $(k.settings.browse_button).get(0);
                    d = a.settings.browse_button_active;
                    b && d && (g.addClass(b, d), g.addEvent(n.body, "mouseup",
                    function() {
                        g.removeClass(b, d)
                    },
                    a.id))
                }), k.bind("Flash:StageEvent:mouseUp",
                function(a) {
                    var b;
                    b = $(k.settings.browse_button).get(0);
                    a = a.settings.browse_button_active;
                    b && a && g.removeClass(b, a)
                }), k.bind("Flash:ExifData",
                function(c, b) {
                    k.trigger("ExifData", k.getFile(a[b.id]), b.data)
                }), k.bind("Flash:GpsData",
                function(c, b) {
                    k.trigger("GpsData", k.getFile(a[b.id]), b.data)
                }), k.bind("QueueChanged",
                function(a) {
                    k.refresh()
                }), k.bind("FilesRemoved",
                function(c, b) {
                    var d;
                    for (d = 0; d < b.length; d++) B().removeFile(a[b[d].id])
                }), k.bind("StateChanged",
                function(a) {
                    k.refresh()
                }), k.bind("Refresh",
                function(a) {
                    var b, d;
                    B().setFileFilters(k.settings.filters, k.settings.multi_selection);
                    if (b = $(k.settings.browse_button).get(0)) d = g.getPos(b, $(a.settings.container).get(0)),
                    b = g.getSize(b),
                    g.extend(n.getElementById(a.id + "_flash_container").style, {
                        top: d.y + "px",
                        left: d.x + "px",
                        width: b.w + "px",
                        height: b.h + "px"
                    })
                }), k.bind("DisableBrowse",
                function(a, b) {
                    B().disableBrowse(b)
                }), r({
                    success: !0
                }))
            }))
        }
    })
})(window, document, plupload); (function(r, n, g, t) {
    function D(g, a) {
        var e;
        if ("FileReader" in r) e = new FileReader,
        e.readAsDataURL(g),
        e.onload = function() {
            a(e.result)
        };
        else return a(g.getAsDataURL())
    }
    function E(g, a) {
        var e;
        if ("FileReader" in r) e = new FileReader,
        e.readAsBinaryString(g),
        e.onload = function() {
            a(e.result)
        };
        else return a(g.getAsBinary())
    }
    function v(g, a, e, f) {
        var c, b, d, q = this;
        D(s[g.id],
        function(u) {
            c = n.createElement("canvas");
            c.style.display = "none";
            n.body.appendChild(c);
            b = new Image;
            b.onerror = b.onabort = function() {
                f({
                    success: !1
                })
            };
            b.onload = function() {
                var y, k, p, m;
                a.width || (a.width = b.width);
                a.height || (a.height = b.height);
                d = Math.min(a.width / b.width, a.height / b.height);
                if (1 > d) y = Math.round(b.width * d),
                k = Math.round(b.height * d);
                else if (a.quality && "image/jpeg" === e) y = b.width,
                k = b.height;
                else {
                    f({
                        success: !1
                    });
                    return
                }
                c.width = y;
                c.height = k;
                m = b;
                var G = m.naturalWidth,
                l = m.naturalHeight,
                r = y,
                s = k,
                I = c.getContext("2d");
                I.save();
                var A;
                A = m.naturalWidth;
                if (1048576 < A * m.naturalHeight) {
                    var z = n.createElement("canvas");
                    z.width = z.height = 1;
                    z = z.getContext("2d");
                    z.drawImage(m, -A + 1, 0);
                    A = 0 === z.getImageData(0, 0, 1, 1).data[3]
                } else A = !1;
                A && (G /= 2, l /= 2);
                A = n.createElement("canvas");
                A.width = A.height = 1024;
                var z = A.getContext("2d"),
                w;
                w = l;
                var x = n.createElement("canvas");
                x.width = 1;
                x.height = w;
                x = x.getContext("2d");
                x.drawImage(m, 0, 0);
                for (var x = x.getImageData(0, 0, 1, w).data, t = 0, C = w, F = w; F > t;) 0 === x[4 * (F - 1) + 3] ? C = F: t = F,
                F = C + t >> 1;
                w = F / w;
                w = 0 === w ? 1 : w;
                for (x = 0; x < l;) {
                    t = x + 1024 > l ? l - x: 1024;
                    for (C = 0; C < G;) {
                        F = C + 1024 > G ? G - C: 1024;
                        z.clearRect(0, 0, 1024, 1024);
                        z.drawImage(m, -C, -x);
                        var v = C * r / G << 0,
                        J = Math.ceil(F * r / G),
                        D = x * s / l / w << 0,
                        K = Math.ceil(t * s / l / w);
                        I.drawImage(A, 0, 0, F, t, v, D, J, K);
                        C += 1024
                    }
                    x += 1024
                }
                I.restore();
                "image/jpeg" === e && (p = new H(atob(u.substring(u.indexOf("base64,") + 7))), p.headers && p.headers.length && (m = new B, m.init(p.get("exif")[0]) && (m.setExif("PixelXDimension", y), m.setExif("PixelYDimension", k), p.set("exif", m.getBinary()), q.hasEventListener("ExifData") && q.trigger("ExifData", g, m.EXIF()), q.hasEventListener("GpsData") && q.trigger("GpsData", g, m.GPS()))));
                if (a.quality && "image/jpeg" === e) try {
                    u = c.toDataURL(e, a.quality / 100)
                } catch(L) {
                    u = c.toDataURL(e)
                } else u = c.toDataURL(e);
                u = u.substring(u.indexOf("base64,") + 7);
                u = atob(u);
                p && p.headers && p.headers.length && (u = p.restore(u), p.purge());
                c.parentNode.removeChild(c);
                f({
                    success: !0,
                    data: u
                })
            };
            b.src = u
        })
    }
    function k() {
        function g(a, b) {
            var d = e ? 0 : -8 * (b - 1),
            q = 0,
            h;
            for (h = 0; h < b; h++) q |= f.charCodeAt(a + h) << Math.abs(d + 8 * h);
            return q
        }
        function a(a, b, d) {
            d = 3 === arguments.length ? d: f.length - b - 1;
            f = f.substr(0, b) + a + f.substr(d + b)
        }
        var e = !1,
        f;
        return {
            II: function(a) {
                if (a === t) return e;
                e = a
            },
            init: function(a) {
                e = !1;
                f = a
            },
            SEGMENT: function(c, b, d) {
                switch (arguments.length) {
                case 1:
                    return f.substr(c, f.length - c - 1);
                case 2:
                    return f.substr(c, b);
                case 3:
                    a(d, c, b);
                    break;
                default:
                    return f
                }
            },
            BYTE: function(a) {
                return g(a, 1)
            },
            SHORT: function(a) {
                return g(a, 2)
            },
            LONG: function(c, b) {
                if (b === t) return g(c, 4);
                var d = "",
                f = e ? 0 : -24,
                u;
                for (u = 0; 4 > u; u++) d += String.fromCharCode(b >> Math.abs(f + 8 * u) & 255);
                a(d, c, 4)
            },
            SLONG: function(a) {
                a = g(a, 4);
                return 2147483647 < a ? a - 4294967296 : a
            },
            STRING: function(a, b) {
                var d = "";
                for (b += a; a < b; a++) d += String.fromCharCode(g(a, 1));
                return d
            }
        }
    }
    function H(g) {
        var a = {
            65505 : {
                app: "EXIF",
                name: "APP1",
                signature: "Exif\x00"
            },
            65506 : {
                app: "ICC",
                name: "APP2",
                signature: "ICC_PROFILE\x00"
            },
            65517 : {
                app: "IPTC",
                name: "APP13",
                signature: "Photoshop 3.0\x00"
            }
        },
        e = [],
        f,
        c,
        b = t,
        d = 0;
        f = new k;
        f.init(g);
        if (65496 === f.SHORT(0)) {
            c = 2;
            for (g = Math.min(1048576, g.length); c <= g;) if (b = f.SHORT(c), 65488 <= b && 65495 >= b) c += 2;
            else {
                if (65498 === b || 65497 === b) break;
                d = f.SHORT(c + 2) + 2;
                a[b] && f.STRING(c + 4, a[b].signature.length) === a[b].signature && e.push({
                    hex: b,
                    app: a[b].app.toUpperCase(),
                    name: a[b].name.toUpperCase(),
                    start: c,
                    length: d,
                    segment: f.SEGMENT(c, d)
                });
                c += d
            }
            f.init(null);
            return {
                headers: e,
                restore: function(a) {
                    f.init(a);
                    var b = new H(a);
                    if (!b.headers) return ! 1;
                    for (a = b.headers.length; 0 < a; a--) {
                        var d = b.headers[a - 1];
                        f.SEGMENT(d.start, d.length, "")
                    }
                    b.purge();
                    c = 65504 == f.SHORT(2) ? 4 + f.SHORT(4) : 2;
                    a = 0;
                    for (b = e.length; a < b; a++) f.SEGMENT(c, 0, e[a].segment),
                    c += e[a].length;
                    return f.SEGMENT()
                },
                get: function(a) {
                    for (var b = [], d = 0, c = e.length; d < c; d++) e[d].app === a.toUpperCase() && b.push(e[d].segment);
                    return b
                },
                set: function(a, b) {
                    var d = [];
                    "string" === typeof b ? d.push(b) : d = b;
                    for (var c = ii = 0,
                    g = e.length; c < g && !(e[c].app === a.toUpperCase() && (e[c].segment = d[ii], e[c].length = d[ii].length, ii++), ii >= d.length); c++);
                },
                purge: function() {
                    e = [];
                    f.init(null)
                }
            }
        }
    }
    function B() {
        function h(b, d) {
            var e = a.SHORT(b),
            g,
            h,
            k,
            p,
            m,
            l = [],
            n = {};
            for (g = 0; g < e; g++) if (m = b + 12 * g + 2, k = d[a.SHORT(m)], k !== t) {
                h = a.SHORT(m += 2);
                p = a.LONG(m += 2);
                m += 4;
                l = [];
                switch (h) {
                case 1:
                case 7:
                    4 < p && (m = a.LONG(m) + f.tiffHeader);
                    for (h = 0; h < p; h++) l[h] = a.BYTE(m + h);
                    break;
                case 2:
                    4 < p && (m = a.LONG(m) + f.tiffHeader);
                    n[k] = a.STRING(m, p - 1);
                    continue;
                case 3:
                    2 < p && (m = a.LONG(m) + f.tiffHeader);
                    for (h = 0; h < p; h++) l[h] = a.SHORT(m + 2 * h);
                    break;
                case 4:
                    1 < p && (m = a.LONG(m) + f.tiffHeader);
                    for (h = 0; h < p; h++) l[h] = a.LONG(m + 4 * h);
                    break;
                case 5:
                    m = a.LONG(m) + f.tiffHeader;
                    for (h = 0; h < p; h++) l[h] = a.LONG(m + 4 * h) / a.LONG(m + 4 * h + 4);
                    break;
                case 9:
                    m = a.LONG(m) + f.tiffHeader;
                    for (h = 0; h < p; h++) l[h] = a.SLONG(m + 4 * h);
                    break;
                case 10:
                    m = a.LONG(m) + f.tiffHeader;
                    for (h = 0; h < p; h++) l[h] = a.SLONG(m + 4 * h) / a.SLONG(m + 4 * h + 4);
                    break;
                default:
                    continue
                }
                p = 1 == p ? l[0] : l;
                c.hasOwnProperty(k) && "object" != typeof p ? n[k] = c[k][p] : n[k] = p
            }
            return n
        }
        var a, e, f = {},
        c;
        a = new k;
        e = {
            tiff: {
                274 : "Orientation",
                34665 : "ExifIFDPointer",
                34853 : "GPSInfoIFDPointer"
            },
            exif: {
                36864 : "ExifVersion",
                40961 : "ColorSpace",
                40962 : "PixelXDimension",
                40963 : "PixelYDimension",
                36867 : "DateTimeOriginal",
                33434 : "ExposureTime",
                33437 : "FNumber",
                34855 : "ISOSpeedRatings",
                37377 : "ShutterSpeedValue",
                37378 : "ApertureValue",
                37383 : "MeteringMode",
                37384 : "LightSource",
                37385 : "Flash",
                41986 : "ExposureMode",
                41987 : "WhiteBalance",
                41990 : "SceneCaptureType",
                41988 : "DigitalZoomRatio",
                41992 : "Contrast",
                41993 : "Saturation",
                41994 : "Sharpness"
            },
            gps: {
                0 : "GPSVersionID",
                1 : "GPSLatitudeRef",
                2 : "GPSLatitude",
                3 : "GPSLongitudeRef",
                4 : "GPSLongitude"
            }
        };
        c = {
            ColorSpace: {
                1 : "sRGB",
                0 : "Uncalibrated"
            },
            MeteringMode: {
                0 : "Unknown",
                1 : "Average",
                2 : "CenterWeightedAverage",
                3 : "Spot",
                4 : "MultiSpot",
                5 : "Pattern",
                6 : "Partial",
                255 : "Other"
            },
            LightSource: {
                1 : "Daylight",
                2 : "Fliorescent",
                3 : "Tungsten",
                4 : "Flash",
                9 : "Fine weather",
                10 : "Cloudy weather",
                11 : "Shade",
                12 : "Daylight fluorescent (D 5700 - 7100K)",
                13 : "Day white fluorescent (N 4600 -5400K)",
                14 : "Cool white fluorescent (W 3900 - 4500K)",
                15 : "White fluorescent (WW 3200 - 3700K)",
                17 : "Standard light A",
                18 : "Standard light B",
                19 : "Standard light C",
                20 : "D55",
                21 : "D65",
                22 : "D75",
                23 : "D50",
                24 : "ISO studio tungsten",
                255 : "Other"
            },
            Flash: {
                0 : "Flash did not fire.",
                1 : "Flash fired.",
                5 : "Strobe return light not detected.",
                7 : "Strobe return light detected.",
                9 : "Flash fired, compulsory flash mode",
                13 : "Flash fired, compulsory flash mode, return light not detected",
                15 : "Flash fired, compulsory flash mode, return light detected",
                16 : "Flash did not fire, compulsory flash mode",
                24 : "Flash did not fire, auto mode",
                25 : "Flash fired, auto mode",
                29 : "Flash fired, auto mode, return light not detected",
                31 : "Flash fired, auto mode, return light detected",
                32 : "No flash function",
                65 : "Flash fired, red-eye reduction mode",
                69 : "Flash fired, red-eye reduction mode, return light not detected",
                71 : "Flash fired, red-eye reduction mode, return light detected",
                73 : "Flash fired, compulsory flash mode, red-eye reduction mode",
                77 : "Flash fired, compulsory flash mode, red-eye reduction mode, return light not detected",
                79 : "Flash fired, compulsory flash mode, red-eye reduction mode, return light detected",
                89 : "Flash fired, auto mode, red-eye reduction mode",
                93 : "Flash fired, auto mode, return light not detected, red-eye reduction mode",
                95 : "Flash fired, auto mode, return light detected, red-eye reduction mode"
            },
            ExposureMode: {
                0 : "Auto exposure",
                1 : "Manual exposure",
                2 : "Auto bracket"
            },
            WhiteBalance: {
                0 : "Auto white balance",
                1 : "Manual white balance"
            },
            SceneCaptureType: {
                0 : "Standard",
                1 : "Landscape",
                2 : "Portrait",
                3 : "Night scene"
            },
            Contrast: {
                0 : "Normal",
                1 : "Soft",
                2 : "Hard"
            },
            Saturation: {
                0 : "Normal",
                1 : "Low saturation",
                2 : "High saturation"
            },
            Sharpness: {
                0 : "Normal",
                1 : "Soft",
                2 : "Hard"
            },
            GPSLatitudeRef: {
                N: "North latitude",
                S: "South latitude"
            },
            GPSLongitudeRef: {
                E: "East longitude",
                W: "West longitude"
            }
        };
        return {
            init: function(b) {
                f = {
                    tiffHeader: 10
                };
                if (b === t || !b.length) return ! 1;
                a.init(b);
                return 65505 === a.SHORT(0) && "EXIF\x00" === a.STRING(4, 5).toUpperCase() ? (b = t, b = f.tiffHeader, a.II(18761 == a.SHORT(b)), 42 !== a.SHORT(b += 2) ? b = !1 : (f.IFD0 = f.tiffHeader + a.LONG(b + 2), b = h(f.IFD0, e.tiff), f.exifIFD = "ExifIFDPointer" in b ? f.tiffHeader + b.ExifIFDPointer: t, f.gpsIFD = "GPSInfoIFDPointer" in b ? f.tiffHeader + b.GPSInfoIFDPointer: t, b = !0), b) : !1
            },
            EXIF: function() {
                var a;
                a = h(f.exifIFD, e.exif);
                if (a.ExifVersion && "array" === g.typeOf(a.ExifVersion)) {
                    for (var d = 0,
                    c = ""; d < a.ExifVersion.length; d++) c += String.fromCharCode(a.ExifVersion[d]);
                    a.ExifVersion = c
                }
                return a
            },
            GPS: function() {
                var a;
                a = h(f.gpsIFD, e.gps);
                a.GPSVersionID && (a.GPSVersionID = a.GPSVersionID.join("."));
                return a
            },
            setExif: function(b, d) {
                if ("PixelXDimension" !== b && "PixelYDimension" !== b) return ! 1;
                var c;
                c = b;
                var g, h, k, p = 0;
                if ("string" === typeof c) for (hex in g = e.exif, g) if (g[hex] === c) {
                    c = hex;
                    break
                }
                g = f.exifIFD;
                h = a.SHORT(g);
                for (i = 0; i < h; i++) if (k = g + 12 * i + 2, a.SHORT(k) == c) {
                    p = k + 8;
                    break
                }
                p ? (a.LONG(p, d), c = !0) : c = !1;
                return c
            },
            getBinary: function() {
                return a.SEGMENT()
            }
        }
    }
    var s = {},
    l;
    g.runtimes.Html5 = g.addRuntime("html5", {
        getFeatures: function() {
            var h, a, e, f, c, b;
            a = e = c = b = !1;
            r.XMLHttpRequest && (h = new XMLHttpRequest, e = !!h.upload, a = !(!h.sendAsBinary && !h.upload));
            a && (f = !!(h.sendAsBinary || r.Uint8Array && r.ArrayBuffer), c = !(!File || !File.prototype.getAsDataURL && !r.FileReader || !f), b = !(!File || !(File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice)));
            l = g.ua.safari && g.ua.windows;
            h = a;
            a = n.createElement("div");
            return {
                html5: h,
                dragdrop: "draggable" in a || "ondragstart" in a && "ondrop" in a,
                jpgresize: c,
                pngresize: c,
                multipart: c || !!r.FileReader || !!r.FormData,
                canSendBinary: f,
                cantSendBlobInFormData: !(!(g.ua.gecko && r.FormData && r.FileReader) || FileReader.prototype.readAsArrayBuffer) || g.ua.android,
                progress: e,
                chunks: b,
                multi_selection: !(g.ua.safari && g.ua.windows),
                triggerDialog: g.ua.gecko && r.FormData || g.ua.webkit || r.FileReader
            }
        },
        init: function(h, a) {
            function e(a) {
                var d, c, e = [],
                f,
                k = {};
                for (c = 0; c < a.length; c++) d = a[c],
                k[d.name] && g.ua.safari && g.ua.windows || (k[d.name] = !0, f = g.guid(), s[f] = d, e.push(new g.File(f, d.fileName || d.name, d.fileSize || d.size)));
                e.length && h.trigger("FilesAdded", e)
            }
            var f, c;
            f = this.getFeatures();
            f.html5 ? (h.bind("Init",
            function(a) {
                var d, c, f = [],
                k,
                l,
                p = a.settings.filters,
                m,
                r;
                k = n.body;
                d = n.createElement("div");
                d.id = a.id + "_html5_container";
                g.extend(d.style, {
                    position: "absolute",
                    background: h.settings.shim_bgcolor || "transparent",
                    width: "100px",
                    height: "100px",
                    overflow: "hidden",
                    zIndex: 99999,
                    opacity: h.settings.shim_bgcolor ? "": 0
                });
                d.className = "plupload html5";
                h.settings.container && (k = $(h.settings.container).get(0), "static" === g.getStyle(k, "position") && (k.style.position = "relative"));
                k.appendChild(d);
                k = 0;
                a: for (; k < p.length; k++) for (m = p[k].extensions.split(/,/), l = 0; l < m.length; l++) {
                    if ("*" === m[l]) {
                        f = [];
                        break a
                    } (r = g.mimeTypes[m[l]]) && -1 === g.inArray(r, f) && f.push(r)
                }
                d.innerHTML = '\x3cinput id\x3d"' + h.id + '_html5"  style\x3d"font-size:999px" type\x3d"file" accept\x3d"' + f.join(",") + '" ' + (h.settings.multi_selection && h.features.multi_selection ? 'multiple\x3d"multiple"': "") + " /\x3e";
                d.scrollTop = 100;
                f = n.getElementById(h.id + "_html5");
                a.features.triggerDialog ? g.extend(f.style, {
                    position: "absolute",
                    width: "100%",
                    height: "100%"
                }) : g.extend(f.style, {
                    cssFloat: "right",
                    styleFloat: "right"
                });
                f.onchange = function() {
                    e(this.files);
                    this.value = ""
                };
                if (c = $(h.settings.browse_button).get(0)) {
                    var s = a.settings.browse_button_hover,
                    t = a.settings.browse_button_active;
                    d = a.features.triggerDialog ? c: d;
                    s && (g.addEvent(d, "mouseover",
                    function() {
                        g.addClass(c, s)
                    },
                    a.id), g.addEvent(d, "mouseout",
                    function() {
                        g.removeClass(c, s)
                    },
                    a.id));
                    t && (g.addEvent(d, "mousedown",
                    function() {
                        g.addClass(c, t)
                    },
                    a.id), g.addEvent(n.body, "mouseup",
                    function() {
                        g.removeClass(c, t)
                    },
                    a.id));
                    a.features.triggerDialog && g.addEvent(c, "click",
                    function(d) {
                        var c = n.getElementById(a.id + "_html5");
                        c && !c.disabled && c.click();
                        d.preventDefault()
                    },
                    a.id)
                }
            }), h.bind("PostInit",
            function() {
                var a = $(h.settings.drop_element).get(0);
                a && (l ? g.addEvent(a, "dragenter",
                function(d) {
                    var c;
                    c = n.getElementById(h.id + "_drop");
                    c || (c = n.createElement("input"), c.setAttribute("type", "file"), c.setAttribute("id", h.id + "_drop"), c.setAttribute("multiple", "multiple"), g.addEvent(c, "change",
                    function() {
                        e(this.files);
                        g.removeEvent(c, "change", h.id);
                        c.parentNode.removeChild(c)
                    },
                    h.id), g.addEvent(c, "dragover",
                    function(a) {
                        a.stopPropagation()
                    },
                    h.id), a.appendChild(c));
                    g.getPos(a, $(h.settings.container).get(0));
                    d = g.getSize(a);
                    "static" === g.getStyle(a, "position") && g.extend(a.style, {
                        position: "relative"
                    });
                    g.extend(c.style, {
                        position: "absolute",
                        display: "block",
                        top: 0,
                        left: 0,
                        width: d.w + "px",
                        height: d.h + "px",
                        opacity: 0
                    })
                },
                h.id) : (g.addEvent(a, "dragover",
                function(a) {
                    a.preventDefault()
                },
                h.id), g.addEvent(a, "drop",
                function(a) {
                    var b = a.dataTransfer;
                    b && b.files && e(b.files);
                    a.preventDefault()
                },
                h.id)))
            }), h.bind("Refresh",
            function(a) {
                var c, e, f;
                if (c = $(h.settings.browse_button).get(0)) e = g.getPos(c, $(a.settings.container).get(0)),
                f = g.getSize(c),
                a = n.getElementById(h.id + "_html5_container"),
                g.extend(a.style, {
                    top: e.y + "px",
                    left: e.x + "px",
                    width: f.w + "px",
                    height: f.h + "px"
                }),
                h.features.triggerDialog && ("static" === g.getStyle(c, "position") && g.extend(c.style, {
                    position: "relative"
                }), e = parseInt(g.getStyle(c, "zIndex"), 10), isNaN(e) && (e = 0), g.extend(c.style, {
                    zIndex: e
                }), g.extend(a.style, {
                    zIndex: e - 1
                }))
            }), h.bind("DisableBrowse",
            function(a, c) {
                var e = n.getElementById(a.id + "_html5");
                e && (e.disabled = c)
            }), h.bind("CancelUpload",
            function() {
                c && c.abort && c.abort()
            }), h.bind("UploadFile",
            function(a, d) {
                function e(a, b, c) {
                    var d;
                    if (File.prototype.slice) try {
                        return a.slice(),
                        a.slice(b, c)
                    } catch(f) {
                        return a.slice(b, c - b)
                    } else return (d = File.prototype.webkitSlice || File.prototype.mozSlice) ? d.call(a, b, c) : null
                }
                function h(l) {
                    function m() {
                        function h(d) {
                            if (c.sendAsBinary) c.sendAsBinary(d);
                            else if (a.features.canSendBinary) {
                                for (var e = new Uint8Array(d.length), f = 0; f < d.length; f++) e[f] = d.charCodeAt(f) & 255;
                                c.send(e.buffer)
                            }
                        }
                        function s(e) {
                            var k = 0,
                            q = "----pluploadboundary" + g.guid(),
                            y,
                            v = "";
                            c = new XMLHttpRequest;
                            c.upload && (c.upload.onprogress = function(c) {
                                var e = d.loaded;
                                d.loaded = Math.min(d.size, u + c.loaded - k);
                                d.loaded = Math.max(e, d.loaded);
                                a.trigger("UploadProgress", d)
                            });
                            c.onreadystatechange = function() {
                                var f, h;
                                if (4 == c.readyState && a.state !== g.STOPPED) {
                                    try {
                                        f = c.status
                                    } catch(k) {
                                        f = 0
                                    }
                                    if (400 <= f) a.trigger("Error", {
                                        code: g.HTTP_ERROR,
                                        message: g.translate("HTTP Error."),
                                        file: d,
                                        status: f
                                    });
                                    else {
                                        if (A) {
                                            h = {
                                                chunk: n,
                                                chunks: A,
                                                response: c.responseText,
                                                status: f
                                            };
                                            a.trigger("ChunkUploaded", d, h);
                                            u += x;
                                            if (h.cancelled) {
                                                d.status = g.FAILED;
                                                return
                                            }
                                            d.loaded = Math.min(d.size, (n + 1) * w)
                                        } else d.loaded = d.size;
                                        a.trigger("UploadProgress", d);
                                        e = t = y = v = null; ! A || ++n >= A ? (d.status = g.DONE, a.trigger("FileUploaded", d, {
                                            response: c.responseText,
                                            status: f
                                        })) : $.ajax({
                                            type: "get",
                                            url: a.settings.chunk_check_url,
                                            dataType: "json",
                                            data: {
                                                size: l.size,
                                                lastModified: l.lastModified,
                                                fileName: l.name,
                                                tenantKey: TEAMS.currentTenant.tenantKey
                                            },
                                            success: function(a) {
                                                n = a.nowChunk;
                                                m()
                                            }
                                        })
                                    }
                                }
                            };
                            if (a.settings.multipart && f.multipart) {
                                z.name = d.target_name || d.name;
                                c.open("post", C, !0);
                                g.each(a.settings.headers,
                                function(a, b) {
                                    c.setRequestHeader(b, a)
                                });
                                if ("string" !== typeof e && r.FormData) {
                                    y = new FormData;
                                    g.each(g.extend(z, a.settings.multipart_params),
                                    function(a, b) {
                                        y.append(b, a)
                                    });
                                    y.append(a.settings.file_data_name, e);
                                    c.send(y);
                                    return
                                }
                                if ("string" === typeof e) {
                                    c.setRequestHeader("Content-Type", "multipart/form-data; boundary\x3d" + q);
                                    g.each(g.extend(z, a.settings.multipart_params),
                                    function(a, b) {
                                        v += "--" + q + '\r\nContent-Disposition: form-data; name\x3d"' + b + '"\r\n\r\n';
                                        v += unescape(encodeURIComponent(a)) + "\r\n"
                                    });
                                    B = g.mimeTypes[d.name.replace(/^.+\.([^.]+)/, "$1").toLowerCase()] || "application/octet-stream";
                                    v += "--" + q + '\r\nContent-Disposition: form-data; name\x3d"' + a.settings.file_data_name + '"; filename\x3d"' + unescape(encodeURIComponent(d.name)) + '"\r\nContent-Type: ' + B + "\r\n\r\n" + e + "\r\n--" + q + "--\r\n";
                                    k = v.length - e.length;
                                    e = v;
                                    h(e);
                                    return
                                }
                            }
                            C = g.buildUrl(a.settings.url, g.extend(z, a.settings.multipart_params));
                            c.open("post", C, !0);
                            c.setRequestHeader("Content-Type", "application/octet-stream");
                            g.each(a.settings.headers,
                            function(a, b) {
                                c.setRequestHeader(b, a)
                            });
                            "string" === typeof e ? h(e) : c.send(e)
                        }
                        var t, A, z, w, x, B, C = a.settings.url;
                        d.status != g.DONE && d.status != g.FAILED && a.state != g.STOPPED && (z = {
                            name: d.target_name || d.name
                        },
                        z.size = l.size, z.lastModified = l.lastModified, k.chunk_size && d.size > k.chunk_size && (f.chunks || "string" == typeof l) ? (w = k.chunk_size, A = Math.ceil(d.size / w), x = Math.min(w, d.size - n * w), t = "string" == typeof l ? l.substring(n * w, n * w + x) : e(l, n * w, n * w + x), z.chunk = n, z.chunks = A) : (x = d.size, t = l), a.settings.multipart && f.multipart && "string" !== typeof t && r.FileReader && f.cantSendBlobInFormData && f.chunks && a.settings.chunk_size ?
                        function() {
                            var a = new FileReader;
                            a.onload = function() {
                                s(a.result);
                                a = null
                            };
                            a.readAsBinaryString(t)
                        } () : s(t))
                    }
                    var n = 0,
                    u = 0;
                    a.settings.chunk_size && a.settings.chunk_check_url ? $.ajax({
                        type: "get",
                        url: a.settings.chunk_check_url,
                        dataType: "json",
                        data: {
                            size: l.size,
                            lastModified: l.lastModified,
                            fileName: l.name,
                            tenantKey: TEAMS.currentTenant.tenantKey
                        },
                        success: function(a) {
                            n = a.nowChunk;
                            m()
                        }
                    }) : m()
                }
                var k = a.settings,
                l;
                l = s[d.id];
                f.jpgresize && a.settings.resize && /\.(png|jpg|jpeg)$/i.test(d.name) ? v.call(a, d, a.settings.resize, /\.png$/i.test(d.name) ? "image/png": "image/jpeg",
                function(a) {
                    a.success ? (d.size = a.data.length, h(a.data)) : f.chunks ? h(l) : E(l, h)
                }) : !f.chunks && f.jpgresize ? E(l, h) : h(l)
            }), h.bind("Destroy",
            function(a) {
                var c, e, f = n.body,
                h = {
                    inputContainer: a.id + "_html5_container",
                    inputFile: a.id + "_html5",
                    browseButton: a.settings.browse_button,
                    dropElm: a.settings.drop_element
                };
                for (c in h)(e = $(h[c]).get(0)) && g.removeAllEvents(e, a.id);
                g.removeAllEvents(n.body, a.id);
                a.settings.container && (f = $(a.settings.container).get(0));
                f && h.inputContainer && n.getElementById(h.inputContainer) && f.removeChild(n.getElementById(h.inputContainer))
            }), a({
                success: !0
            })) : a({
                success: !1
            })
        }
    })
})(window, document, plupload); (function(m, Ha) {
    var q = m.document,
    x = m.navigator,
    F = m.setTimeout,
    Y = m.Number.parseInt || m.parseInt,
    L = m.Number.parseFloat || m.parseFloat,
    Z = m.Number.isNaN || m.isNaN,
    M = m.encodeURIComponent,
    G = m.Math,
    $ = m.Date,
    aa = m.ActiveXObject,
    ba = m.Array.prototype.slice,
    N = m.Object.keys,
    r = m.Object.prototype.hasOwnProperty,
    O = function() {
        var a;
        if (a = "function" === typeof m.Object.defineProperty) try {
            var b = {};
            m.Object.defineProperty(b, "y", {
                value: "z"
            });
            a = "z" === b.y
        } catch(c) {
            a = !1
        }
        if (a) return m.Object.defineProperty
    } (),
    k = function(a) {
        return ba.call(a, 0)
    },
    v = function(a, b, c) {
        if ("function" === typeof b.indexOf) return b.indexOf(a, c);
        var d = b.length;
        for ("undefined" === typeof c ? c = 0 : 0 > c && (c = d + c); c < d; c++) if (r.call(b, c) && b[c] === a) return c;
        return - 1
    },
    s = function() {
        var a, b, c, d, e, f = k(arguments),
        g = f[0] || {};
        a = 1;
        for (b = f.length; a < b; a++) if (null != (c = f[a])) for (d in c) r.call(c, d) && (e = c[d], g !== e && void 0 !== e && (g[d] = e));
        return g
    },
    z = function(a) {
        var b, c, d;
        if ("object" !== typeof a || null == a) b = a;
        else if ("number" === typeof a.length) for (b = [], c = 0, d = a.length; c < d; c++) r.call(a, c) && (b[c] = z(a[c]));
        else for (c in b = {},
        a) r.call(a, c) && (b[c] = z(a[c]));
        return b
    },
    P = function(a) {
        if (null == a) return [];
        if (N) return N(a);
        var b = [],
        c;
        for (c in a) r.call(a, c) && b.push(c);
        return b
    },
    Q = function(a) {
        return function() {
            return a.now ? a.now() : (new a).getTime()
        }
    } ($),
    R = function(a, b) {
        if (a && 1 === a.nodeType && b && (1 === b.nodeType || 9 === b.nodeType)) {
            do {
                if (a === b) return ! 0;
                a = a.parentNode
            } while ( a )
        }
        return ! 1
    },
    l = {
        bridge: null,
        version: "0.0.0",
        pluginType: "unknown",
        disabled: null,
        outdated: null,
        unavailable: null,
        deactivated: null,
        overdue: null,
        ready: null
    },
    y = {},
    w,
    p = {},
    H = null,
    ca = {
        ready: "Flash communication is established",
        error: {
            "flash-disabled": "Flash is disabled or not installed",
            "flash-outdated": "Flash is too outdated to support ZeroClipboard",
            "flash-unavailable": "Flash is unable to communicate bidirectionally with JavaScript",
            "flash-deactivated": "Flash is too outdated for your browser and/or is configured as click-to-activate",
            "flash-overdue": "Flash communication was established but NOT within the acceptable time limit"
        }
    },
    h = {
        swfPath: function() {
            var a, b, c, d, e = "ZeroClipboard.swf";
            if (!q.currentScript || !(d = q.currentScript.src)) {
                var f = q.getElementsByTagName("script");
                if ("readyState" in f[0]) for (a = f.length; a--&&("interactive" !== f[a].readyState || !(d = f[a].src)););
                else if ("loading" === q.readyState) d = f[f.length - 1].src;
                else {
                    for (a = f.length; a--;) {
                        c = f[a].src;
                        if (!c) {
                            b = null;
                            break
                        }
                        c = c.split("#")[0].split("?")[0];
                        c = c.slice(0, c.lastIndexOf("/") + 1);
                        if (null == b) b = c;
                        else if (b !== c) {
                            b = null;
                            break
                        }
                    }
                    null !== b && (d = b)
                }
            }
            d && (d = d.split("#")[0].split("?")[0], e = d.slice(0, d.lastIndexOf("/") + 1) + e);
            return e
        } (),
        trustedDomains: m.location.host ? [m.location.host] : [],
        cacheBust: !0,
        forceEnhancedClipboard: !1,
        flashLoadTimeout: 3E4,
        autoActivate: !0,
        bubbleEvents: !0,
        containerId: "global-zeroclipboard-html-bridge",
        containerClass: "global-zeroclipboard-container",
        swfObjectId: "global-zeroclipboard-flash-bridge",
        hoverClass: "zeroclipboard-is-hover",
        activeClass: "zeroclipboard-is-active",
        forceHandCursor: !1,
        title: null,
        zIndex: 999999999
    },
    da = function(a) {
        if ("object" === typeof a && null !== a) for (var b in a) if (r.call(a, b)) if (/^(?:forceHandCursor|title|zIndex|bubbleEvents)$/.test(b)) h[b] = a[b];
        else if (null == l.bridge) if ("containerId" === b || "swfObjectId" === b) {
            var c = a[b];
            if ("string" === typeof c && c && /^[A-Za-z][A-Za-z0-9_:\-\.]*$/.test(c)) h[b] = a[b];
            else throw Error("The specified `" + b + "` value is not valid as an HTML4 Element ID");
        } else h[b] = a[b];
        if ("string" === typeof a && a) {
            if (r.call(h, a)) return h[a]
        } else return z(h)
    },
    ea = function() {
        for (var a = ["userAgent", "platform", "appName"], b = {},
        c = 0, d = a.length; c < d; c++) a[c] in x && (b[a[c]] = x[a[c]]);
        var a = ["bridge"],
        c = {},
        e;
        for (e in l) - 1 === v(e, a) && (c[e] = l[e]);
        return {
            browser: b,
            flash: c,
            zeroclipboard: {
                version: g.version,
                config: g.config()
            }
        }
    },
    fa = function() {
        return !! (l.disabled || l.outdated || l.unavailable || l.deactivated)
    },
    ga = function(a, b) {
        var c, d, e, f = {};
        if ("string" === typeof a && a) e = a.toLowerCase().split(/\s+/);
        else if ("object" === typeof a && a && "undefined" === typeof b) for (c in a) if (r.call(a, c) && "string" === typeof c && c && "function" === typeof a[c]) g.on(c, a[c]);
        if (e && e.length) {
            c = 0;
            for (d = e.length; c < d; c++) a = e[c].replace(/^on/, ""),
            f[a] = !0,
            y[a] || (y[a] = []),
            y[a].push(b);
            f.ready && l.ready && g.emit({
                type: "ready"
            });
            if (f.error) for (e = ["disabled", "outdated", "unavailable", "deactivated", "overdue"], c = 0, d = e.length; c < d; c++) if (!0 === l[e[c]]) {
                g.emit({
                    type: "error",
                    name: "flash-" + e[c]
                });
                break
            }
        }
        return g
    },
    ha = function(a, b) {
        var c, d, e, f, n;
        if (0 === arguments.length) f = P(y);
        else if ("string" === typeof a && a) f = a.split(/\s+/);
        else if ("object" === typeof a && a && "undefined" === typeof b) for (c in a) r.call(a, c) && "string" === typeof c && c && "function" === typeof a[c] && g.off(c, a[c]);
        if (f && f.length) for (c = 0, d = f.length; c < d; c++) if (a = f[c].toLowerCase().replace(/^on/, ""), (n = y[a]) && n.length) if (b) for (e = v(b, n); - 1 !== e;) n.splice(e, 1),
        e = v(b, n, e);
        else n.length = 0;
        return g
    },
    ia = function(a) {
        return "string" === typeof a && a ? z(y[a]) || null: z(y)
    },
    ja = function(a) {
        var b, c, d;
        if (a = S(a)) {
            b = a;
            var e = b.target || w || null,
            f = "swf" === b._source;
            delete b._source;
            switch (b.type) {
            case "error":
                v(b.name, ["flash-disabled", "flash-outdated", "flash-deactivated", "flash-overdue"]) && s(l, {
                    disabled: "flash-disabled" === b.name,
                    outdated: "flash-outdated" === b.name,
                    unavailable: "flash-unavailable" === b.name,
                    deactivated: "flash-deactivated" === b.name,
                    overdue: "flash-overdue" === b.name,
                    ready: !1
                });
                break;
            case "ready":
                e = !0 === l.deactivated;
                s(l, {
                    disabled: !1,
                    outdated: !1,
                    unavailable: !1,
                    deactivated: !1,
                    overdue: e,
                    ready: !e
                });
                break;
            case "copy":
                var n, u, e = b.relatedTarget; ! p["text/html"] && !p["text/plain"] && e && (u = e.value || e.outerHTML || e.innerHTML) && (n = e.value || e.textContent || e.innerText) ? (b.clipboardData.clearData(), b.clipboardData.setData("text/plain", n), u !== n && b.clipboardData.setData("text/html", u)) : !p["text/plain"] && b.target && (n = b.target.getAttribute("data-clipboard-text")) && (b.clipboardData.clearData(), b.clipboardData.setData("text/plain", n));
                break;
            case "aftercopy":
                g.clearData();
                if (n = e) {
                    var k;
                    try {
                        k = q.activeElement
                    } catch(t) {
                        k = null
                    }
                    n = e !== k
                }
                n && e.focus && e.focus();
                break;
            case "_mouseover":
                g.focus(e); ! 0 === h.bubbleEvents && f && (e && e !== b.relatedTarget && !R(b.relatedTarget, e) && A(s({},
                b, {
                    type: "mouseenter",
                    bubbles: !1,
                    cancelable: !1
                })), A(s({},
                b, {
                    type: "mouseover"
                })));
                break;
            case "_mouseout":
                g.blur(); ! 0 === h.bubbleEvents && f && (e && e !== b.relatedTarget && !R(b.relatedTarget, e) && A(s({},
                b, {
                    type: "mouseleave",
                    bubbles: !1,
                    cancelable: !1
                })), A(s({},
                b, {
                    type: "mouseout"
                })));
                break;
            case "_mousedown":
                T(e, h.activeClass); ! 0 === h.bubbleEvents && f && A(s({},
                b, {
                    type: b.type.slice(1)
                }));
                break;
            case "_mouseup":
                B(e, h.activeClass); ! 0 === h.bubbleEvents && f && A(s({},
                b, {
                    type: b.type.slice(1)
                }));
                break;
            case "_click":
            case "_mousemove":
                !0 === h.bubbleEvents && f && A(s({},
                b, {
                    type: b.type.slice(1)
                }))
            }
            b = /^_(?:click|mouse(?:over|out|down|up|move))$/.test(b.type) ? !0 : void 0;
            if (!b) {
                if ("ready" === a.type && !0 === l.overdue) return g.emit({
                    type: "error",
                    name: "flash-overdue"
                });
                b = s({},
                a);
                if ("object" === typeof b && b && b.type && (e = U(b), (n = (y["*"] || []).concat(y[b.type] || [])) && n.length)) {
                    var x, D;
                    u = 0;
                    for (k = n.length; u < k; u++) f = n[u],
                    x = this,
                    "string" === typeof f && "function" === typeof m[f] && (f = m[f]),
                    "object" === typeof f && f && "function" === typeof f.handleEvent && (x = f, f = f.handleEvent),
                    "function" === typeof f && (D = s({},
                    b), V(f, x, [D], e))
                }
                if ("copy" === a.type) {
                    c = {};
                    a = {};
                    if ("object" === typeof p && p) {
                        for (d in p) if (d && r.call(p, d) && "string" === typeof p[d] && p[d]) switch (d.toLowerCase()) {
                        case "text/plain":
                        case "text":
                        case "air:text":
                        case "flash:text":
                            c.text = p[d];
                            a.text = d;
                            break;
                        case "text/html":
                        case "html":
                        case "air:html":
                        case "flash:html":
                            c.html = p[d];
                            a.html = d;
                            break;
                        case "application/rtf":
                        case "text/rtf":
                        case "rtf":
                        case "richtext":
                        case "air:rtf":
                        case "flash:rtf":
                            c.rtf = p[d],
                            a.rtf = d
                        }
                        d = {
                            data: c,
                            formatMap: a
                        }
                    } else d = void 0;
                    c = d.data;
                    H = d.formatMap
                }
                return c
            }
        }
    },
    la = function() {
        "boolean" !== typeof l.ready && (l.ready = !1);
        if (!g.isFlashUnusable() && null === l.bridge) {
            var a = h.flashLoadTimeout;
            "number" === typeof a && 0 <= a && F(function() {
                "boolean" !== typeof l.deactivated && (l.deactivated = !0); ! 0 === l.deactivated && g.emit({
                    type: "error",
                    name: "flash-deactivated"
                })
            },
            a);
            l.overdue = !1;
            ka()
        }
    },
    na = function() {
        g.clearData();
        g.blur();
        g.emit("destroy");
        ma();
        g.off()
    },
    oa = function(a, b) {
        var c;
        if ("object" === typeof a && a && "undefined" === typeof b) c = a,
        g.clearData();
        else if ("string" === typeof a && a) c = {},
        c[a] = b;
        else return;
        for (var d in c)"string" === typeof d && d && r.call(c, d) && "string" === typeof c[d] && c[d] && (p[d] = c[d])
    },
    pa = function(a) {
        if ("undefined" === typeof a) {
            if (p) for (var b in p) r.call(p, b) && delete p[b];
            H = null
        } else "string" === typeof a && r.call(p, a) && delete p[a]
    },
    qa = function(a) {
        if ("undefined" === typeof a) return z(p);
        if ("string" === typeof a && r.call(p, a)) return p[a]
    },
    ra = function(a) {
        if (a && 1 === a.nodeType) {
            w && (B(w, h.activeClass), w !== a && B(w, h.hoverClass));
            w = a;
            T(a, h.hoverClass);
            var b = a.getAttribute("title") || h.title;
            if ("string" === typeof b && b) {
                var c = C(l.bridge);
                c && c.setAttribute("title", b)
            } (b = !0 === h.forceHandCursor) || (a = (b = m.getComputedStyle(a, null).getPropertyValue("cursor")) && "auto" !== b || "A" !== a.nodeName ? b: "pointer", b = "pointer" === a);
            a = b; ! 0 === l.ready && (l.bridge && "function" === typeof l.bridge.setHandCursor ? l.bridge.setHandCursor(a) : l.ready = !1);
            var d;
            w && (d = C(l.bridge)) && (a = W(w), s(d.style, {
                width: a.width + "px",
                height: a.height + "px",
                top: a.top + "px",
                left: a.left + "px",
                zIndex: "" + I(h.zIndex)
            }))
        }
    },
    sa = function() {
        var a = C(l.bridge);
        a && (a.removeAttribute("title"), a.style.left = "0px", a.style.top = "-9999px", a.style.width = "1px", a.style.top = "1px");
        w && (B(w, h.hoverClass), B(w, h.activeClass), w = null)
    },
    ta = function() {
        return w || null
    },
    S = function(a) {
        var b;
        "string" === typeof a && a ? (b = a, a = {}) : "object" === typeof a && a && "string" === typeof a.type && a.type && (b = a.type);
        if (b) {
            s(a, {
                type: b.toLowerCase(),
                target: a.target || w || null,
                relatedTarget: a.relatedTarget || null,
                currentTarget: l && l.bridge || null,
                timeStamp: a.timeStamp || Q() || null
            });
            b = ca[a.type];
            "error" === a.type && a.name && b && (b = b[a.name]);
            b && (a.message = b);
            "ready" === a.type && s(a, {
                target: null,
                version: l.version
            });
            "error" === a.type && (/^flash-(disabled|outdated|unavailable|deactivated|overdue)$/.test(a.name) && s(a, {
                target: null,
                minimumVersion: "11.0.0"
            }), /^flash-(outdated|unavailable|deactivated|overdue)$/.test(a.name) && s(a, {
                version: l.version
            }));
            "copy" === a.type && (a.clipboardData = {
                setData: g.setData,
                clearData: g.clearData
            });
            if ("aftercopy" === a.type && (b = H, "object" === typeof a && a && "object" === typeof b && b)) {
                var c = {},
                d;
                for (d in a) if (r.call(a, d)) if ("success" !== d && "data" !== d) c[d] = a[d];
                else {
                    c[d] = {};
                    var e = a[d],
                    f;
                    for (f in e) f && r.call(e, f) && r.call(b, f) && (c[d][b[f]] = e[f])
                }
                a = c
            }
            a.target && !a.relatedTarget && (d = a, f = (f = (f = a.target) && f.getAttribute && f.getAttribute("data-clipboard-target")) ? q.getElementById(f) : null, d.relatedTarget = f);
            if (a && /^_(?:click|mouse(?:over|out|down|up|move))$/.test(a.type)) {
                d = a.target;
                f = "_mouseover" === a.type && a.relatedTarget ? a.relatedTarget: void 0;
                b = "_mouseout" === a.type && a.relatedTarget ? a.relatedTarget: void 0;
                var e = W(d),
                c = e.left + ("number" === typeof a._stageX ? a._stageX: 0),
                e = e.top + ("number" === typeof a._stageY ? a._stageY: 0),
                n = c - (q.body.scrollLeft + q.documentElement.scrollLeft),
                h = e - (q.body.scrollTop + q.documentElement.scrollTop),
                k = (m.screenLeft || m.screenX || 0) + n,
                p = (m.screenTop || m.screenY || 0) + h,
                t = "number" === typeof a.movementX ? a.movementX: 0,
                D = "number" === typeof a.movementY ? a.movementY: 0;
                delete a._stageX;
                delete a._stageY;
                s(a, {
                    srcElement: d,
                    fromElement: f,
                    toElement: b,
                    screenX: k,
                    screenY: p,
                    pageX: c,
                    pageY: e,
                    clientX: n,
                    clientY: h,
                    x: n,
                    y: h,
                    movementX: t,
                    movementY: D,
                    offsetX: 0,
                    offsetY: 0,
                    layerX: 0,
                    layerY: 0
                })
            }
            return a
        }
    },
    U = function(a) {
        return ! /^(?:(?:before)?copy|destroy)$/.test(a && "string" === typeof a.type && a.type || "")
    },
    V = function(a, b, c, d) {
        d ? F(function() {
            a.apply(b, c)
        },
        0) : a.apply(b, c)
    },
    A = function(a) {
        if (a && "string" === typeof a.type && a) {
            var b, c = a.target || null;
            b = c && c.ownerDocument || q;
            a = s({
                view: b.defaultView || m,
                canBubble: !0,
                cancelable: !0,
                detail: "click" === a.type ? 1 : 0,
                button: "number" === typeof a.which ? a.which - 1 : "number" === typeof a.button ? a.button: b.createEvent ? 0 : 1
            },
            a);
            c && b.createEvent && c.dispatchEvent && (a = [a.type, a.canBubble, a.cancelable, a.view, a.detail, a.screenX, a.screenY, a.clientX, a.clientY, a.ctrlKey, a.altKey, a.shiftKey, a.metaKey, a.button, a.relatedTarget], b = b.createEvent("MouseEvents"), b.initMouseEvent && (b.initMouseEvent.apply(b, a), b._source = "js", c.dispatchEvent(b)))
        }
    },
    C = function(a) {
        for (a = a && a.parentNode; a && "OBJECT" === a.nodeName && a.parentNode;) a = a.parentNode;
        return a || null
    },
    ka = function() {
        var a, b = l.bridge,
        c = C(b);
        if (!b) {
            var b = ua(m.location.host, h),
            d = "never" === b ? "none": "all",
            e,
            f,
            n,
            u = "",
            k = [];
            h.trustedDomains && ("string" === typeof h.trustedDomains ? e = [h.trustedDomains] : "object" === typeof h.trustedDomains && "length" in h.trustedDomains && (e = h.trustedDomains));
            if (e && e.length) for (c = 0, f = e.length; c < f; c++) if (r.call(e, c) && e[c] && "string" === typeof e[c] && (n = J(e[c]))) {
                if ("*" === n) {
                    k = [n];
                    break
                }
                k.push.apply(k, [n, "//" + n, m.location.protocol + "//" + n])
            }
            k.length && (u += "trustedOrigins\x3d" + M(k.join(","))); ! 0 === h.forceEnhancedClipboard && (u += (u ? "\x26": "") + "forceEnhancedClipboard\x3dtrue");
            "string" === typeof h.swfObjectId && h.swfObjectId && (u += (u ? "\x26": "") + "swfObjectId\x3d" + M(h.swfObjectId));
            e = u;
            c = h.swfPath;
            f = null == h || h && !0 === h.cacheBust ? ( - 1 === h.swfPath.indexOf("?") ? "?": "\x26") + "noCache\x3d" + Q() : "";
            f = c + f;
            c = q.createElement("div");
            c.id = h.containerId;
            c.className = h.containerClass;
            c.style.position = "absolute";
            c.style.left = "0px";
            c.style.top = "-9999px";
            c.style.width = "1px";
            c.style.height = "1px";
            c.style.zIndex = "" + I(h.zIndex);
            n = q.createElement("div");
            c.appendChild(n);
            q.body.appendChild(c);
            u = q.createElement("div");
            k = "activex" === l.pluginType;
            u.innerHTML = '\x3cobject id\x3d"' + h.swfObjectId + '" name\x3d"' + h.swfObjectId + '" width\x3d"100%" height\x3d"100%" ' + (k ? 'classid\x3d"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"': 'type\x3d"application/x-shockwave-flash" data\x3d"' + f + '"') + "\x3e" + (k ? '\x3cparam name\x3d"movie" value\x3d"' + f + '"/\x3e': "") + '\x3cparam name\x3d"allowScriptAccess" value\x3d"' + b + '"/\x3e\x3cparam name\x3d"allowNetworking" value\x3d"' + d + '"/\x3e\x3cparam name\x3d"menu" value\x3d"false"/\x3e\x3cparam name\x3d"wmode" value\x3d"transparent"/\x3e\x3cparam name\x3d"flashvars" value\x3d"' + e + '"/\x3e\x3c/object\x3e';
            b = u.firstChild;
            b.ZeroClipboard = g;
            c.replaceChild(b, n)
        }
        b || ((b = q[h.swfObjectId]) && (a = b.length) && (b = b[a - 1]), !b && c && (b = c.firstChild));
        l.bridge = b || null;
        return b
    },
    ma = function() {
        var a = l.bridge;
        if (a) {
            var b = C(a);
            b && ("activex" === l.pluginType && "readyState" in a ? (a.style.display = "none",
            function d() {
                if (4 === a.readyState) {
                    for (var e in a)"function" === typeof a[e] && (a[e] = null);
                    a.parentNode && a.parentNode.removeChild(a);
                    b.parentNode && b.parentNode.removeChild(b)
                } else F(d, 10)
            } ()) : (a.parentNode && a.parentNode.removeChild(a), b.parentNode && b.parentNode.removeChild(b)));
            l.ready = null;
            l.bridge = null;
            l.deactivated = null
        }
    },
    J = function(a) {
        if (null == a || "" === a) return null;
        a = a.replace(/^\s+|\s+$/g, "");
        if ("" === a) return null;
        var b = a.indexOf("//");
        a = -1 === b ? a: a.slice(b + 2);
        var c = a.indexOf("/");
        return (a = -1 === c ? a: -1 === b || 0 === c ? null: a.slice(0, c)) && ".swf" === a.slice( - 4).toLowerCase() ? null: a || null
    },
    ua = function() {
        var a = function(a, c) {
            var d, e, f;
            if (null != a && "*" !== c[0] && ("string" === typeof a && (a = [a]), "object" === typeof a && "number" === typeof a.length)) for (d = 0, e = a.length; d < e; d++) if (r.call(a, d) && (f = J(a[d]))) {
                if ("*" === f) {
                    c.length = 0;
                    c.push("*");
                    break
                } - 1 === v(f, c) && c.push(f)
            }
        };
        return function(b, c) {
            var d = J(c.swfPath);
            null === d && (d = b);
            var e = [];
            a(c.trustedOrigins, e);
            a(c.trustedDomains, e);
            var f = e.length;
            if (0 < f) {
                if (1 === f && "*" === e[0]) return "always";
                if ( - 1 !== v(b, e)) return 1 === f && b === d ? "sameDomain": "always"
            }
            return "never"
        }
    } (),
    T = function(a, b) {
        if (!a || 1 !== a.nodeType) return a;
        if (a.classList) return a.classList.contains(b) || a.classList.add(b),
        a;
        if (b && "string" === typeof b) {
            var c = (b || "").split(/\s+/);
            if (1 === a.nodeType) if (a.className) {
                for (var d = " " + a.className + " ",
                e = a.className,
                f = 0,
                g = c.length; f < g; f++) 0 > d.indexOf(" " + c[f] + " ") && (e += " " + c[f]);
                a.className = e.replace(/^\s+|\s+$/g, "")
            } else a.className = b
        }
        return a
    },
    B = function(a, b) {
        if (!a || 1 !== a.nodeType) return a;
        if (a.classList) return a.classList.contains(b) && a.classList.remove(b),
        a;
        if ("string" === typeof b && b) {
            var c = b.split(/\s+/);
            if (1 === a.nodeType && a.className) {
                for (var d = (" " + a.className + " ").replace(/[\n\t]/g, " "), e = 0, f = c.length; e < f; e++) d = d.replace(" " + c[e] + " ", " ");
                a.className = d.replace(/^\s+|\s+$/g, "")
            }
        }
        return a
    },
    W = function(a) {
        var b = {
            left: 0,
            top: 0,
            width: 0,
            height: 0
        };
        if (a.getBoundingClientRect) {
            a = a.getBoundingClientRect();
            var c, d;
            "pageXOffset" in m && "pageYOffset" in m ? (c = m.pageXOffset, d = m.pageYOffset) : (c = 1, "function" === typeof q.body.getBoundingClientRect && (c = q.body.getBoundingClientRect(), c = c.right - c.left, d = q.body.offsetWidth, c = G.round(c / d * 100) / 100), d = c, c = G.round(q.documentElement.scrollLeft / d), d = G.round(q.documentElement.scrollTop / d));
            var e = q.documentElement.clientTop || 0;
            b.left = a.left + c - (q.documentElement.clientLeft || 0);
            b.top = a.top + d - e;
            b.width = "width" in a ? a.width: a.right - a.left;
            b.height = "height" in a ? a.height: a.bottom - a.top
        }
        return b
    },
    I = function(a) {
        if (/^(?:auto|inherit)$/.test(a)) return a;
        var b;
        "number" !== typeof a || Z(a) ? "string" === typeof a && (b = I(Y(a, 10))) : b = a;
        return "number" === typeof b ? b: "auto"
    }; (function(a) {
        function b(a) {
            a = a.match(/[\d]+/g);
            a.length = 3;
            return a.join(".")
        }
        function c(a) {
            a && (e = !0, a.version && (h = b(a.version)), !h && a.description && (h = b(a.description)), a.filename && (a = a.filename, g = !!a && (a = a.toLowerCase()) && (/^(pepflashplayer\.dll|libpepflashplayer\.so|pepperflashplayer\.plugin)$/.test(a) || "chrome.plugin" === a.slice( - 13))))
        }
        var d, e = !1,
        f = !1,
        g = !1,
        h = "";
        if (x.plugins && x.plugins.length) a = x.plugins["Shockwave Flash"],
        c(a),
        x.plugins["Shockwave Flash 2.0"] && (e = !0, h = "2.0.0.11");
        else if (x.mimeTypes && x.mimeTypes.length) a = (a = x.mimeTypes["application/x-shockwave-flash"]) && a.enabledPlugin,
        c(a);
        else if ("undefined" !== typeof a) {
            f = !0;
            try {
                d = new a("ShockwaveFlash.ShockwaveFlash.7"),
                e = !0,
                h = b(d.GetVariable("$version"))
            } catch(k) {
                try {
                    d = new a("ShockwaveFlash.ShockwaveFlash.6"),
                    e = !0,
                    h = "6.0.21"
                } catch(m) {
                    try {
                        d = new a("ShockwaveFlash.ShockwaveFlash"),
                        e = !0,
                        h = b(d.GetVariable("$version"))
                    } catch(p) {
                        f = !1
                    }
                }
            }
        }
        l.disabled = !0 !== e;
        l.outdated = h && L(h) < L("11.0.0");
        l.version = h || "0.0.0";
        l.pluginType = g ? "pepper": f ? "activex": e ? "netscape": "unknown"
    })(aa);
    var g = function() {
        if (! (this instanceof g)) return new g;
        "function" === typeof g._createClient && g._createClient.apply(this, k(arguments))
    };
    g.version = "2.1.1"; (function(a, b) {
        b in a && "function" === typeof O && O(a, b, {
            value: a[b],
            writable: !1,
            configurable: !0,
            enumerable: !0
        })
    })(g, "version");
    g.config = function() {
        return da.apply(this, k(arguments))
    };
    g.state = function() {
        return ea.apply(this, k(arguments))
    };
    g.isFlashUnusable = function() {
        return fa.apply(this, k(arguments))
    };
    g.on = function() {
        return ga.apply(this, k(arguments))
    };
    g.off = function() {
        return ha.apply(this, k(arguments))
    };
    g.handlers = function() {
        return ia.apply(this, k(arguments))
    };
    g.emit = function() {
        return ja.apply(this, k(arguments))
    };
    g.create = function() {
        return la.apply(this, k(arguments))
    };
    g.destroy = function() {
        return na.apply(this, k(arguments))
    };
    g.setData = function() {
        return oa.apply(this, k(arguments))
    };
    g.clearData = function() {
        return pa.apply(this, k(arguments))
    };
    g.getData = function() {
        return qa.apply(this, k(arguments))
    };
    g.focus = g.activate = function() {
        return ra.apply(this, k(arguments))
    };
    g.blur = g.deactivate = function() {
        return sa.apply(this, k(arguments))
    };
    g.activeElement = function() {
        return ta.apply(this, k(arguments))
    };
    var va = 0,
    t = {},
    wa = 0,
    E = {},
    K = {};
    s(h, {
        autoActivate: !0
    });
    var xa = function(a) {
        var b = this;
        b.id = "" + va++;
        t[b.id] = {
            instance: b,
            elements: [],
            handlers: {}
        };
        a && b.clip(a);
        g.on("*",
        function(a) {
            return b.emit(a)
        });
        g.on("destroy",
        function() {
            b.destroy()
        });
        g.create()
    },
    ya = function(a, b) {
        var c, d, e, f = {},
        g = t[this.id] && t[this.id].handlers;
        if ("string" === typeof a && a) e = a.toLowerCase().split(/\s+/);
        else if ("object" === typeof a && a && "undefined" === typeof b) for (c in a) if (r.call(a, c) && "string" === typeof c && c && "function" === typeof a[c]) this.on(c, a[c]);
        if (e && e.length) {
            c = 0;
            for (d = e.length; c < d; c++) a = e[c].replace(/^on/, ""),
            f[a] = !0,
            g[a] || (g[a] = []),
            g[a].push(b);
            f.ready && l.ready && this.emit({
                type: "ready",
                client: this
            });
            if (f.error) for (e = ["disabled", "outdated", "unavailable", "deactivated", "overdue"], c = 0, d = e.length; c < d; c++) if (l[e[c]]) {
                this.emit({
                    type: "error",
                    name: "flash-" + e[c],
                    client: this
                });
                break
            }
        }
        return this
    },
    za = function(a, b) {
        var c, d, e, f, g, h = t[this.id] && t[this.id].handlers;
        if (0 === arguments.length) f = P(h);
        else if ("string" === typeof a && a) f = a.split(/\s+/);
        else if ("object" === typeof a && a && "undefined" === typeof b) for (c in a) r.call(a, c) && "string" === typeof c && c && "function" === typeof a[c] && this.off(c, a[c]);
        if (f && f.length) for (c = 0, d = f.length; c < d; c++) if (a = f[c].toLowerCase().replace(/^on/, ""), (g = h[a]) && g.length) if (b) for (e = v(b, g); - 1 !== e;) g.splice(e, 1),
        e = v(b, g, e);
        else g.length = 0;
        return this
    },
    Aa = function(a) {
        var b = null,
        c = t[this.id] && t[this.id].handlers;
        c && (b = "string" === typeof a && a ? c[a] ? c[a].slice(0) : [] : z(c));
        return b
    },
    Ba = function(a) {
        var b;
        var c = a;
        if (!c || !c.type || c.client && c.client !== this) b = !1;
        else {
            var d = t[this.id] && t[this.id].elements,
            e = !!d && 0 < d.length;
            b = !c.target || e && -1 !== v(c.target, d);
            d = c.relatedTarget && e && -1 !== v(c.relatedTarget, d);
            c = c.client && c.client === this;
            b = b || d || c ? !0 : !1
        }
        if (b && ("object" === typeof a && a && "string" === typeof a.type && a.type && (a = s({},
        a)), a = s({},
        S(a), {
            client: this
        }), "object" === typeof a && a && a.type && (b = U(a), (c = (t[this.id] && t[this.id].handlers["*"] || []).concat(t[this.id] && t[this.id].handlers[a.type] || [])) && c.length))) for (var f, g, h, d = 0,
        e = c.length; d < e; d++) f = c[d],
        g = this,
        "string" === typeof f && "function" === typeof m[f] && (f = m[f]),
        "object" === typeof f && f && "function" === typeof f.handleEvent && (g = f, f = f.handleEvent),
        "function" === typeof f && (h = s({},
        a), V(f, g, [h], b));
        return this
    },
    Da = function(a) {
        a = X(a);
        for (var b = 0; b < a.length; b++) if (r.call(a, b) && a[b] && 1 === a[b].nodeType) {
            a[b].zcClippingId ? -1 === v(this.id, E[a[b].zcClippingId]) && E[a[b].zcClippingId].push(this.id) : (a[b].zcClippingId = "zcClippingId_" + wa++, E[a[b].zcClippingId] = [this.id], !0 === h.autoActivate && Ca(a[b]));
            var c = t[this.id] && t[this.id].elements; - 1 === v(a[b], c) && c.push(a[b])
        }
        return this
    },
    Ea = function(a) {
        var b = t[this.id];
        if (!b) return this;
        var b = b.elements,
        c;
        a = "undefined" === typeof a ? b.slice(0) : X(a);
        for (var d = a.length; d--;) if (r.call(a, d) && a[d] && 1 === a[d].nodeType) {
            for (c = 0; - 1 !== (c = v(a[d], b, c));) b.splice(c, 1);
            var e = E[a[d].zcClippingId];
            if (e) {
                for (c = 0; - 1 !== (c = v(this.id, e, c));) e.splice(c, 1);
                if (0 === e.length) {
                    if (!0 === h.autoActivate && (c = a[d]) && 1 === c.nodeType && (e = K[c.zcClippingId], "object" === typeof e && e)) {
                        for (var f = void 0,
                        g = void 0,
                        k = ["move", "leave", "enter", "out", "over"], l = 0, m = k.length; l < m; l++) f = "mouse" + k[l],
                        g = e[f],
                        "function" === typeof g && c.removeEventListener(f, g, !1);
                        delete K[c.zcClippingId]
                    }
                    delete a[d].zcClippingId
                }
            }
        }
        return this
    },
    Fa = function() {
        var a = t[this.id];
        return a && a.elements ? a.elements.slice(0) : []
    },
    Ga = function() {
        this.unclip();
        this.off();
        delete t[this.id]
    },
    X = function(a) {
        "string" === typeof a && (a = []);
        return "number" !== typeof a.length ? [a] : a
    },
    Ca = function(a) {
        if (a && 1 === a.nodeType) {
            var b = function(a) {
                if (a || (a = m.event))"js" !== a._source && (a.stopImmediatePropagation(), a.preventDefault()),
                delete a._source
            },
            c = function(c) {
                if (c || (c = m.event)) b(c),
                g.focus(a)
            };
            a.addEventListener("mouseover", c, !1);
            a.addEventListener("mouseout", b, !1);
            a.addEventListener("mouseenter", b, !1);
            a.addEventListener("mouseleave", b, !1);
            a.addEventListener("mousemove", b, !1);
            K[a.zcClippingId] = {
                mouseover: c,
                mouseout: b,
                mouseenter: b,
                mouseleave: b,
                mousemove: b
            }
        }
    };
    g._createClient = function() {
        xa.apply(this, k(arguments))
    };
    g.prototype.on = function() {
        return ya.apply(this, k(arguments))
    };
    g.prototype.off = function() {
        return za.apply(this, k(arguments))
    };
    g.prototype.handlers = function() {
        return Aa.apply(this, k(arguments))
    };
    g.prototype.emit = function() {
        return Ba.apply(this, k(arguments))
    };
    g.prototype.clip = function() {
        return Da.apply(this, k(arguments))
    };
    g.prototype.unclip = function() {
        return Ea.apply(this, k(arguments))
    };
    g.prototype.elements = function() {
        return Fa.apply(this, k(arguments))
    };
    g.prototype.destroy = function() {
        return Ga.apply(this, k(arguments))
    };
    g.prototype.setText = function(a) {
        g.setData("text/plain", a);
        return this
    };
    g.prototype.setHtml = function(a) {
        g.setData("text/html", a);
        return this
    };
    g.prototype.setRichText = function(a) {
        g.setData("application/rtf", a);
        return this
    };
    g.prototype.setData = function() {
        g.setData.apply(this, k(arguments));
        return this
    };
    g.prototype.clearData = function() {
        g.clearData.apply(this, k(arguments));
        return this
    };
    g.prototype.getData = function() {
        return g.getData.apply(this, k(arguments))
    };
    "function" === typeof define && define.amd ? define(function() {
        return g
    }) : "object" === typeof module && module && "object" === typeof module.exports && module.exports ? module.exports = g: m.ZeroClipboard = g
})(function() {
    return this
} ()); (function() {
    var e, g, h;
    e = jQuery;
    h = function(b, d) {
        return setTimeout(function() {
            return e(b).find("img").each(function(c, a) {
                return g(a.src, d)
            })
        },
        1)
    };
    g = function(b, d) {
        var c;
        c = new Image;
        c.onload = function() {
            var a, b;
            a = document.createElement("canvas");
            a.width = c.width;
            a.height = c.height;
            a.getContext("2d").drawImage(c, 0, 0, a.width, a.height);
            b = null;
            try {
                b = a.toDataURL("image/png")
            } catch(e) {}
            if (b) return d({
                dataURL: b,
                dataContentType: "image/png"
            })
        };
        return c.src = b
    };
    getFileData = function(b, d, c) {
        return c({
            pataFileName: d.name,
            dataContentType: d.type,
            dataURL: b
        })
    };
    e.paste = function(b) {
        e(b).on("paste",
        function(d) {
            var c = e(b).text(),
            a,
            f,
            g,
            k,
            l = this;
            if (null != (null != (a = d.originalEvent) ? a.clipboardData: void 0)) if (a = d.originalEvent.clipboardData, a.items) for (k = a.items, f = 0, g = k.length; f < g; f++) {
                a = k[f];
                if ("text/plain" === a.type) {
                    a.getAsString(function(a) {
                        c += a
                    });
                    return
                }
                d = new FileReader;
                var m = a.getAsFile();
                d.onload = function(a) {
                    return getFileData(a.target.result, m,
                    function(a) {
                        return e(b).trigger("pasteFile", a)
                    })
                };
                d.readAsDataURL(m)
            } else if (0 < a.types.length) {
                for (f = 0; f < a.files.length; f++) {
                    var n = a.files[f];
                    d = new FileReader;
                    d.onload = function(a) {
                        return getFileData(a.target.result, n,
                        function(a) {
                            return e(b).trigger("pasteFile", a)
                        })
                    };
                    d.readAsDataURL(n)
                }
                text = a.getData("Text");
                if (null != text) {
                    c += text;
                    return
                }
            } else h(b,
            function(a) {
                return e(b).trigger("pasteImage", a)
            });
            if (a = window.clipboardData) {
                if (null != (l = text = a.getData("Text")) && l.length) {
                    c += text;
                    return
                }
                h(b,
                function(a) {
                    return e(b).trigger("pasteImage", a)
                })
            }
            return setTimeout(function() {
                return e(b).text(c)
            },
            2)
        });
        return e(b)
    }
}).call(this); (function() {
    var e, g, h;
    e = jQuery;
    h = function(b, d) {
        return setTimeout(function() {
            return e(b).find("img").each(function(c, a) {
                return g(a.src, d)
            })
        },
        1)
    };
    g = function(b, d) {
        var c;
        c = new Image;
        c.onload = function() {
            var a, b;
            a = document.createElement("canvas");
            a.width = c.width;
            a.height = c.height;
            a.getContext("2d").drawImage(c, 0, 0, a.width, a.height);
            b = null;
            try {
                b = a.toDataURL("image/png")
            } catch(e) {}
            if (b) return d({
                dataURL: b,
                dataContentType: "image/png"
            })
        };
        return c.src = b
    };
    getFileData = function(b, d, c) {
        return c({
            pataFileName: d.name,
            dataContentType: d.type,
            dataURL: b
        })
    };
    e.pasteupload = function(b) {
        e(b).on("paste",
        function(d) {
            var c = e(b).val(),
            a,
            f,
            g,
            k,
            l = this;
            if (null != (null != (a = d.originalEvent) ? a.clipboardData: void 0)) if (a = d.originalEvent.clipboardData, a.items) for (k = a.items, f = 0, g = k.length; f < g; f++) {
                a = k[f];
                if ("text/plain" === a.type) {
                    a.getAsString(function(a) {
                        c += a
                    });
                    return
                }
                d = new FileReader;
                var m = a.getAsFile();
                d.onload = function(a) {
                    return getFileData(a.target.result, m,
                    function(a) {
                        return e(b).trigger("pasteFile", a)
                    })
                };
                d.readAsDataURL(m)
            } else if (0 < a.types.length) {
                for (f = 0; f < a.files.length; f++) {
                    var n = a.files[f];
                    d = new FileReader;
                    d.onload = function(a) {
                        return getFileData(a.target.result, n,
                        function(a) {
                            return e(b).trigger("pasteFile", a)
                        })
                    };
                    d.readAsDataURL(n)
                }
                text = a.getData("Text");
                if (null != text) {
                    c += text;
                    return
                }
            } else h(b,
            function(a) {
                return e(b).trigger("pasteImage", a)
            });
            if (a = window.clipboardData) {
                if (null != (l = text = a.getData("Text")) && l.length) {
                    c += text;
                    return
                }
                h(b,
                function(a) {
                    return e(b).trigger("pasteImage", a)
                })
            }
            return setTimeout(function() {
                return e(b).val(c)
            },
            2)
        });
        return e(b)
    }
}).call(this);
$.fn.outerHTML = function() {
    var a;
    if (this.length) {
        if (! (a = this[0].outerHTML)) {
            a = this[0];
            var b = document.createElement("div");
            b.appendChild(a.cloneNode(!0));
            a = b.innerHTML
        }
    } else a = this;
    return a
};
$.fn.fixPlaceholder = function() {
    var a = !1;
    "placeholder" in document.createElement("input") && (a = !0);
    a || (a = document.activeElement, $(this).find(":text,textarea").bind("focus",
    function() {
        "" != $(this).attr("placeholder") && $(this).val() == $(this).attr("placeholder") && $(this).val("")
    }).bind("blur",
    function() {
        "" == $(this).attr("placeholder") || "" != $(this).val() && $(this).val() != $(this).attr("placeholder") || $(this).val($(this).attr("placeholder"))
    }), $(a).focus())
};
var teamsAlert = window.alert;
window.alert = null;
var console = console || {
    log: function() {
        return ! 1
    }
};
var swfobject = function() {
    function v() {
        if (!t) {
            try {
                var a = d.getElementsByTagName("body")[0].appendChild(d.createElement("span"));
                a.parentNode.removeChild(a)
            } catch(b) {
                return
            }
            t = !0;
            for (var a = y.length,
            c = 0; c < a; c++) y[c]()
        }
    }
    function L(a) {
        t ? a() : y[y.length] = a
    }
    function M(a) {
        if ("undefined" != typeof n.addEventListener) n.addEventListener("load", a, !1);
        else if ("undefined" != typeof d.addEventListener) d.addEventListener("load", a, !1);
        else if ("undefined" != typeof n.attachEvent) T(n, "onload", a);
        else if ("function" == typeof n.onload) {
            var b = n.onload;
            n.onload = function() {
                b();
                a()
            }
        } else n.onload = a
    }
    function U() {
        var a = d.getElementsByTagName("body")[0],
        b = d.createElement("object");
        b.setAttribute("type", "application/x-shockwave-flash");
        var c = a.appendChild(b);
        if (c) {
            var f = 0; (function() {
                if ("undefined" != typeof c.GetVariable) {
                    var g = c.GetVariable("$version");
                    g && (g = g.split(" ")[1].split(","), e.pv = [parseInt(g[0], 10), parseInt(g[1], 10), parseInt(g[2], 10)])
                } else if (10 > f) {
                    f++;
                    setTimeout(arguments.callee, 10);
                    return
                }
                a.removeChild(b);
                c = null;
                D()
            })()
        } else D()
    }
    function D() {
        var a = r.length;
        if (0 < a) for (var b = 0; b < a; b++) {
            var c = r[b].id,
            f = r[b].callbackFn,
            g = {
                success: !1,
                id: c
            };
            if (0 < e.pv[0]) {
                var d = p(c);
                if (d) if (!z(r[b].swfVersion) || e.wk && 312 > e.wk) if (r[b].expressInstall && E()) {
                    g = {};
                    g.data = r[b].expressInstall;
                    g.width = d.getAttribute("width") || "0";
                    g.height = d.getAttribute("height") || "0";
                    d.getAttribute("class") && (g.styleclass = d.getAttribute("class"));
                    d.getAttribute("align") && (g.align = d.getAttribute("align"));
                    for (var h = {},
                    d = d.getElementsByTagName("param"), k = d.length, l = 0; l < k; l++)"movie" != d[l].getAttribute("name").toLowerCase() && (h[d[l].getAttribute("name")] = d[l].getAttribute("value"));
                    F(g, h, c, f)
                } else V(d),
                f && f(g);
                else u(c, !0),
                f && (g.success = !0, g.ref = G(c), f(g))
            } else u(c, !0),
            f && ((c = G(c)) && "undefined" != typeof c.SetVariable && (g.success = !0, g.ref = c), f(g))
        }
    }
    function G(a) {
        var b = null; (a = p(a)) && "OBJECT" == a.nodeName && ("undefined" != typeof a.SetVariable ? b = a: (a = a.getElementsByTagName("object")[0]) && (b = a));
        return b
    }
    function E() {
        return ! A && z("6.0.65") && (e.win || e.mac) && !(e.wk && 312 > e.wk)
    }
    function F(a, b, c, f) {
        A = !0;
        H = f || null;
        N = {
            success: !1,
            id: c
        };
        var g = p(c);
        if (g) {
            "OBJECT" == g.nodeName ? (x = I(g), B = null) : (x = g, B = c);
            a.id = "SWFObjectExprInst";
            if ("undefined" == typeof a.width || !/%$/.test(a.width) && 310 > parseInt(a.width, 10)) a.width = "310";
            if ("undefined" == typeof a.height || !/%$/.test(a.height) && 137 > parseInt(a.height, 10)) a.height = "137";
            d.title = d.title.slice(0, 47) + " - Flash Player Installation";
            f = e.ie && e.win ? "ActiveX": "PlugIn";
            f = "MMredirectURL\x3d" + n.location.toString().replace(/&/g, "%26") + "\x26MMplayerType\x3d" + f + "\x26MMdoctitle\x3d" + d.title;
            b.flashvars = "undefined" != typeof b.flashvars ? b.flashvars + ("\x26" + f) : f;
            e.ie && e.win && 4 != g.readyState && (f = d.createElement("div"), c += "SWFObjectNew", f.setAttribute("id", c), g.parentNode.insertBefore(f, g), g.style.display = "none",
            function() {
                4 == g.readyState ? g.parentNode.removeChild(g) : setTimeout(arguments.callee, 10)
            } ());
            J(a, b, c)
        }
    }
    function V(a) {
        if (e.ie && e.win && 4 != a.readyState) {
            var b = d.createElement("div");
            a.parentNode.insertBefore(b, a);
            b.parentNode.replaceChild(I(a), b);
            a.style.display = "none"; (function() {
                4 == a.readyState ? a.parentNode.removeChild(a) : setTimeout(arguments.callee, 10)
            })()
        } else a.parentNode.replaceChild(I(a), a)
    }
    function I(a) {
        var b = d.createElement("div");
        if (e.win && e.ie) b.innerHTML = a.innerHTML;
        else if (a = a.getElementsByTagName("object")[0]) if (a = a.childNodes) for (var c = a.length,
        f = 0; f < c; f++) 1 == a[f].nodeType && "PARAM" == a[f].nodeName || 8 == a[f].nodeType || b.appendChild(a[f].cloneNode(!0));
        return b
    }
    function J(a, b, c) {
        var f, g = p(c);
        if (e.wk && 312 > e.wk) return f;
        if (g) if ("undefined" == typeof a.id && (a.id = c), e.ie && e.win) {
            var q = "",
            h;
            for (h in a) a[h] != Object.prototype[h] && ("data" == h.toLowerCase() ? b.movie = a[h] : "styleclass" == h.toLowerCase() ? q += ' class\x3d"' + a[h] + '"': "classid" != h.toLowerCase() && (q += " " + h + '\x3d"' + a[h] + '"'));
            h = "";
            for (var k in b) b[k] != Object.prototype[k] && (h += '\x3cparam name\x3d"' + k + '" value\x3d"' + b[k] + '" /\x3e');
            g.outerHTML = '\x3cobject classid\x3d"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"' + q + "\x3e" + h + "\x3c/object\x3e";
            C[C.length] = a.id;
            f = p(a.id)
        } else {
            k = d.createElement("object");
            k.setAttribute("type", "application/x-shockwave-flash");
            for (var l in a) a[l] != Object.prototype[l] && ("styleclass" == l.toLowerCase() ? k.setAttribute("class", a[l]) : "classid" != l.toLowerCase() && k.setAttribute(l, a[l]));
            for (q in b) b[q] != Object.prototype[q] && "movie" != q.toLowerCase() && (a = k, h = q, l = b[q], c = d.createElement("param"), c.setAttribute("name", h), c.setAttribute("value", l), a.appendChild(c));
            g.parentNode.replaceChild(k, g);
            f = k
        }
        return f
    }
    function O(a) {
        var b = p(a);
        b && "OBJECT" == b.nodeName && (e.ie && e.win ? (b.style.display = "none",
        function() {
            if (4 == b.readyState) {
                var c = p(a);
                if (c) {
                    for (var f in c)"function" == typeof c[f] && (c[f] = null);
                    c.parentNode.removeChild(c)
                }
            } else setTimeout(arguments.callee, 10)
        } ()) : b.parentNode.removeChild(b))
    }
    function p(a) {
        var b = null;
        try {
            b = d.getElementById(a)
        } catch(c) {}
        return b
    }
    function T(a, b, c) {
        a.attachEvent(b, c);
        w[w.length] = [a, b, c]
    }
    function z(a) {
        var b = e.pv;
        a = a.split(".");
        a[0] = parseInt(a[0], 10);
        a[1] = parseInt(a[1], 10) || 0;
        a[2] = parseInt(a[2], 10) || 0;
        return b[0] > a[0] || b[0] == a[0] && b[1] > a[1] || b[0] == a[0] && b[1] == a[1] && b[2] >= a[2] ? !0 : !1
    }
    function P(a, b, c, f) {
        if (!e.ie || !e.mac) {
            var g = d.getElementsByTagName("head")[0];
            g && (c = c && "string" == typeof c ? c: "screen", f && (K = m = null), m && K == c || (f = d.createElement("style"), f.setAttribute("type", "text/css"), f.setAttribute("media", c), m = g.appendChild(f), e.ie && e.win && "undefined" != typeof d.styleSheets && 0 < d.styleSheets.length && (m = d.styleSheets[d.styleSheets.length - 1]), K = c), e.ie && e.win ? m && "object" == typeof m.addRule && m.addRule(a, b) : m && "undefined" != typeof d.createTextNode && m.appendChild(d.createTextNode(a + " {" + b + "}")))
        }
    }
    function u(a, b) {
        if (Q) {
            var c = b ? "visible": "hidden";
            t && p(a) ? p(a).style.visibility = c: P("#" + a, "visibility:" + c)
        }
    }
    function R(a) {
        return null != /[\\\"<>\.;]/.exec(a) && "undefined" != typeof encodeURIComponent ? encodeURIComponent(a) : a
    }
    var n = window,
    d = document,
    s = navigator,
    S = !1,
    y = [function() {
        S ? U() : D()
    }],
    r = [],
    C = [],
    w = [],
    x,
    B,
    H,
    N,
    t = !1,
    A = !1,
    m,
    K,
    Q = !0,
    e = function() {
        var a = "undefined" != typeof d.getElementById && "undefined" != typeof d.getElementsByTagName && "undefined" != typeof d.createElement,
        b = s.userAgent.toLowerCase(),
        c = s.platform.toLowerCase(),
        f = c ? /win/.test(c) : /win/.test(b),
        c = c ? /mac/.test(c) : /mac/.test(b),
        b = /webkit/.test(b) ? parseFloat(b.replace(/^.*webkit\/(\d+(\.\d+)?).*$/, "$1")) : !1,
        g = !+"\v1",
        e = [0, 0, 0],
        h = null;
        if ("undefined" != typeof s.plugins && "object" == typeof s.plugins["Shockwave Flash"]) ! (h = s.plugins["Shockwave Flash"].description) || "undefined" != typeof s.mimeTypes && s.mimeTypes["application/x-shockwave-flash"] && !s.mimeTypes["application/x-shockwave-flash"].enabledPlugin || (S = !0, g = !1, h = h.replace(/^.*\s+(\S+\s+\S+$)/, "$1"), e[0] = parseInt(h.replace(/^(.*)\..*$/, "$1"), 10), e[1] = parseInt(h.replace(/^.*\.(.*)\s.*$/, "$1"), 10), e[2] = /[a-zA-Z]/.test(h) ? parseInt(h.replace(/^.*[a-zA-Z]+(.*)$/, "$1"), 10) : 0);
        else if ("undefined" != typeof n.ActiveXObject) try {
            var k = new ActiveXObject("ShockwaveFlash.ShockwaveFlash");
            k && (h = k.GetVariable("$version")) && (g = !0, h = h.split(" ")[1].split(","), e = [parseInt(h[0], 10), parseInt(h[1], 10), parseInt(h[2], 10)])
        } catch(l) {}
        return {
            w3: a,
            pv: e,
            wk: b,
            ie: g,
            win: f,
            mac: c
        }
    } (); (function() {
        e.w3 && (("undefined" != typeof d.readyState && "complete" == d.readyState || "undefined" == typeof d.readyState && (d.getElementsByTagName("body")[0] || d.body)) && v(), t || ("undefined" != typeof d.addEventListener && d.addEventListener("DOMContentLoaded", v, !1), e.ie && e.win && (d.attachEvent("onreadystatechange",
        function() {
            "complete" == d.readyState && (d.detachEvent("onreadystatechange", arguments.callee), v())
        }), n == top &&
        function() {
            if (!t) {
                try {
                    d.documentElement.doScroll("left")
                } catch(a) {
                    setTimeout(arguments.callee, 0);
                    return
                }
                v()
            }
        } ()), e.wk &&
        function() {
            t || (/loaded|complete/.test(d.readyState) ? v() : setTimeout(arguments.callee, 0))
        } (), M(v)))
    })(); (function() {
        e.ie && e.win && window.attachEvent("onunload",
        function() {
            for (var a = w.length,
            b = 0; b < a; b++) w[b][0].detachEvent(w[b][1], w[b][2]);
            a = C.length;
            for (b = 0; b < a; b++) O(C[b]);
            for (var c in e) e[c] = null;
            e = null;
            for (var f in swfobject) swfobject[f] = null;
            swfobject = null
        })
    })();
    return {
        registerObject: function(a, b, c, f) {
            if (e.w3 && a && b) {
                var d = {};
                d.id = a;
                d.swfVersion = b;
                d.expressInstall = c;
                d.callbackFn = f;
                r[r.length] = d;
                u(a, !1)
            } else f && f({
                success: !1,
                id: a
            })
        },
        getObjectById: function(a) {
            if (e.w3) return G(a)
        },
        embedSWF: function(a, b, c, d, g, q, h, k, l, n) {
            var p = {
                success: !1,
                id: b
            };
            e.w3 && !(e.wk && 312 > e.wk) && a && b && c && d && g ? (u(b, !1), L(function() {
                c += "";
                d += "";
                var e = {};
                if (l && "object" === typeof l) for (var m in l) e[m] = l[m];
                e.data = a;
                e.width = c;
                e.height = d;
                m = {};
                if (k && "object" === typeof k) for (var r in k) m[r] = k[r];
                if (h && "object" === typeof h) for (var s in h) m.flashvars = "undefined" != typeof m.flashvars ? m.flashvars + ("\x26" + s + "\x3d" + h[s]) : s + "\x3d" + h[s];
                if (z(g)) r = J(e, m, b),
                e.id == b && u(b, !0),
                p.success = !0,
                p.ref = r;
                else {
                    if (q && E()) {
                        e.data = q;
                        F(e, m, b, n);
                        return
                    }
                    u(b, !0)
                }
                n && n(p)
            })) : n && n(p)
        },
        switchOffAutoHideShow: function() {
            Q = !1
        },
        ua: e,
        getFlashPlayerVersion: function() {
            return {
                major: e.pv[0],
                minor: e.pv[1],
                release: e.pv[2]
            }
        },
        hasFlashPlayerVersion: z,
        createSWF: function(a, b, c) {
            if (e.w3) return J(a, b, c)
        },
        showExpressInstall: function(a, b, c, d) {
            e.w3 && E() && F(a, b, c, d)
        },
        removeSWF: function(a) {
            e.w3 && O(a)
        },
        createCSS: function(a, b, c, d) {
            e.w3 && P(a, b, c, d)
        },
        addDomLoadEvent: L,
        addLoadEvent: M,
        getQueryParamValue: function(a) {
            var b = d.location.search || d.location.hash;
            if (b) { / \ ? /.test(b)&&
(b=b.split("?")[1]);if(null==a)return R(b);for(var b=b.split("\x26"),c=0;c<b.length;c++)if(b[c].substring(0,b[c].indexOf("\x3d"))==a)return R(b[c].substring(b[c].indexOf("\x3d")+1))}return""},expressInstallCallback:function(){if(A){var a=p("SWFObjectExprInst");a&&x&&(a.parentNode.replaceChild(x,a),B&&(u(B,!0),e.ie&&e.win&&(x.style.display="block")),H&&H(N));A=!1}}}}();
(function(){if(!window.WEB_SOCKET_FORCE_FLASH){if(window.WebSocket)return;if(window.MozWebSocket){window.WebSocket=MozWebSocket;return}}var d;d=window.WEB_SOCKET_LOGGER?WEB_SOCKET_LOGGER:window.console&&window.console.log&&window.console.error?window.console:{log:function(){},error:function(){}};10>swfobject.getFlashPlayerVersion().major?d.error("Flash Player \x3e\x3d 10.0.0 is required."):("file:"==location.protocol&&d.error("WARNING: web-socket-js doesn't work in file:/ //... URL unless you set Flash Security Settings properly. Open the page via Web server i.e. http://..."),
                window.WebSocket = function(a, b, c, d, f) {
                    var e = this;
                    e.__id = WebSocket.__nextId++;
                    WebSocket.__instances[e.__id] = e;
                    e.readyState = WebSocket.CONNECTING;
                    e.bufferedAmount = 0;
                    e.__events = {};
                    b ? "string" == typeof b && (b = [b]) : b = [];
                    e.__createTask = setTimeout(function() {
                        WebSocket.__addTask(function() {
                            e.__createTask = null;
                            WebSocket.__flash.create(e.__id, a, b, c || null, d || 0, f || null)
                        })
                    },
                    0)
                },
                WebSocket.prototype.send = function(a) {
                    if (this.readyState == WebSocket.CONNECTING) throw "INVALID_STATE_ERR: Web Socket connection has not been established";
                    a = WebSocket.__flash.send(this.__id, encodeURIComponent(a));
                    if (0 > a) return ! 0;
                    this.bufferedAmount += a;
                    return ! 1
                },
                WebSocket.prototype.close = function() {
                    this.__createTask ? (clearTimeout(this.__createTask), this.__createTask = null, this.readyState = WebSocket.CLOSED) : this.readyState != WebSocket.CLOSED && this.readyState != WebSocket.CLOSING && (this.readyState = WebSocket.CLOSING, WebSocket.__flash.close(this.__id))
                },
                WebSocket.prototype.addEventListener = function(a, b, c) {
                    a in this.__events || (this.__events[a] = []);
                    this.__events[a].push(b)
                },
                WebSocket.prototype.removeEventListener = function(a, b, c) {
                    if (a in this.__events) for (a = this.__events[a], c = a.length - 1; 0 <= c; --c) if (a[c] === b) {
                        a.splice(c, 1);
                        break
                    }
                },
                WebSocket.prototype.dispatchEvent = function(a) {
                    for (var b = this.__events[a.type] || [], c = 0; c < b.length; ++c) b[c](a); (b = this["on" + a.type]) && b.apply(this, [a])
                },
                WebSocket.prototype.__handleEvent = function(a) {
                    "readyState" in a && (this.readyState = a.readyState);
                    "protocol" in a && (this.protocol = a.protocol);
                    var b;
                    if ("open" == a.type || "error" == a.type) b = this.__createSimpleEvent(a.type);
                    else if ("close" == a.type) b = this.__createSimpleEvent("close"),
                    b.wasClean = a.wasClean ? !0 : !1,
                    b.code = a.code,
                    b.reason = a.reason;
                    else if ("message" == a.type) a = decodeURIComponent(a.message),
                    b = this.__createMessageEvent("message", a);
                    else throw "unknown event type: " + a.type;
                    this.dispatchEvent(b)
                },
                WebSocket.prototype.__createSimpleEvent = function(a) {
                    if (document.createEvent && window.Event) {
                        var b = document.createEvent("Event");
                        b.initEvent(a, !1, !1);
                        return b
                    }
                    return {
                        type: a,
                        bubbles: !1,
                        cancelable: !1
                    }
                },
                WebSocket.prototype.__createMessageEvent = function(a, b) {
                    if (document.createEvent && window.MessageEvent && !window.opera) {
                        var c = document.createEvent("MessageEvent");
                        c.initMessageEvent("message", !1, !1, b, null, null, window, null);
                        return c
                    }
                    return {
                        type: a,
                        data: b,
                        bubbles: !1,
                        cancelable: !1
                    }
                },
                WebSocket.CONNECTING = 0,
                WebSocket.OPEN = 1,
                WebSocket.CLOSING = 2,
                WebSocket.CLOSED = 3,
                WebSocket.__isFlashImplementation = !0,
                WebSocket.__initialized = !1,
                WebSocket.__flash = null,
                WebSocket.__instances = {},
                WebSocket.__tasks = [],
                WebSocket.__nextId = 0,
                WebSocket.loadFlashPolicyFile = function(a) {
                    WebSocket.__addTask(function() {
                        WebSocket.__flash.loadManualPolicyFile(a)
                    })
                },
                WebSocket.__initialize = function() {
                    if (!WebSocket.__initialized) if (WebSocket.__initialized = !0, WebSocket.__swfLocation && (window.WEB_SOCKET_SWF_LOCATION = WebSocket.__swfLocation), window.WEB_SOCKET_SWF_LOCATION) {
                        if (!window.WEB_SOCKET_SUPPRESS_CROSS_DOMAIN_SWF_ERROR && !WEB_SOCKET_SWF_LOCATION.match(/(^|\/)WebSocketMainInsecure\.swf(\?.*)?$/) && WEB_SOCKET_SWF_LOCATION.match(/^\w+:\/\/([^\/]+)/)) {
                            var a = RegExp.$1;
                            location.host != a && d.error("[WebSocket] You must host HTML and WebSocketMain.swf in the same host ('" + location.host + "' !\x3d '" + a + "'). See also 'How to host HTML file and SWF file in different domains' section in README.md. If you use WebSocketMainInsecure.swf, you can suppress this message by WEB_SOCKET_SUPPRESS_CROSS_DOMAIN_SWF_ERROR \x3d true;")
                        }
                        a = document.createElement("div");
                        a.id = "webSocketContainer";
                        a.style.position = "absolute";
                        WebSocket.__isFlashLite() ? (a.style.left = "0px", a.style.top = "0px") : (a.style.left = "-100px", a.style.top = "-100px");
                        var b = document.createElement("div");
                        b.id = "webSocketFlash";
                        a.appendChild(b);
                        document.body.appendChild(a);
                        swfobject.embedSWF(WEB_SOCKET_SWF_LOCATION, "webSocketFlash", "1", "1", "10.0.0", null, null, {
                            hasPriority: !0,
                            swliveconnect: !0,
                            allowScriptAccess: "always"
                        },
                        null,
                        function(a) {
                            a.success || d.error("[WebSocket] swfobject.embedSWF failed")
                        })
                    } else d.error("[WebSocket] set WEB_SOCKET_SWF_LOCATION to location of WebSocketMain.swf")
                },
                WebSocket.__onFlashInitialized = function() {
                    setTimeout(function() {
                        WebSocket.__flash = document.getElementById("webSocketFlash");
                        WebSocket.__flash.setCallerUrl(location.href);
                        WebSocket.__flash.setDebug( !! window.WEB_SOCKET_DEBUG);
                        for (var a = 0; a < WebSocket.__tasks.length; ++a) WebSocket.__tasks[a]();
                        WebSocket.__tasks = []
                    },
                    0)
                },
                WebSocket.__onFlashEvent = function() {
                    setTimeout(function() {
                        try {
                            for (var a = WebSocket.__flash.receiveEvents(), b = 0; b < a.length; ++b) WebSocket.__instances[a[b].webSocketId].__handleEvent(a[b])
                        } catch(c) {
                            d.error(c)
                        }
                    },
                    0);
                    return ! 0
                },
                WebSocket.__log = function(a) {
                    d.log(decodeURIComponent(a))
                },
                WebSocket.__error = function(a) {
                    d.error(decodeURIComponent(a))
                },
                WebSocket.__addTask = function(a) {
                    WebSocket.__flash ? a() : WebSocket.__tasks.push(a)
                },
                WebSocket.__isFlashLite = function() {
                    if (!window.navigator || !window.navigator.mimeTypes) return ! 1;
                    var a = window.navigator.mimeTypes["application/x-shockwave-flash"];
                    return a && a.enabledPlugin && a.enabledPlugin.filename ? a.enabledPlugin.filename.match(/flashlite/i) ? !0 : !1 : !1
                },
                window.WEB_SOCKET_DISABLE_AUTO_INITIALIZATION || swfobject.addDomLoadEvent(function() {
                    WebSocket.__initialize()
                }))
            })(); +
            function(e) {
                var d = function(b, a) {
                    this.$element = this.hoverState = this.timeout = this.enabled = this.options = this.type = null;
                    this.init("tooltip", b, a)
                };
                d.VERSION = "3.3.4";
                d.TRANSITION_DURATION = 150;
                d.DEFAULTS = {
                    animation: !0,
                    placement: "top",
                    selector: !1,
                    template: '\x3cdiv class\x3d"tooltip" role\x3d"tooltip"\x3e\x3cdiv class\x3d"tooltip-arrow"\x3e\x3c/div\x3e\x3cdiv class\x3d"tooltip-inner"\x3e\x3c/div\x3e\x3c/div\x3e',
                    trigger: "hover focus",
                    title: "",
                    delay: 0,
                    html: !1,
                    container: !1,
                    viewport: {
                        selector: "body",
                        padding: 0
                    }
                };
                d.prototype.init = function(b, a, c) {
                    this.enabled = !0;
                    this.type = b;
                    this.$element = e(a);
                    this.options = this.getOptions(c);
                    this.$viewport = this.options.viewport && e(this.options.viewport.selector || this.options.viewport);
                    if (this.$element[0] instanceof document.constructor && !this.options.selector) throw Error("`selector` option must be specified when initializing " + this.type + " on the window.document object!");
                    b = this.options.trigger.split(" ");
                    for (a = b.length; a--;) if (c = b[a], "click" == c) this.$element.on("click." + this.type, this.options.selector, e.proxy(this.toggle, this));
                    else if ("manual" != c) {
                        var f = "hover" == c ? "mouseleave": "focusout";
                        this.$element.on(("hover" == c ? "mouseenter": "focusin") + "." + this.type, this.options.selector, e.proxy(this.enter, this));
                        this.$element.on(f + "." + this.type, this.options.selector, e.proxy(this.leave, this))
                    }
                    this.options.selector ? this._options = e.extend({},
                    this.options, {
                        trigger: "manual",
                        selector: ""
                    }) : this.fixTitle()
                };
                d.prototype.getDefaults = function() {
                    return d.DEFAULTS
                };
                d.prototype.getOptions = function(b) {
                    b = e.extend({},
                    this.getDefaults(), this.$element.data(), b);
                    b.delay && "number" == typeof b.delay && (b.delay = {
                        show: b.delay,
                        hide: b.delay
                    });
                    return b
                };
                d.prototype.getDelegateOptions = function() {
                    var b = {},
                    a = this.getDefaults();
                    this._options && e.each(this._options,
                    function(c, f) {
                        a[c] != f && (b[c] = f)
                    });
                    return b
                };
                d.prototype.enter = function(b) {
                    var a = b instanceof this.constructor ? b: e(b.currentTarget).data("bs." + this.type);
                    if (a && a.$tip && a.$tip.is(":visible")) a.hoverState = "in";
                    else {
                        a || (a = new this.constructor(b.currentTarget, this.getDelegateOptions()), e(b.currentTarget).data("bs." + this.type, a));
                        clearTimeout(a.timeout);
                        a.hoverState = "in";
                        if (!a.options.delay || !a.options.delay.show) return a.show();
                        a.timeout = setTimeout(function() {
                            "in" == a.hoverState && a.show()
                        },
                        a.options.delay.show)
                    }
                };
                d.prototype.leave = function(b) {
                    var a = b instanceof this.constructor ? b: e(b.currentTarget).data("bs." + this.type);
                    a || (a = new this.constructor(b.currentTarget, this.getDelegateOptions()), e(b.currentTarget).data("bs." + this.type, a));
                    clearTimeout(a.timeout);
                    a.hoverState = "out";
                    if (!a.options.delay || !a.options.delay.hide) return a.hide();
                    a.timeout = setTimeout(function() {
                        "out" == a.hoverState && a.hide()
                    },
                    a.options.delay.hide)
                };
                d.prototype.show = function() {
                    var b = e.Event("show.bs." + this.type);
                    if (this.hasContent() && this.enabled) {
                        this.$element.trigger(b);
                        var a = e.contains(this.$element[0].ownerDocument.documentElement, this.$element[0]);
                        if (!b.isDefaultPrevented() && a) {
                            var c = this,
                            b = this.tip(),
                            a = this.getUID(this.type);
                            this.setContent();
                            b.attr("id", a);
                            this.$element.attr("aria-describedby", a);
                            this.options.animation && b.addClass("fade");
                            var a = "function" == typeof this.options.placement ? this.options.placement.call(this, b[0], this.$element[0]) : this.options.placement,
                            f = /\s?auto?\s?/i,
                            l = f.test(a);
                            l && (a = a.replace(f, "") || "top");
                            b.detach().css({
                                top: 0,
                                left: 0,
                                display: "block"
                            }).addClass(a).data("bs." + this.type, this);
                            this.options.container ? b.appendTo(this.options.container) : b.insertAfter(this.$element);
                            var f = this.getPosition(),
                            h = b[0].offsetWidth,
                            g = b[0].offsetHeight;
                            if (l) {
                                var l = a,
                                k = this.options.container ? e(this.options.container) : this.$element.parent(),
                                k = this.getPosition(k),
                                a = "bottom" == a && f.bottom + g > k.bottom ? "top": "top" == a && f.top - g < k.top ? "bottom": "right" == a && f.right + h > k.width ? "left": "left" == a && f.left - h < k.left ? "right": a;
                                b.removeClass(l).addClass(a)
                            }
                            f = this.getCalculatedOffset(a, f, h, g);
                            this.applyPlacement(f, a);
                            a = function() {
                                var a = c.hoverState;
                                c.$element.trigger("shown.bs." + c.type);
                                c.hoverState = null;
                                "out" == a && c.leave(c)
                            };
                            e.support.transition && this.$tip.hasClass("fade") ? b.one("bsTransitionEnd", a).emulateTransitionEnd(d.TRANSITION_DURATION) : a()
                        }
                    }
                };
                d.prototype.applyPlacement = function(b, a) {
                    var c = this.tip(),
                    f = c[0].offsetWidth,
                    d = c[0].offsetHeight,
                    h = parseInt(c.css("margin-top"), 10),
                    g = parseInt(c.css("margin-left"), 10);
                    isNaN(h) && (h = 0);
                    isNaN(g) && (g = 0);
                    b.top += h;
                    b.left += g;
                    e.offset.setOffset(c[0], e.extend({
                        using: function(a) {
                            c.css({
                                top: Math.round(a.top),
                                left: Math.round(a.left)
                            })
                        }
                    },
                    b), 0);
                    c.addClass("in");
                    var g = c[0].offsetWidth,
                    k = c[0].offsetHeight;
                    "top" == a && k != d && (b.top = b.top + d - k);
                    var m = this.getViewportAdjustedDelta(a, b, g, k);
                    m.left ? b.left += m.left: b.top += m.top;
                    f = (h = /top|bottom/.test(a)) ? 2 * m.left - f + g: 2 * m.top - d + k;
                    d = h ? "offsetWidth": "offsetHeight";
                    c.offset(b);
                    this.replaceArrow(f, c[0][d], h)
                };
                d.prototype.replaceArrow = function(b, a, c) {
                    this.arrow().css(c ? "left": "top", 50 * (1 - b / a) + "%").css(c ? "top": "left", "")
                };
                d.prototype.setContent = function() {
                    var b = this.tip(),
                    a = this.getTitle();
                    b.find(".tooltip-inner")[this.options.html ? "html": "text"](a);
                    b.removeClass("fade in top bottom left right")
                };
                d.prototype.hide = function(b) {
                    function a() {
                        "in" != c.hoverState && f.detach();
                        c.$element.removeAttr("aria-describedby").trigger("hidden.bs." + c.type);
                        b && b()
                    }
                    var c = this,
                    f = e(this.$tip),
                    l = e.Event("hide.bs." + this.type);
                    this.$element.trigger(l);
                    if (!l.isDefaultPrevented()) return f.removeClass("in"),
                    e.support.transition && f.hasClass("fade") ? f.one("bsTransitionEnd", a).emulateTransitionEnd(d.TRANSITION_DURATION) : a(),
                    this.hoverState = null,
                    this
                };
                d.prototype.fixTitle = function() {
                    var b = this.$element; (b.attr("title") || "string" != typeof b.attr("data-original-title")) && b.attr("data-original-title", b.attr("title") || "").attr("title", "")
                };
                d.prototype.hasContent = function() {
                    return this.getTitle()
                };
                d.prototype.getPosition = function(b) {
                    b = b || this.$element;
                    var a = b[0],
                    c = "BODY" == a.tagName,
                    a = a.getBoundingClientRect();
                    null == a.width && (a = e.extend({},
                    a, {
                        width: a.right - a.left,
                        height: a.bottom - a.top
                    }));
                    var d = c ? {
                        top: 0,
                        left: 0
                    }: b.offset();
                    b = {
                        scroll: c ? document.documentElement.scrollTop || document.body.scrollTop: b.scrollTop()
                    };
                    c = c ? {
                        width: e(window).width(),
                        height: e(window).height()
                    }: null;
                    return e.extend({},
                    a, b, c, d)
                };
                d.prototype.getCalculatedOffset = function(b, a, c, d) {
                    return "bottom" == b ? {
                        top: a.top + a.height,
                        left: a.left + a.width / 2 - c / 2
                    }: "top" == b ? {
                        top: a.top - d,
                        left: a.left + a.width / 2 - c / 2
                    }: "left" == b ? {
                        top: a.top + a.height / 2 - d / 2,
                        left: a.left - c
                    }: {
                        top: a.top + a.height / 2 - d / 2,
                        left: a.left + a.width
                    }
                };
                d.prototype.getViewportAdjustedDelta = function(b, a, c, d) {
                    var e = {
                        top: 0,
                        left: 0
                    };
                    if (!this.$viewport) return e;
                    var h = this.options.viewport && this.options.viewport.padding || 0,
                    g = this.getPosition(this.$viewport);
                    /right|left/.test(b) ? (c = a.top - h - g.scroll, a = a.top + h - g.scroll + d, c < g.top ? e.top = g.top - c: a > g.top + g.height && (e.top = g.top + g.height - a)) : (d = a.left - h, a = a.left + h + c, d < g.left ? e.left = g.left - d: a > g.width && (e.left = g.left + g.width - a));
                    return e
                };
                d.prototype.getTitle = function() {
                    var b = this.$element,
                    a = this.options;
                    return b.attr("data-original-title") || ("function" == typeof a.title ? a.title.call(b[0]) : a.title)
                };
                d.prototype.getUID = function(b) {
                    do b += ~~ (1E6 * Math.random());
                    while (document.getElementById(b));
                    return b
                };
                d.prototype.tip = function() {
                    return this.$tip = this.$tip || e(this.options.template)
                };
                d.prototype.arrow = function() {
                    return this.$arrow = this.$arrow || this.tip().find(".tooltip-arrow")
                };
                d.prototype.enable = function() {
                    this.enabled = !0
                };
                d.prototype.disable = function() {
                    this.enabled = !1
                };
                d.prototype.toggleEnabled = function() {
                    this.enabled = !this.enabled
                };
                d.prototype.toggle = function(b) {
                    var a = this;
                    b && (a = e(b.currentTarget).data("bs." + this.type), a || (a = new this.constructor(b.currentTarget, this.getDelegateOptions()), e(b.currentTarget).data("bs." + this.type, a)));
                    a.tip().hasClass("in") ? a.leave(a) : a.enter(a)
                };
                d.prototype.destroy = function() {
                    var b = this;
                    clearTimeout(this.timeout);
                    this.hide(function() {
                        b.$element.off("." + b.type).removeData("bs." + b.type)
                    })
                };
                var n = e.fn.tooltip;
                e.fn.tooltip = function(b) {
                    return this.each(function() {
                        var a = e(this),
                        c = a.data("bs.tooltip"),
                        f = "object" == typeof b && b;
                        if (c || !/destroy|hide/.test(b)) if (c || a.data("bs.tooltip", c = new d(this, f)), "string" == typeof b) c[b]()
                    })
                };
                e.fn.tooltip.Constructor = d;
                e.fn.tooltip.noConflict = function() {
                    e.fn.tooltip = n;
                    return this
                }
            } (jQuery); (function(b) {
                b.fn.extend({
                    photoGallery: function(C) {
                        function D() {
                            var a = f.width(),
                            q = f.height(),
                            b = a * c.ratio,
                            q = q * c.ratio;
                            1 > b - a && (b = Math.ceil(b));
                            a = (b / u * 100).toFixed(0);
                            90 < a && 110 > a ? (a = 100, b = u, q = y) : 1600 < a && (a = 1600, b = 16 * u, q = 16 * y);
                            f.width(b).height(q);
                            z();
                            w(a);
                            G(b, q)
                        }
                        function d() {
                            P.css("display", "none");
                            var a = f.width(),
                            b = f.height(),
                            e = (a / c.ratio / u * 100).toFixed(0);
                            5 > e ? (e = 5, a = u / 20, b = y / 20) : 90 < e && 110 > e ? (e = 100, a = u, b = y) : (a /= c.ratio, b /= c.ratio);
                            f.width(a).height(b);
                            z();
                            w(e);
                            G(a, b)
                        }
                        function G(a, b) {
                            F && (a = [b, b = a][0]);
                            a > document.body.clientWidth || b > document.body.clientHeight ? (v.show(), M()) : v.hide()
                        }
                        function N(a) {
                            var b = document.body.clientHeight - 8,
                            c = document.body.clientWidth - 8;
                            if ("90" == a || "270" == a) c = [b, b = c][0];
                            a = Math.min(u, c);
                            b = Math.min(y, b);
                            a / b > O ? a = b * O: b = a / O;
                            f.css({
                                width: a,
                                height: b
                            });
                            z()
                        }
                        function x(a) {
                            var b = c.thumbnailsWidth,
                            e = c.thumbnailsHeight;
                            if ("90" == a || "270" == a) b = [e, e = b][0];
                            H.css({
                                maxWidth: b,
                                maxHeight: e
                            });
                            v.hide()
                        }
                        function w(a) {
                            t.find(".percentTip").remove();
                            b("\x3cdiv class\x3d'percentTip'\x3e\x3cspan\x3e" + a + "%\x3c/span\x3e\x3c/div\x3e").appendTo(t).fadeOut(1500)
                        }
                        function z() {
                            var a = f.width(),
                            b = f.height(),
                            b = (document.body.clientHeight - b) / 2;
                            f.css("left", (document.body.clientWidth - a) / 2 + "px").css("top", b + "px")
                        }
                        function M() {
                            var a = v.find("img"),
                            b = a.width(),
                            a = a.height(),
                            e = f.width(),
                            I = f.height(),
                            n = f.offset(),
                            s = n.left,
                            k = n.top,
                            l = document.body.clientWidth,
                            n = document.body.clientHeight,
                            p,
                            m;
                            F && (b = [a, a = b][0], e = [I, I = e][0]);
                            p = b / (e / l);
                            e < l && (p = b);
                            m = a / (I / n);
                            I < n && (m = a);
                            s = (c.thumbnailsWidth - b) / 2 + -s / e * b;
                            e < l && (s = (c.thumbnailsWidth - b) / 2);
                            b = (c.thumbnailsHeight - a) / 2 + -k / I * a;
                            I < n && (b = (c.thumbnailsHeight - a) / 2);
                            v.find(".thumbDrag").css({
                                width: p,
                                height: m,
                                left: s,
                                top: b
                            })
                        }
                        function J() {
                            E();
                            b(c.imgs).each(function(a, e) {
                                if (a == c.activeIndex && 0 == b(this)[0].imgHeight && 0 == b(this)[0].imgWidth) b(c.template.IMAGE).appendTo(t).attr("src", e.url).attr("index", a).on("error",
                                function() {
                                    var a = b(this)[0];
                                    a.src = "/static/images/galleryIcon/nopic.png";
                                    b(a).attr("comp", "error")
                                }).on("load",
                                function() {
                                    var a = b(this)[0],
                                    e = a.naturalHeight,
                                    q = a.naturalWidth,
                                    k = q / e,
                                    l = document.body.clientHeight,
                                    p = document.body.clientWidth,
                                    m,
                                    r,
                                    g = document.body.clientHeight - 16,
                                    h = document.body.clientWidth - 8,
                                    d = b(a).attr("index");
                                    b(a).attr("comp", "true");
                                    if (0 == c.imgs[d].imgWidth || 0 == c.imgs[d].imgHeight) r = Math.max(p, q),
                                    m = Math.max(l, e),
                                    r > h && (r = h, m = Math.max(l, Math.ceil(r / k)), q > r && (q = r, e = Math.ceil(q / k))),
                                    m > g && (m = g, r = Math.max(p, Math.ceil(m * k)), e > m && (e = m, q = Math.ceil(e * k))),
                                    a = b(this)[0],
                                    l = a.naturalHeight,
                                    p = a.naturalWidth,
                                    k = p / l,
                                    g = p,
                                    h = l,
                                    d == c.activeIndex ? (g = q, h = e) : (p > r && (g = r, l = h = Math.ceil(g / k), h > m && (l = h = m, g = Math.ceil(h * k))), l > m && (h = m, g = Math.ceil(h * k), g > r && (g = r, h = Math.ceil(g / k)))),
                                    b(a).css({
                                        width: g,
                                        height: h,
                                        left: (A - g) / 2,
                                        top: (B - h) / 2
                                    }),
                                    c.imgs[d].imgWidth = g,
                                    c.imgs[d].imgHeight = h,
                                    b(a).attr("index") == c.activeIndex && (u = g, y = h, O = g / h, f = b(".image[index\x3d'" + c.activeIndex + "']", t).addClass("active").css({
                                        width: g,
                                        height: h
                                    }).removeClass("rotate0 rotate90 rotate180 rotate270"), z(), P.css("display", "none"))
                                }).on("dblclick",
                                function() {})
                            });
                            var a = b(".image[index\x3d'" + c.activeIndex + "']", t);
                            b(a) && 0 != b(a).imgWidth && b(a).imgWidth < document.body.clientWidth && 0 != b(a).imageHeight && b(a).imageHeight < document.body.clientHeight && (f = b(".image[index\x3d'" + c.activeIndex + "']", t).addClass("active"))
                        }
                        function E() {
                            A = document.body.clientWidth;
                            B = document.body.clientHeight;
                            u = c.imgs[c.activeIndex].imgWidth;
                            y = c.imgs[c.activeIndex].imgHeight;
                            O = u / y;
                            b(".image", t).removeClass("active");
                            f = b(".image[index\x3d'" + c.activeIndex + "']", t).addClass("active").css({
                                width: u,
                                height: y
                            }).removeClass("rotate0 rotate90 rotate180 rotate270");
                            H = v.find("img").attr("src", c.imgs[c.activeIndex].url);
                            v.find("img").removeAttr("class").removeAttr("style");
                            F = !1;
                            v.hide();
                            Q.removeClass("active");
                            R.removeClass("active");
                            "true" == f.attr("comp") ? P.css("display", "none") : P.css("display", "inline-block");
                            z()
                        }
                        var V = -1 < navigator.userAgent.indexOf("Firefox") ? "DOMMouseScroll": "mousewheel",
                        c = b.extend({
                            ratio: 1.2,
                            thumbnailsWidth: 180,
                            thumbnailsHeight: 120,
                            template: {
                                OPERTATION: '\x3cdiv class\x3d"oper"\x3e\x3cspan class\x3d"prev"\x3e\x3ci class\x3d"icon_tool-prev"\x3e\x3c/i\x3e\x3c/span\x3e\x3cspan class\x3d"loading"\x3e\x3cimg src\x3d"/static/images/galleryIcon/loading.gif"\x3e\x3c/img\x3e\x3c/span\x3e\x3cspan class\x3d"next"\x3e\x3ci class\x3d"icon_tool-next"\x3e\x3c/i\x3e\x3c/span\x3e\x3c/div\x3e\x3cdiv class\x3d"tool"\x3e\x3cdiv class\x3d"toolct"\x3e\x3cspan class\x3d"oper_bigger" title\x3d"\u653e\u5927\u56fe\u7247"\x3e\x3ci class\x3d"icon_tool-bigger"\x3e\x3c/i\x3e\x3c/span\x3e\x3cspan class\x3d"oper_smaller" title\x3d"\u7f29\u5c0f\u56fe\u7247"\x3e\x3ci class\x3d"icon_tool-smaller"\x3e\x3c/i\x3e\x3c/span\x3e\x3cspan class\x3d"oper_rotate" title\x3d"\u5411\u53f3\u65cb\u8f6c"\x3e\x3ci class\x3d"icon_tool-rotate"\x3e\x3c/i\x3e\x3c/span\x3e\x3cspan class\x3d"oper_close" title\x3d"\u5173\u95ed"\x3e\x3ci class\x3d"icon_tool-close"\x3e\x3c/i\x3e\x3c/span\x3e\x3c/div\x3e\x3c/div\x3e',
                                THUMBNAILS: '\x3cdiv class\x3d\'thumbnails\'\x3e\x3cspan class\x3d"thumbClose" title\x3d"\u5173\u95ed\u7f29\u7565\u56fe"\x3e\x3ci class\x3d"icon_close-small"\x3e\x3c/i\x3e\x3c/span\x3e\x3cimg ondragstart\x3d"return false;"/\x3e\x3cdiv class\x3d"thumbDrag"\x3e\x3cspan\x3e\x3c/span\x3e\x3c/div\x3e\x3c/div\x3e',
                                IMAGE: '\x3cimg class\x3d"image" ondragstart\x3d"return false;"/\x3e'
                            }
                        },
                        C),
                        t = b(this);
                        t.append(c.template.OPERTATION).append(c.template.THUMBNAILS);
                        var W = b(this).find(".tool");
                        C = b(this).find(".oper_fullscreen");
                        var aa = b(this).find(".oper_bigger"),
                        ba = b(this).find(".oper_smaller"),
                        ca = b(this).find(".oper_rotate"),
                        da = b(this).find(".oper_close"),
                        Q = b(this).find(".prev"),
                        R = b(this).find(".next");
                        b(this).find(".oper");
                        var v = b(this).find(".thumbnails"),
                        P = b(this).find(".loading"),
                        f,
                        H,
                        u,
                        y,
                        O,
                        K,
                        S,
                        A,
                        B,
                        F,
                        L,
                        T;
                        Q.on("click",
                        function() {
                            0 < c.activeIndex && c.activeIndex--;
                            J();
                            E()
                        }).on("mouseover",
                        function(a) {
                            0 < c.activeIndex && b(this).addClass("active")
                        }).on("mouseout",
                        function(a) {
                            b(this).removeClass("active")
                        });
                        R.on("click",
                        function() {
                            c.activeIndex < c.imgs.length - 1 && c.activeIndex++;
                            J();
                            E()
                        }).on("mouseover",
                        function(a) {
                            c.activeIndex < c.imgs.length - 1 && b(this).addClass("active")
                        }).on("mouseout",
                        function(a) {
                            b(this).removeClass("active")
                        });
                        v.css({
                            height: c.thumbnailsHeight,
                            width: c.thumbnailsWidth
                        }).on("mouseenter",
                        function(a) {
                            L = -1
                        }).on("mousedown",
                        function(a) {
                            L = a.pageX || a.clientX;
                            T = a.pageY || a.clientY;
                            A = document.body.clientWidth;
                            B = document.body.clientHeight;
                            a.stopPropagation()
                        }).on("mousemove",
                        function(a) {
                            if (0 < L) {
                                var q = a.pageX || a.clientX;
                                a = a.pageY || a.clientY;
                                var e = b(this).find(".thumbDrag"),
                                d = f.width(),
                                n = f.height(),
                                s = H.width(),
                                k = H.height(),
                                l = parseFloat(e.css("left")) + (q - L),
                                p = parseFloat(e.css("top")) + (a - T),
                                m = e.width(),
                                r = e.height(),
                                g,
                                h;
                                F && (s = [k, k = s][0], d = [n, n = d][0]);
                                g = (c.thumbnailsHeight - k) / 2;
                                h = (c.thumbnailsWidth - s) / 2;
                                m = c.thumbnailsWidth - m - h - 2;
                                r = c.thumbnailsHeight - r - g - 2;
                                l < h ? l = h: l > m && (l = m);
                                p < g ? p = g: p > r && (p = r);
                                e.css({
                                    left: l,
                                    top: p
                                });
                                L = q;
                                T = a;
                                l = d < A ? (A - d) / 2 : -d * (l - h) / s;
                                p = n < B ? (B - n) / 2 : -n * (p - g) / k;
                                f.offset({
                                    left: l,
                                    top: p
                                })
                            }
                        }).on("mouseup",
                        function(a) {
                            L = -1
                        });
                        v.find(".thumbClose").on("click",
                        function() {
                            v.hide()
                        });
                        t.on("mouseover",
                        function(a) {
                            W.show();
                            0 < c.activeIndex && Q.addClass("active");
                            c.activeIndex < c.imgs.length - 1 && R.addClass("active")
                        }).on("mouseenter",
                        function(a) {
                            K = -1
                        }).on("mouseout",
                        function(a) {
                            W.hide();
                            Q.removeClass("active");
                            R.removeClass("active")
                        }).on("mousedown",
                        function(a) {
                            K = a.pageX || a.clientX;
                            S = a.pageY || a.clientY;
                            A = document.body.clientWidth;
                            B = document.body.clientHeight;
                            a.stopPropagation()
                        }).on("mousemove",
                        function(a) {
                            if (0 < K) {
                                var b = a.pageX || a.clientX;
                                a = a.pageY || a.clientY;
                                var c = f.offset(),
                                d = c.left + (b - K),
                                n = c.top + (a - S),
                                s = f.width(),
                                k = f.height();
                                F && (s = [k, k = s][0]);
                                s > A ? 0 < d ? d = 0 : d < A - s && (d = A - s) : d = c.left;
                                k > B ? 0 < n ? n = 0 : n < B - k && (n = B - k) : n = c.top;
                                f.offset({
                                    left: d,
                                    top: n
                                });
                                K = b;
                                S = a;
                                M()
                            }
                        }).on("mouseup",
                        function(a) {
                            K = -1
                        });
                        var U, X, Y, Z, $;
                        C.on("click",
                        function() {
                            var a = window.parent.document,
                            c = b(a.getElementById("J_pg"));
                            U ? (U = !1, c.css({
                                top: Z,
                                left: $,
                                width: X,
                                height: Y
                            })) : (U = !0, X = document.body.clientWidth, Y = document.body.clientHeight, Z = c.css("top"), $ = c.css("left"), c.css({
                                top: 0,
                                left: 0,
                                width: a.body.clientWidth,
                                height: a.body.clientHeight
                            }))
                        });
                        aa.on("click",
                        function() {
                            D()
                        });
                        ba.on("click",
                        function() {
                            d()
                        });
                        ca.on("click",
                        function() {
                            var a = f.attr("class").match(/(rotate)(\d*)/);
                            if (a) {
                                var b = (1 * a[2] + 90) % 360;
                                f.removeClass(a[0]).addClass("rotate" + b);
                                H.removeClass(a[0]).addClass("rotate" + b);
                                N(b);
                                x(b);
                                F = 90 == b || 270 == b
                            } else f.addClass("rotate90"),
                            H.addClass("rotate90"),
                            N("90"),
                            x("90"),
                            F = !0
                        });
                        da.on("click",
                        function() {
                            b(".closeWin").click()
                        });
                        b(window).on("resize",
                        function() {
                            z()
                        });
                        document.attachEvent ? document.attachEvent("on" + V,
                        function(a) {
                            0 < parseInt(a.wheelDelta || -a.detail) ? D() : d()
                        }) : document.addEventListener && document.addEventListener(V,
                        function(a) {
                            0 < parseInt(a.wheelDelta || -a.detail) ? D() : d()
                        },
                        !1);
                        document.onkeydown = function(a) {
                            a = a || window.event;
                            a.keyCode && (37 == a.keyCode && (0 < c.activeIndex && c.activeIndex--, E()), 39 == a.keyCode && (c.activeIndex < c.imgs.length - 1 && c.activeIndex++, E()))
                        };
                        J();
                        return this
                    }
                });
                b.extend({
                    openPhotoGallery: function(C) {
                        var D = b(C).attr("imghref");
                        if (D) {
                            var d = document.body.clientHeight,
                            G = document.body.clientWidth,
                            N = G / d,
                            x, w, z = document.body.clientHeight - 16,
                            M = document.body.clientWidth - 8;
                            w = G;
                            x = d;
                            w > M && (w = M, x = Math.max(d, Math.ceil(w / N)));
                            x > z && (x = z, w = Math.max(G, Math.ceil(x * N)));
                            var J = 0,
                            E = [];
                            b(C).parent().parent().parent().find(".gallery-pic").each(function(d, c) {
                                var t = b(this).attr("imghref");
                                t == D && (J = d);
                                E.push({
                                    url: t,
                                    imgHeight: 0,
                                    imgWidth: 0
                                })
                            });
                            localStorage.photoGalleryImgs = JSON.stringify(E);
                            localStorage.photoGalleryActiveIndex = J;
                            b("#J_pg").remove();
                            b("\x3ciframe id\x3d'piciframe' style\x3d'z-index:99999'\x3e\x3c/iframe").appendTo("body").attr("id", "J_pg").attr("src", "/static/jquery-photo-gallery/gallery.html").css({
                                position: "absolute",
                                left: (document.body.clientWidth - w) / 2,
                                top: (document.body.clientHeight - x) / 2,
                                width: w,
                                height: x,
                                background: "rgba(177, 178, 179, 0.6)",
                                border: "1px solid #6D6D6D",
                                "border-radius": "4px"
                            });
                            b(window.parent.document.getElementById("J_pg")).css({
                                top: 0,
                                left: 0,
                                width: "100%",
                                height: "100%"
                            })
                        }
                    },
                    initGallery: function() {
                        var C = localStorage.photoGalleryActiveIndex,
                        D = JSON.parse(localStorage.photoGalleryImgs);
                        localStorage.removeItem("photoGalleryActiveIndex");
                        localStorage.removeItem("photoGalleryImgs");
                        b(".gallery").photoGallery({
                            imgs: D,
                            activeIndex: C
                        });
                        b(".closeWin").click(function() {
                            var d = (window.parent || window.top).document.getElementById("J_pg");
                            b(d).remove()
                        });
                        b(".gallery").click(function(d) { (b(d.target).hasClass("gallery") || b(d.target).hasClass("oper") || b(d.target).hasClass("thumbnails")) && b(".closeWin").click()
                        });
                        window.parent.window.onkeydown = function(d) {
                            27 == d.which && b(".closeWin").click()
                        };
                        window.parent.window.onresize = function() {
                            null != window && null != window.parent && b(".box").css({
                                height: parseInt(window.parent.document.body.clientHeight - 2),
                                width: parseInt(window.parent.document.body.clientWidth - 2)
                            })
                        }
                    }
                })
            })(jQuery);
            var DropDownList = function(b, p) {
                var m = [],
                n = function() {
                    for (;;) {
                        var a = ((new Date).getTime() + "Select" + Math.floor(1E5 * Math.random())).toString();
                        if (!m[a]) return m[a] = !0,
                        a
                    }
                },
                k = function(a, b) {
                    source = a;
                    var c = Array.prototype.slice.call(arguments, 1),
                    e = Object.prototype.toString;
                    return c.length ? (c = 1 == c.length ? null !== b && /\[object Array\]|\[object Object\]/.test(e.call(b)) ? b: c: c, source.replace(/#\{(.+?)\}/g,
                    function(a, b) {
                        var d = c[b];
                        "[object Function]" == e.call(d) && (d = d(b));
                        return "undefined" == typeof d ? "": d
                    })) : source
                },
                l = function() {};
                l.prototype = {
                    constructor: l,
                    _addListItem: function(a) {
                        var d = this,
                        c = b("\x3ca\x3e\x3c/a\x3e");
                        c.attr("data-value", a.value);
                        c.html(a.text);
                        c.attr("href", "javascript:;");
                        c.attr("style", "text-decoration:none;");
                        a = b("\x3cli\x3e\x3c/li\x3e");
                        c.click(function(a) {
                            a = b(this).attr("data-value");
                            d.select.val(a);
                            d.select.trigger("change");
                            d.hide().listEle.trigger("hide-list")
                        });
                        a.append(c);
                        d.listUL.append(a);
                        return d
                    },
                    _list: function() {
                        var a = this;
                        this.listId = k("dropdownOptionsFor#{id}", {
                            id: this.select.attr("id") || n()
                        });
                        this.listEle = b("\x3cdiv\x3e\x3c/div\x3e").attr("id", this.listId).addClass("dropdown-list").addClass("custom-scroll-bar").hide();
                        this.listUL = b("\x3cul\x3e\x3c/ul\x3e").appendTo(this.listEle);
                        this.listBox = b("\x3cdiv\x3e\x3c/div\x3e").appendTo(this.listEle).addClass("dropdown-custombox");
                        this.configs.box && this.listBox.append(this.configs.box);
                        var d = function(c) {
                            b.contains(a.listEle[0], c.target) || a.hide()
                        };
                        this.listEle.bind("show-list",
                        function() {
                            b(document).bind("click", d)
                        });
                        this.listEle.bind("hide-list",
                        function() {
                            b(document).unbind("click", d)
                        });
                        b.each(this.select[0].options,
                        function(c, b) {
                            a._addListItem(b)
                        });
                        this.listUL.addClass("dropdown-options");
                        var c = function() {
                            b(window).unbind("resize", c);
                            setTimeout(function() {
                                var d = a.element.position();
                                a.listEle.css({
                                    left: d.left - 1,
                                    top: d.top + a.element[0].offsetHeight
                                });
                                b(window).resize(c)
                            },
                            10)
                        };
                        b(window).resize(c);
                        a.listEle.appendTo(a.dropdownEle)
                    },
                    _createFromSelect: function() {
                        this.select = b(this.configs.select);
                        var a = k("dropdownThemeFor#{id}", {
                            id: this.select.attr("id")
                        }),
                        d = b("\x3ca\x3e\x3c/a\x3e").attr({
                            id: a,
                            href: "javascript:;",
                            "class": "dropdown-select",
                            style: "text-decoration:none;"
                        }),
                        c = b("\x3cspan\x3e\x3c/span\x3e").attr({
                            "class": "dropdown-input"
                        }),
                        a = b("\x3ci\x3e\x3c/i\x3e").attr({
                            "class": "icon-caret-down"
                        }),
                        e = this.select.attr("class");
                        0 <= this.select[0].selectedIndex && c.html("\x3cnobr\x3e" + this.select[0].options[this.select[0].selectedIndex].text + "\x3c/nobr\x3e");
                        a.html("\x26#160");
                        d.append(c).append(a);
                        e && b.each(e.split(/\s+/),
                        function(a, c) {
                            d.addClass(c)
                        });
                        this.select.hide();
                        this.dropdownEle = b("\x3cdiv\x3e\x3c/div\x3e").append(d).insertAfter(this.select).addClass("mod-dropdownlist").addClass(this.configs.className || "").addClass("dropdown");
                        if (e = this.select.attr("data-width") || "") {
                            var h = parseInt(c.css("padding-left"), 10) || 0,
                            f = parseInt(c.css("padding-right"), 10) || 0;
                            c.css("width", parseInt(e, 10) - h - f - a[0].offsetWidth - 7)
                        }
                        c.parent().click(function(a) {
                            "true" != b(this).attr("stop") && (1 == b(this).attr("data-showwing") ? g.hide() : g.show(), a.preventDefault(), a.stopPropagation())
                        });
                        var g = this;
                        this.select.change(function(a) {
                            0 <= g.select[0].selectedIndex && c.html("\x3cnobr\x3e" + g.select[0].options[g.select[0].selectedIndex].text + "\x3c/nobr\x3e")
                        });
                        this.input = c;
                        this.icon = a;
                        this.element = c.parent();
                        this._list();
                        return d
                    },
                    _init: function(a) {
                        this.configs = b.extend({
                            container: "body"
                        },
                        a || {});
                        var d;
                        a = {};
                        a.id = this.configs.attrs.id || n();
                        a["data-cols"] = this.configs.attrs.column || 8;
                        this.configs.attrs.width && (a["data-width"] = this.configs.attrs.width);
                        if (this.configs.options) {
                            this.configs.attrs.id && (a.id = this.configs.attrs.id);
                            a["data-cols"] = this.configs.attrs.column || 8;
                            this.configs.attrs.width && (a["data-width"] = this.configs.attrs.width);
                            d = b("\x3cselect\x3e\x3c/select\x3e").attr(a).hide().appendTo(b(this.configs.container));
                            var c;
                            b.each(this.configs.options,
                            function(a, b) {
                                c = new Option(b[0] || b, b[1] || b[0] || b);
                                b[2] && (c.selected = !0, c.setAttribute("selected", "selected"));
                                d[0].options.add(c)
                            });
                            this.configs.select = d
                        } else if (d = this.configs.select.attr(a).hide(), !d.is("select")) return;
                        b("#" + k("dropdownThemeFor#{id}", {
                            id: d[0].id
                        })).remove();
                        d[0].options.length && this._createFromSelect(d).show();
                        return this
                    },
                    val: function(a) {
                        return (a || "").length ? (this.select.val(a).trigger("change"), this) : this.select.val()
                    },
                    change: function() {
                        if (arguments.length) return this.select.change.apply(this.select, arguments);
                        this.select.trigger("change");
                        return this
                    },
                    show: function() {
                        var a = this;
                        a.element.attr("data-showwing", 1);
                        var d = parseInt(a.configs.attrs.height, 10) || 30;
                        b("li a", a.listUL).css({
                            height: d + "px",
                            "line-height": d + "px"
                        });
                        var c = a.select.attr("data-cols") || "10",
                        e = b("li", a.listUL),
                        d = d * Math.min(parseInt(c, 10), e.length),
                        c = b("#" + k("dropdownThemeFor#{id}", {
                            id: a.select.attr("id")
                        })),
                        h = c.position(),
                        c = {
                            position: "absolute",
                            left: h.left,
                            top: h.top + c[0].offsetHeight,
                            width: c[0].offsetWidth - (parseInt(c.css("border-left-width"), 10) || 0) - (parseInt(c.css("border-right-width"), 10) || 0),
                            "z-index": (parseInt(a.select.parent().css("z-index"), 10) || 0) + 10
                        };
                        a.listUL.css({
                            height: d
                        });
                        a.listEle.css(c).show().trigger("show-list");
                        var f;
                        b.each(e,
                        function(c, d) {
                            if (a.select.val() == b("a", d).attr("data-value")) return f = d,
                            !1
                        });
                        f && setTimeout(function() {
                            b(f).siblings("li").removeClass("dropdown-options-focus").end().addClass("dropdown-options-focus");
                            f.parentNode.scrollTop = f.offsetTop
                        },
                        10);
                        return a
                    },
                    hide: function() {
                        this.element.attr("data-showwing", 0);
                        this.listEle.hide().trigger("hide-list");
                        return this
                    },
                    add: function(a) {
                        a = new Option(a.text, a.value);
                        this.select[0].options.add(a);
                        this._addListItem(a);
                        return this
                    }
                };
                return {
                    version: "1.3",
                    create: function(a) {
                        return (new l)._init(a)
                    }
                }
            } (jQuery); (function(r) {
                r.fn.qrcode = function(h) {
                    var s;
                    function u(a) {
                        this.mode = s;
                        this.data = a
                    }
                    function o(a, c) {
                        this.typeNumber = a;
                        this.errorCorrectLevel = c;
                        this.modules = null;
                        this.moduleCount = 0;
                        this.dataCache = null;
                        this.dataList = []
                    }
                    function q(a, c) {
                        if (void 0 == a.length) throw Error(a.length + "/" + c);
                        for (var d = 0; d < a.length && 0 == a[d];) d++;
                        this.num = Array(a.length - d + c);
                        for (var b = 0; b < a.length - d; b++) this.num[b] = a[b + d]
                    }
                    function p(a, c) {
                        this.totalCount = a;
                        this.dataCount = c
                    }
                    function t() {
                        this.buffer = [];
                        this.length = 0
                    }
                    u.prototype = {
                        getLength: function() {
                            return this.data.length
                        },
                        write: function(a) {
                            for (var c = 0; c < this.data.length; c++) a.put(this.data.charCodeAt(c), 8)
                        }
                    };
                    o.prototype = {
                        addData: function(a) {
                            this.dataList.push(new u(a));
                            this.dataCache = null
                        },
                        isDark: function(a, c) {
                            if (0 > a || this.moduleCount <= a || 0 > c || this.moduleCount <= c) throw Error(a + "," + c);
                            return this.modules[a][c]
                        },
                        getModuleCount: function() {
                            return this.moduleCount
                        },
                        make: function() {
                            if (1 > this.typeNumber) {
                                for (var a = 1,
                                a = 1; 40 > a; a++) {
                                    for (var c = p.getRSBlocks(a, this.errorCorrectLevel), d = new t, b = 0, e = 0; e < c.length; e++) b += c[e].dataCount;
                                    for (e = 0; e < this.dataList.length; e++) c = this.dataList[e],
                                    d.put(c.mode, 4),
                                    d.put(c.getLength(), j.getLengthInBits(c.mode, a)),
                                    c.write(d);
                                    if (d.getLengthInBits() <= 8 * b) break
                                }
                                this.typeNumber = a
                            }
                            this.makeImpl(!1, this.getBestMaskPattern())
                        },
                        makeImpl: function(a, c) {
                            this.moduleCount = 4 * this.typeNumber + 17;
                            this.modules = Array(this.moduleCount);
                            for (var d = 0; d < this.moduleCount; d++) {
                                this.modules[d] = Array(this.moduleCount);
                                for (var b = 0; b < this.moduleCount; b++) this.modules[d][b] = null
                            }
                            this.setupPositionProbePattern(0, 0);
                            this.setupPositionProbePattern(this.moduleCount - 7, 0);
                            this.setupPositionProbePattern(0, this.moduleCount - 7);
                            this.setupPositionAdjustPattern();
                            this.setupTimingPattern();
                            this.setupTypeInfo(a, c);
                            7 <= this.typeNumber && this.setupTypeNumber(a);
                            null == this.dataCache && (this.dataCache = o.createData(this.typeNumber, this.errorCorrectLevel, this.dataList));
                            this.mapData(this.dataCache, c)
                        },
                        setupPositionProbePattern: function(a, c) {
                            for (var d = -1; 7 >= d; d++) if (! ( - 1 >= a + d || this.moduleCount <= a + d)) for (var b = -1; 7 >= b; b++) - 1 >= c + b || this.moduleCount <= c + b || (this.modules[a + d][c + b] = 0 <= d && 6 >= d && (0 == b || 6 == b) || 0 <= b && 6 >= b && (0 == d || 6 == d) || 2 <= d && 4 >= d && 2 <= b && 4 >= b ? !0 : !1)
                        },
                        getBestMaskPattern: function() {
                            for (var a = 0,
                            c = 0,
                            d = 0; 8 > d; d++) {
                                this.makeImpl(!0, d);
                                var b = j.getLostPoint(this);
                                if (0 == d || a > b) a = b,
                                c = d
                            }
                            return c
                        },
                        createMovieClip: function(a, c, d) {
                            a = a.createEmptyMovieClip(c, d);
                            this.make();
                            for (c = 0; c < this.modules.length; c++) for (var d = 1 * c,
                            b = 0; b < this.modules[c].length; b++) {
                                var e = 1 * b;
                                this.modules[c][b] && (a.beginFill(0, 100), a.moveTo(e, d), a.lineTo(e + 1, d), a.lineTo(e + 1, d + 1), a.lineTo(e, d + 1), a.endFill())
                            }
                            return a
                        },
                        setupTimingPattern: function() {
                            for (var a = 8; a < this.moduleCount - 8; a++) null == this.modules[a][6] && (this.modules[a][6] = 0 == a % 2);
                            for (a = 8; a < this.moduleCount - 8; a++) null == this.modules[6][a] && (this.modules[6][a] = 0 == a % 2)
                        },
                        setupPositionAdjustPattern: function() {
                            for (var a = j.getPatternPosition(this.typeNumber), c = 0; c < a.length; c++) for (var d = 0; d < a.length; d++) {
                                var b = a[c],
                                e = a[d];
                                if (null == this.modules[b][e]) for (var f = -2; 2 >= f; f++) for (var i = -2; 2 >= i; i++) this.modules[b + f][e + i] = -2 == f || 2 == f || -2 == i || 2 == i || 0 == f && 0 == i ? !0 : !1
                            }
                        },
                        setupTypeNumber: function(a) {
                            for (var c = j.getBCHTypeNumber(this.typeNumber), d = 0; 18 > d; d++) {
                                var b = !a && 1 == (c >> d & 1);
                                this.modules[Math.floor(d / 3)][d % 3 + this.moduleCount - 8 - 3] = b
                            }
                            for (d = 0; 18 > d; d++) b = !a && 1 == (c >> d & 1),
                            this.modules[d % 3 + this.moduleCount - 8 - 3][Math.floor(d / 3)] = b
                        },
                        setupTypeInfo: function(a, c) {
                            for (var d = j.getBCHTypeInfo(this.errorCorrectLevel << 3 | c), b = 0; 15 > b; b++) {
                                var e = !a && 1 == (d >> b & 1);
                                6 > b ? this.modules[b][8] = e: 8 > b ? this.modules[b + 1][8] = e: this.modules[this.moduleCount - 15 + b][8] = e
                            }
                            for (b = 0; 15 > b; b++) e = !a && 1 == (d >> b & 1),
                            8 > b ? this.modules[8][this.moduleCount - b - 1] = e: 9 > b ? this.modules[8][15 - b - 1 + 1] = e: this.modules[8][15 - b - 1] = e;
                            this.modules[this.moduleCount - 8][8] = !a
                        },
                        mapData: function(a, c) {
                            for (var d = -1,
                            b = this.moduleCount - 1,
                            e = 7,
                            f = 0,
                            i = this.moduleCount - 1; 0 < i; i -= 2) for (6 == i && i--;;) {
                                for (var g = 0; 2 > g; g++) if (null == this.modules[b][i - g]) {
                                    var n = !1;
                                    f < a.length && (n = 1 == (a[f] >>> e & 1));
                                    j.getMask(c, b, i - g) && (n = !n);
                                    this.modules[b][i - g] = n;
                                    e--; - 1 == e && (f++, e = 7)
                                }
                                b += d;
                                if (0 > b || this.moduleCount <= b) {
                                    b -= d;
                                    d = -d;
                                    break
                                }
                            }
                        }
                    };
                    o.PAD0 = 236;
                    o.PAD1 = 17;
                    o.createData = function(a, c, d) {
                        for (var c = p.getRSBlocks(a, c), b = new t, e = 0; e < d.length; e++) {
                            var f = d[e];
                            b.put(f.mode, 4);
                            b.put(f.getLength(), j.getLengthInBits(f.mode, a));
                            f.write(b)
                        }
                        for (e = a = 0; e < c.length; e++) a += c[e].dataCount;
                        if (b.getLengthInBits() > 8 * a) throw Error("code length overflow. (" + b.getLengthInBits() + ">" + 8 * a + ")");
                        for (b.getLengthInBits() + 4 <= 8 * a && b.put(0, 4); 0 != b.getLengthInBits() % 8;) b.putBit(!1);
                        for (; ! (b.getLengthInBits() >= 8 * a);) {
                            b.put(o.PAD0, 8);
                            if (b.getLengthInBits() >= 8 * a) break;
                            b.put(o.PAD1, 8)
                        }
                        return o.createBytes(b, c)
                    };
                    o.createBytes = function(a, c) {
                        for (var d = 0,
                        b = 0,
                        e = 0,
                        f = Array(c.length), i = Array(c.length), g = 0; g < c.length; g++) {
                            var n = c[g].dataCount,
                            h = c[g].totalCount - n,
                            b = Math.max(b, n),
                            e = Math.max(e, h);
                            f[g] = Array(n);
                            for (var k = 0; k < f[g].length; k++) f[g][k] = 255 & a.buffer[k + d];
                            d += n;
                            k = j.getErrorCorrectPolynomial(h);
                            n = (new q(f[g], k.getLength() - 1)).mod(k);
                            i[g] = Array(k.getLength() - 1);
                            for (k = 0; k < i[g].length; k++) h = k + n.getLength() - i[g].length,
                            i[g][k] = 0 <= h ? n.get(h) : 0
                        }
                        for (k = g = 0; k < c.length; k++) g += c[k].totalCount;
                        d = Array(g);
                        for (k = n = 0; k < b; k++) for (g = 0; g < c.length; g++) k < f[g].length && (d[n++] = f[g][k]);
                        for (k = 0; k < e; k++) for (g = 0; g < c.length; g++) k < i[g].length && (d[n++] = i[g][k]);
                        return d
                    };
                    s = 4;
                    for (var j = {
                        PATTERN_POSITION_TABLE: [[], [6, 18], [6, 22], [6, 26], [6, 30], [6, 34], [6, 22, 38], [6, 24, 42], [6, 26, 46], [6, 28, 50], [6, 30, 54], [6, 32, 58], [6, 34, 62], [6, 26, 46, 66], [6, 26, 48, 70], [6, 26, 50, 74], [6, 30, 54, 78], [6, 30, 56, 82], [6, 30, 58, 86], [6, 34, 62, 90], [6, 28, 50, 72, 94], [6, 26, 50, 74, 98], [6, 30, 54, 78, 102], [6, 28, 54, 80, 106], [6, 32, 58, 84, 110], [6, 30, 58, 86, 114], [6, 34, 62, 90, 118], [6, 26, 50, 74, 98, 122], [6, 30, 54, 78, 102, 126], [6, 26, 52, 78, 104, 130], [6, 30, 56, 82, 108, 134], [6, 34, 60, 86, 112, 138], [6, 30, 58, 86, 114, 142], [6, 34, 62, 90, 118, 146], [6, 30, 54, 78, 102, 126, 150], [6, 24, 50, 76, 102, 128, 154], [6, 28, 54, 80, 106, 132, 158], [6, 32, 58, 84, 110, 136, 162], [6, 26, 54, 82, 110, 138, 166], [6, 30, 58, 86, 114, 142, 170]],
                        G15: 1335,
                        G18: 7973,
                        G15_MASK: 21522,
                        getBCHTypeInfo: function(a) {
                            for (var c = a << 10; 0 <= j.getBCHDigit(c) - j.getBCHDigit(j.G15);) c ^= j.G15 << j.getBCHDigit(c) - j.getBCHDigit(j.G15);
                            return (a << 10 | c) ^ j.G15_MASK
                        },
                        getBCHTypeNumber: function(a) {
                            for (var c = a << 12; 0 <= j.getBCHDigit(c) - j.getBCHDigit(j.G18);) c ^= j.G18 << j.getBCHDigit(c) - j.getBCHDigit(j.G18);
                            return a << 12 | c
                        },
                        getBCHDigit: function(a) {
                            for (var c = 0; 0 != a;) c++,
                            a >>>= 1;
                            return c
                        },
                        getPatternPosition: function(a) {
                            return j.PATTERN_POSITION_TABLE[a - 1]
                        },
                        getMask: function(a, c, d) {
                            switch (a) {
                            case 0:
                                return 0 == (c + d) % 2;
                            case 1:
                                return 0 == c % 2;
                            case 2:
                                return 0 == d % 3;
                            case 3:
                                return 0 == (c + d) % 3;
                            case 4:
                                return 0 == (Math.floor(c / 2) + Math.floor(d / 3)) % 2;
                            case 5:
                                return 0 == c * d % 2 + c * d % 3;
                            case 6:
                                return 0 == (c * d % 2 + c * d % 3) % 2;
                            case 7:
                                return 0 == (c * d % 3 + (c + d) % 2) % 2;
                            default:
                                throw Error("bad maskPattern:" + a);
                            }
                        },
                        getErrorCorrectPolynomial: function(a) {
                            for (var c = new q([1], 0), d = 0; d < a; d++) c = c.multiply(new q([1, l.gexp(d)], 0));
                            return c
                        },
                        getLengthInBits: function(a, c) {
                            if (1 <= c && 10 > c) switch (a) {
                            case 1:
                                return 10;
                            case 2:
                                return 9;
                            case s:
                                return 8;
                            case 8:
                                return 8;
                            default:
                                throw Error("mode:" + a);
                            } else if (27 > c) switch (a) {
                            case 1:
                                return 12;
                            case 2:
                                return 11;
                            case s:
                                return 16;
                            case 8:
                                return 10;
                            default:
                                throw Error("mode:" + a);
                            } else if (41 > c) switch (a) {
                            case 1:
                                return 14;
                            case 2:
                                return 13;
                            case s:
                                return 16;
                            case 8:
                                return 12;
                            default:
                                throw Error("mode:" + a);
                            } else throw Error("type:" + c);
                        },
                        getLostPoint: function(a) {
                            for (var c = a.getModuleCount(), d = 0, b = 0; b < c; b++) for (var e = 0; e < c; e++) {
                                for (var f = 0,
                                i = a.isDark(b, e), g = -1; 1 >= g; g++) if (! (0 > b + g || c <= b + g)) for (var h = -1; 1 >= h; h++) 0 > e + h || c <= e + h || 0 == g && 0 == h || i == a.isDark(b + g, e + h) && f++;
                                5 < f && (d += 3 + f - 5)
                            }
                            for (b = 0; b < c - 1; b++) for (e = 0; e < c - 1; e++) if (f = 0, a.isDark(b, e) && f++, a.isDark(b + 1, e) && f++, a.isDark(b, e + 1) && f++, a.isDark(b + 1, e + 1) && f++, 0 == f || 4 == f) d += 3;
                            for (b = 0; b < c; b++) for (e = 0; e < c - 6; e++) a.isDark(b, e) && !a.isDark(b, e + 1) && a.isDark(b, e + 2) && a.isDark(b, e + 3) && a.isDark(b, e + 4) && !a.isDark(b, e + 5) && a.isDark(b, e + 6) && (d += 40);
                            for (e = 0; e < c; e++) for (b = 0; b < c - 6; b++) a.isDark(b, e) && !a.isDark(b + 1, e) && a.isDark(b + 2, e) && a.isDark(b + 3, e) && a.isDark(b + 4, e) && !a.isDark(b + 5, e) && a.isDark(b + 6, e) && (d += 40);
                            for (e = f = 0; e < c; e++) for (b = 0; b < c; b++) a.isDark(b, e) && f++;
                            a = Math.abs(100 * f / c / c - 50) / 5;
                            return d + 10 * a
                        }
                    },
                    l = {
                        glog: function(a) {
                            if (1 > a) throw Error("glog(" + a + ")");
                            return l.LOG_TABLE[a]
                        },
                        gexp: function(a) {
                            for (; 0 > a;) a += 255;
                            for (; 256 <= a;) a -= 255;
                            return l.EXP_TABLE[a]
                        },
                        EXP_TABLE: Array(256),
                        LOG_TABLE: Array(256)
                    },
                    m = 0; 8 > m; m++) l.EXP_TABLE[m] = 1 << m;
                    for (m = 8; 256 > m; m++) l.EXP_TABLE[m] = l.EXP_TABLE[m - 4] ^ l.EXP_TABLE[m - 5] ^ l.EXP_TABLE[m - 6] ^ l.EXP_TABLE[m - 8];
                    for (m = 0; 255 > m; m++) l.LOG_TABLE[l.EXP_TABLE[m]] = m;
                    q.prototype = {
                        get: function(a) {
                            return this.num[a]
                        },
                        getLength: function() {
                            return this.num.length
                        },
                        multiply: function(a) {
                            for (var c = Array(this.getLength() + a.getLength() - 1), d = 0; d < this.getLength(); d++) for (var b = 0; b < a.getLength(); b++) c[d + b] ^= l.gexp(l.glog(this.get(d)) + l.glog(a.get(b)));
                            return new q(c, 0)
                        },
                        mod: function(a) {
                            if (0 > this.getLength() - a.getLength()) return this;
                            for (var c = l.glog(this.get(0)) - l.glog(a.get(0)), d = Array(this.getLength()), b = 0; b < this.getLength(); b++) d[b] = this.get(b);
                            for (b = 0; b < a.getLength(); b++) d[b] ^= l.gexp(l.glog(a.get(b)) + c);
                            return (new q(d, 0)).mod(a)
                        }
                    };
                    p.RS_BLOCK_TABLE = [[1, 26, 19], [1, 26, 16], [1, 26, 13], [1, 26, 9], [1, 44, 34], [1, 44, 28], [1, 44, 22], [1, 44, 16], [1, 70, 55], [1, 70, 44], [2, 35, 17], [2, 35, 13], [1, 100, 80], [2, 50, 32], [2, 50, 24], [4, 25, 9], [1, 134, 108], [2, 67, 43], [2, 33, 15, 2, 34, 16], [2, 33, 11, 2, 34, 12], [2, 86, 68], [4, 43, 27], [4, 43, 19], [4, 43, 15], [2, 98, 78], [4, 49, 31], [2, 32, 14, 4, 33, 15], [4, 39, 13, 1, 40, 14], [2, 121, 97], [2, 60, 38, 2, 61, 39], [4, 40, 18, 2, 41, 19], [4, 40, 14, 2, 41, 15], [2, 146, 116], [3, 58, 36, 2, 59, 37], [4, 36, 16, 4, 37, 17], [4, 36, 12, 4, 37, 13], [2, 86, 68, 2, 87, 69], [4, 69, 43, 1, 70, 44], [6, 43, 19, 2, 44, 20], [6, 43, 15, 2, 44, 16], [4, 101, 81], [1, 80, 50, 4, 81, 51], [4, 50, 22, 4, 51, 23], [3, 36, 12, 8, 37, 13], [2, 116, 92, 2, 117, 93], [6, 58, 36, 2, 59, 37], [4, 46, 20, 6, 47, 21], [7, 42, 14, 4, 43, 15], [4, 133, 107], [8, 59, 37, 1, 60, 38], [8, 44, 20, 4, 45, 21], [12, 33, 11, 4, 34, 12], [3, 145, 115, 1, 146, 116], [4, 64, 40, 5, 65, 41], [11, 36, 16, 5, 37, 17], [11, 36, 12, 5, 37, 13], [5, 109, 87, 1, 110, 88], [5, 65, 41, 5, 66, 42], [5, 54, 24, 7, 55, 25], [11, 36, 12], [5, 122, 98, 1, 123, 99], [7, 73, 45, 3, 74, 46], [15, 43, 19, 2, 44, 20], [3, 45, 15, 13, 46, 16], [1, 135, 107, 5, 136, 108], [10, 74, 46, 1, 75, 47], [1, 50, 22, 15, 51, 23], [2, 42, 14, 17, 43, 15], [5, 150, 120, 1, 151, 121], [9, 69, 43, 4, 70, 44], [17, 50, 22, 1, 51, 23], [2, 42, 14, 19, 43, 15], [3, 141, 113, 4, 142, 114], [3, 70, 44, 11, 71, 45], [17, 47, 21, 4, 48, 22], [9, 39, 13, 16, 40, 14], [3, 135, 107, 5, 136, 108], [3, 67, 41, 13, 68, 42], [15, 54, 24, 5, 55, 25], [15, 43, 15, 10, 44, 16], [4, 144, 116, 4, 145, 117], [17, 68, 42], [17, 50, 22, 6, 51, 23], [19, 46, 16, 6, 47, 17], [2, 139, 111, 7, 140, 112], [17, 74, 46], [7, 54, 24, 16, 55, 25], [34, 37, 13], [4, 151, 121, 5, 152, 122], [4, 75, 47, 14, 76, 48], [11, 54, 24, 14, 55, 25], [16, 45, 15, 14, 46, 16], [6, 147, 117, 4, 148, 118], [6, 73, 45, 14, 74, 46], [11, 54, 24, 16, 55, 25], [30, 46, 16, 2, 47, 17], [8, 132, 106, 4, 133, 107], [8, 75, 47, 13, 76, 48], [7, 54, 24, 22, 55, 25], [22, 45, 15, 13, 46, 16], [10, 142, 114, 2, 143, 115], [19, 74, 46, 4, 75, 47], [28, 50, 22, 6, 51, 23], [33, 46, 16, 4, 47, 17], [8, 152, 122, 4, 153, 123], [22, 73, 45, 3, 74, 46], [8, 53, 23, 26, 54, 24], [12, 45, 15, 28, 46, 16], [3, 147, 117, 10, 148, 118], [3, 73, 45, 23, 74, 46], [4, 54, 24, 31, 55, 25], [11, 45, 15, 31, 46, 16], [7, 146, 116, 7, 147, 117], [21, 73, 45, 7, 74, 46], [1, 53, 23, 37, 54, 24], [19, 45, 15, 26, 46, 16], [5, 145, 115, 10, 146, 116], [19, 75, 47, 10, 76, 48], [15, 54, 24, 25, 55, 25], [23, 45, 15, 25, 46, 16], [13, 145, 115, 3, 146, 116], [2, 74, 46, 29, 75, 47], [42, 54, 24, 1, 55, 25], [23, 45, 15, 28, 46, 16], [17, 145, 115], [10, 74, 46, 23, 75, 47], [10, 54, 24, 35, 55, 25], [19, 45, 15, 35, 46, 16], [17, 145, 115, 1, 146, 116], [14, 74, 46, 21, 75, 47], [29, 54, 24, 19, 55, 25], [11, 45, 15, 46, 46, 16], [13, 145, 115, 6, 146, 116], [14, 74, 46, 23, 75, 47], [44, 54, 24, 7, 55, 25], [59, 46, 16, 1, 47, 17], [12, 151, 121, 7, 152, 122], [12, 75, 47, 26, 76, 48], [39, 54, 24, 14, 55, 25], [22, 45, 15, 41, 46, 16], [6, 151, 121, 14, 152, 122], [6, 75, 47, 34, 76, 48], [46, 54, 24, 10, 55, 25], [2, 45, 15, 64, 46, 16], [17, 152, 122, 4, 153, 123], [29, 74, 46, 14, 75, 47], [49, 54, 24, 10, 55, 25], [24, 45, 15, 46, 46, 16], [4, 152, 122, 18, 153, 123], [13, 74, 46, 32, 75, 47], [48, 54, 24, 14, 55, 25], [42, 45, 15, 32, 46, 16], [20, 147, 117, 4, 148, 118], [40, 75, 47, 7, 76, 48], [43, 54, 24, 22, 55, 25], [10, 45, 15, 67, 46, 16], [19, 148, 118, 6, 149, 119], [18, 75, 47, 31, 76, 48], [34, 54, 24, 34, 55, 25], [20, 45, 15, 61, 46, 16]];
                    p.getRSBlocks = function(a, c) {
                        var d = p.getRsBlockTable(a, c);
                        if (void 0 == d) throw Error("bad rs block @ typeNumber:" + a + "/errorCorrectLevel:" + c);
                        for (var b = d.length / 3,
                        e = [], f = 0; f < b; f++) for (var h = d[3 * f + 0], g = d[3 * f + 1], j = d[3 * f + 2], l = 0; l < h; l++) e.push(new p(g, j));
                        return e
                    };
                    p.getRsBlockTable = function(a, c) {
                        switch (c) {
                        case 1:
                            return p.RS_BLOCK_TABLE[4 * (a - 1) + 0];
                        case 0:
                            return p.RS_BLOCK_TABLE[4 * (a - 1) + 1];
                        case 3:
                            return p.RS_BLOCK_TABLE[4 * (a - 1) + 2];
                        case 2:
                            return p.RS_BLOCK_TABLE[4 * (a - 1) + 3]
                        }
                    };
                    t.prototype = {
                        get: function(a) {
                            return 1 == (this.buffer[Math.floor(a / 8)] >>> 7 - a % 8 & 1)
                        },
                        put: function(a, c) {
                            for (var d = 0; d < c; d++) this.putBit(1 == (a >>> c - d - 1 & 1))
                        },
                        getLengthInBits: function() {
                            return this.length
                        },
                        putBit: function(a) {
                            var c = Math.floor(this.length / 8);
                            this.buffer.length <= c && this.buffer.push(0);
                            a && (this.buffer[c] |= 128 >>> this.length % 8);
                            this.length++
                        }
                    };
                    "string" === typeof h && (h = {
                        text: h
                    });
                    h = r.extend({},
                    {
                        render: "canvas",
                        width: 256,
                        height: 256,
                        typeNumber: -1,
                        correctLevel: 2,
                        background: "#ffffff",
                        foreground: "#000000"
                    },
                    h);
                    return this.each(function() {
                        var a;
                        if ("canvas" == h.render) {
                            a = new o(h.typeNumber, h.correctLevel);
                            a.addData(h.text);
                            a.make();
                            var c = document.createElement("canvas");
                            c.width = h.width;
                            c.height = h.height;
                            for (var d = c.getContext("2d"), b = h.width / a.getModuleCount(), e = h.height / a.getModuleCount(), f = 0; f < a.getModuleCount(); f++) for (var i = 0; i < a.getModuleCount(); i++) {
                                d.fillStyle = a.isDark(f, i) ? h.foreground: h.background;
                                var g = Math.ceil((i + 1) * b) - Math.floor(i * b),
                                j = Math.ceil((f + 1) * b) - Math.floor(f * b);
                                d.fillRect(Math.round(i * b), Math.round(f * e), g, j)
                            }
                        } else {
                            a = new o(h.typeNumber, h.correctLevel);
                            a.addData(h.text);
                            a.make();
                            c = r("<table></table>").css("width", h.width + "px").css("height", h.height + "px").css("border", "0px").css("border-collapse", "collapse").css("background-color", h.background);
                            d = h.width / a.getModuleCount();
                            b = h.height / a.getModuleCount();
                            for (e = 0; e < a.getModuleCount(); e++) {
                                f = r("<tr></tr>").css("height", b + "px").appendTo(c);
                                for (i = 0; i < a.getModuleCount(); i++) r("<td></td>").css("width", d + "px").css("background-color", a.isDark(e, i) ? h.foreground: h.background).appendTo(f)
                            }
                        }
                        a = c;
                        jQuery(a).appendTo(this)
                    })
                }
            })(jQuery);
            /*! fly - v1.0.0 - 2015-03-23
* https://github.com/amibug/fly
* Copyright (c) 2015 wuyuedong; Licensed MIT */
            !
            function(a) {
                a.fly = function(b, c) {
                    var d = {
                        version: "1.0.0",
                        autoPlay: !0,
                        vertex_Rtop: 20,
                        speed: 1.2,
                        start: {},
                        end: {},
                        onEnd: a.noop
                    },
                    e = this,
                    f = a(b);
                    e.init = function(a) {
                        this.setOptions(a),
                        !!this.settings.autoPlay && this.play()
                    },
                    e.setOptions = function(b) {
                        this.settings = a.extend(!0, {},
                        d, b);
                        var c = this.settings,
                        e = c.start,
                        g = c.end;
                        f.css({
                            marginTop: "0px",
                            marginLeft: "0px",
                            position: "fixed"
                        }).appendTo("body"),
                        null != g.width && null != g.height && a.extend(!0, e, {
                            width: f.width(),
                            height: f.height()
                        });
                        var h = Math.min(e.top, g.top) - Math.abs(e.left - g.left) / 3;
                        h < c.vertex_Rtop && (h = Math.min(c.vertex_Rtop, Math.min(e.top, g.top)));
                        var i = Math.sqrt(Math.pow(e.top - g.top, 2) + Math.pow(e.left - g.left, 2)),
                        j = Math.ceil(Math.min(Math.max(Math.log(i) / .05 - 75, 30), 100) / c.speed),
                        k = e.top == h ? 0 : -Math.sqrt((g.top - h) / (e.top - h)),
                        l = (k * e.left - g.left) / (k - 1),
                        m = g.left == l ? 0 : (g.top - h) / Math.pow(g.left - l, 2);
                        a.extend(!0, c, {
                            count: -1,
                            steps: j,
                            vertex_left: l,
                            vertex_top: h,
                            curvature: m
                        })
                    },
                    e.play = function() {
                        this.move()
                    },
                    e.move = function() {
                        var b = this.settings,
                        c = b.start,
                        d = b.count,
                        e = b.steps,
                        g = b.end,
                        h = c.left + (g.left - c.left) * d / e,
                        i = 0 == b.curvature ? c.top + (g.top - c.top) * d / e: b.curvature * Math.pow(h - b.vertex_left, 2) + b.vertex_top;
                        if (null != g.width && null != g.height) {
                            var j = e / 2,
                            k = g.width - (g.width - c.width) * Math.cos(j > d ? 0 : (d - j) / (e - j) * Math.PI / 2),
                            l = g.height - (g.height - c.height) * Math.cos(j > d ? 0 : (d - j) / (e - j) * Math.PI / 2);
                            f.css({
                                width: k + "px",
                                height: l + "px",
                                "font-size": Math.min(k, l) + "px"
                            })
                        }
                        f.css({
                            left: h + "px",
                            top: i + "px"
                        }),
                        b.count++;
                        var m = window.requestAnimationFrame(a.proxy(this.move, this));
                        d == e && (window.cancelAnimationFrame(m), b.onEnd.apply(this))
                    },
                    e.destroy = function() {
                        f.remove()
                    },
                    e.init(c)
                },
                a.fn.fly = function(b) {
                    return this.each(function() {
                        void 0 == a(this).data("fly") && a(this).data("fly", new a.fly(this, b))
                    })
                }
            } (jQuery);