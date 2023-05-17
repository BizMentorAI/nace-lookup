import * as cherry_core from '/js/cljs_core.js';
function set_property(object, property, value) {
        object[property] = value
      };
var element_QMARK_ = function (thing) {
return cherry_core.boolean$.call(null, thing["nodeName"]);
}
;
var hide = function (element) {
set_property.call(null, element.style, "display", "none");
return element;
}
;
var unhide = function (element) {
set_property.call(null, element.style, "display", "");
return element;
}
;
var unhideAll = (function () {
 let f1 = function (var_args) {
let args25 = cherry_core.array.call(null);
let len__22109__auto__6 = cherry_core.alength.call(null, arguments);
let i37 = 0;
while(true){
if (cherry_core.truth_((i37 < len__22109__auto__6))) {
args25.push((arguments[i37]));
let G__8 = (i37 + 1);
i37 = G__8;
continue;
};break;
}
;
let argseq__22263__auto__9 = ((0 < cherry_core.alength.call(null, args25))) ? (new cherry_core.IndexedSeq(args25.slice(0), 0, null)) : (null);
return f1.cljs$core$IFn$_invoke$arity$variadic(argseq__22263__auto__9);
};
f1["cljs$core$IFn$_invoke$arity$variadic"] = function (args) {
return cherry_core.clj__GT_js.call(null, cherry_core.map.call(null, unhide, cherry_core.filter.call(null, element_QMARK_, args)));
};
f1["cljs$lang$maxFixedArity"] = 0;
f1["cljs$lang$applyTo"] = function (seq4) {
let self__22127__auto__10 = this;
return self__22127__auto__10.cljs$core$IFn$_invoke$arity$variadic(cherry_core.seq.call(null, seq4));
};
return f1;
})()
;
var set_content = function (element, content) {
if (cherry_core.truth_(cherry_core.string_QMARK_.call(null, content))) {
return element["innerText"] = content;
;} else {
if (cherry_core.truth_(element_QMARK_.call(null, content))) {
return element.appendChild(content);} else {
if (cherry_core.truth_(cherry_core.vector_QMARK_.call(null, content))) {
let seq__1115 = cherry_core.seq.call(null, content);
let chunk__1216 = null;
let count__1317 = 0;
let i__1418 = 0;
while(true){
if (cherry_core.truth_((i__1418 < count__1317))) {
let i19 = cherry_core._nth.call(null, chunk__1216, i__1418);
set_content.call(null, element, i19);
null;
let G__20 = seq__1115;
let G__21 = chunk__1216;
let G__22 = count__1317;
let G__23 = cherry_core.unchecked_inc.call(null, i__1418);
seq__1115 = G__20;
chunk__1216 = G__21;
count__1317 = G__22;
i__1418 = G__23;
continue;
} else {
let temp__22496__auto__24 = cherry_core.seq.call(null, seq__1115);
if (cherry_core.truth_(temp__22496__auto__24)) {
let seq__1125 = temp__22496__auto__24;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__1125))) {
let c__22611__auto__26 = cherry_core.chunk_first.call(null, seq__1125);
let G__27 = cherry_core.chunk_rest.call(null, seq__1125);
let G__28 = c__22611__auto__26;
let G__29 = cherry_core.count.call(null, c__22611__auto__26);
let G__30 = 0;
seq__1115 = G__27;
chunk__1216 = G__28;
count__1317 = G__29;
i__1418 = G__30;
continue;
} else {
let i31 = cherry_core.first.call(null, seq__1125);
set_content.call(null, element, i31);
null;
let G__32 = cherry_core.next.call(null, seq__1125);
let G__33 = null;
let G__34 = 0;
let G__35 = 0;
seq__1115 = G__32;
chunk__1216 = G__33;
count__1317 = G__34;
i__1418 = G__35;
continue;
}}};break;
}
} else {
return null;}}}
}
;
var set_style = function (element, style) {
let seq__3640 = cherry_core.seq.call(null, style);
let chunk__3741 = null;
let count__3842 = 0;
let i__3943 = 0;
while(true){
if (cherry_core.truth_((i__3943 < count__3842))) {
let vec__4447 = cherry_core._nth.call(null, chunk__3741, i__3943);
let property48 = cherry_core.nth.call(null, vec__4447, 0, null);
let value49 = cherry_core.nth.call(null, vec__4447, 1, null);
set_property.call(null, element.style, cherry_core.name.call(null, property48), value49);
null;
let G__50 = seq__3640;
let G__51 = chunk__3741;
let G__52 = count__3842;
let G__53 = cherry_core.unchecked_inc.call(null, i__3943);
seq__3640 = G__50;
chunk__3741 = G__51;
count__3842 = G__52;
i__3943 = G__53;
continue;
} else {
let temp__22496__auto__54 = cherry_core.seq.call(null, seq__3640);
if (cherry_core.truth_(temp__22496__auto__54)) {
let seq__3655 = temp__22496__auto__54;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__3655))) {
let c__22611__auto__56 = cherry_core.chunk_first.call(null, seq__3655);
let G__57 = cherry_core.chunk_rest.call(null, seq__3655);
let G__58 = c__22611__auto__56;
let G__59 = cherry_core.count.call(null, c__22611__auto__56);
let G__60 = 0;
seq__3640 = G__57;
chunk__3741 = G__58;
count__3842 = G__59;
i__3943 = G__60;
continue;
} else {
let vec__6164 = cherry_core.first.call(null, seq__3655);
let property65 = cherry_core.nth.call(null, vec__6164, 0, null);
let value66 = cherry_core.nth.call(null, vec__6164, 1, null);
set_property.call(null, element.style, cherry_core.name.call(null, property65), value66);
null;
let G__67 = cherry_core.next.call(null, seq__3655);
let G__68 = null;
let G__69 = 0;
let G__70 = 0;
seq__3640 = G__67;
chunk__3741 = G__68;
count__3842 = G__69;
i__3943 = G__70;
continue;
}}};break;
}

}
;
var create_element = function (name, opts) {
if (cherry_core.truth_(cherry_core.keyword("is").call(null, opts))) {
return document.createElement(name, ({ "is": cherry_core.keyword("is").call(null, opts) }));} else {
return document.createElement(name);}
}
;
var tag = (function () {
 let f71 = function (var_args) {
let G__7475 = cherry_core.alength.call(null, arguments);
switch (G__7475) {case 1:
return f71.cljs$core$IFn$_invoke$arity$1((arguments[0]));
break;
case 2:
return f71.cljs$core$IFn$_invoke$arity$2((arguments[0]), (arguments[1]));
break;
case 3:
return f71.cljs$core$IFn$_invoke$arity$3((arguments[0]), (arguments[1]), (arguments[2]));
break;
default:
throw new Error(cherry_core.str.call(null, "Invalid arity: ", cherry_core.alength.call(null, arguments)))}
};
f71["cljs$core$IFn$_invoke$arity$1"] = function (tname) {
return tag.call(null, tname, cherry_core.array_map(), null);
};
f71["cljs$core$IFn$_invoke$arity$2"] = function (tname, arg) {
if (cherry_core.truth_(cherry_core.map_QMARK_.call(null, cherry_core.js__GT_clj.call(null, arg)))) {
return tag.call(null, tname, arg, null);} else {
return tag.call(null, tname, cherry_core.array_map(), arg);}
};
f71["cljs$core$IFn$_invoke$arity$3"] = function (tname, opts, content) {
let opts77 = cherry_core.js__GT_clj.call(null, opts, cherry_core.array_map(cherry_core.keyword("keywordize-keys"), true));
let element78 = create_element.call(null, tname, opts77);
let seq__7983 = cherry_core.seq.call(null, cherry_core.dissoc.call(null, opts77, cherry_core.keyword("is")));
let chunk__8084 = null;
let count__8185 = 0;
let i__8286 = 0;
while(true){
if (cherry_core.truth_((i__8286 < count__8185))) {
let vec__8790 = cherry_core._nth.call(null, chunk__8084, i__8286);
let key91 = cherry_core.nth.call(null, vec__8790, 0, null);
let value92 = cherry_core.nth.call(null, vec__8790, 1, null);
let G__9394 = key91;
let G__9395 = (cherry_core.keyword_QMARK_.call(null, G__9394)) ? (cherry_core.subs.call(null, cherry_core.str.call(null, G__9394), 1)) : (null);
switch (G__9395) {case "html":
element78["innerHTML"] = value92;

break;
case "class":
element78["className"] = value92;

break;
case "style":
set_style.call(null, element78, value92)
break;
default:
element78.setAttribute(cherry_core.name.call(null, key91), value92)};
null;
let G__97 = seq__7983;
let G__98 = chunk__8084;
let G__99 = count__8185;
let G__100 = cherry_core.unchecked_inc.call(null, i__8286);
seq__7983 = G__97;
chunk__8084 = G__98;
count__8185 = G__99;
i__8286 = G__100;
continue;
} else {
let temp__22496__auto__101 = cherry_core.seq.call(null, seq__7983);
if (cherry_core.truth_(temp__22496__auto__101)) {
let seq__79102 = temp__22496__auto__101;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__79102))) {
let c__22611__auto__103 = cherry_core.chunk_first.call(null, seq__79102);
let G__104 = cherry_core.chunk_rest.call(null, seq__79102);
let G__105 = c__22611__auto__103;
let G__106 = cherry_core.count.call(null, c__22611__auto__103);
let G__107 = 0;
seq__7983 = G__104;
chunk__8084 = G__105;
count__8185 = G__106;
i__8286 = G__107;
continue;
} else {
let vec__108111 = cherry_core.first.call(null, seq__79102);
let key112 = cherry_core.nth.call(null, vec__108111, 0, null);
let value113 = cherry_core.nth.call(null, vec__108111, 1, null);
let G__114115 = key112;
let G__114116 = (cherry_core.keyword_QMARK_.call(null, G__114115)) ? (cherry_core.subs.call(null, cherry_core.str.call(null, G__114115), 1)) : (null);
switch (G__114116) {case "html":
element78["innerHTML"] = value113;

break;
case "class":
element78["className"] = value113;

break;
case "style":
set_style.call(null, element78, value113)
break;
default:
element78.setAttribute(cherry_core.name.call(null, key112), value113)};
null;
let G__118 = cherry_core.next.call(null, seq__79102);
let G__119 = null;
let G__120 = 0;
let G__121 = 0;
seq__7983 = G__118;
chunk__8084 = G__119;
count__8185 = G__120;
i__8286 = G__121;
continue;
}}};break;
}
;
if (cherry_core.truth_(content)) {
set_content.call(null, element78, cherry_core.js__GT_clj.call(null, content))};
return element78;
};
f71["cljs$lang$maxFixedArity"] = 3;
return f71;
})()
;
var createStyleLink = function (path) {
return tag.call(null, "link", ({ "rel": "stylesheet", "href": path }));
}
;
var createWorker = function (path, msg_handler, err_handler) {
let worker122 = new Worker(path, ({ "type": "module" }));
if (cherry_core.truth_(msg_handler)) {
worker122.addEventListener("message", msg_handler)};
worker122.addEventListener("messageerror", function (_PERCENT_1) {
return console.log("Message can't be decoded", _PERCENT_1);
});
worker122.addEventListener("error", function (_PERCENT_1) {
return console.error("Worker error", _PERCENT_1);
});
if (cherry_core.truth_(err_handler)) {
worker122.addEventListener("error", err_handler)};
return worker122;
}
;
var tap = function (value, fun) {
fun.call(null, value);
return value;
}
;

export { hide, set_style, tag, set_content, create_element, unhide, createWorker, tap, createStyleLink, element_QMARK_, unhideAll }
