import * as cherry_core from '/js/cljs_core.js';
function set_property(object, property, value) {
        object[property] = value
      };
var set_content = function (element, content) {
if (cherry_core.truth_(cherry_core.string_QMARK_.call(null, content))) {
return element["innerText"] = content;
;} else {
if (cherry_core.truth_(content["nodeName"])) {
return element.appendChild(content);} else {
if (cherry_core.truth_(cherry_core.vector_QMARK_.call(null, content))) {
let seq__15 = cherry_core.seq.call(null, content);
let chunk__26 = null;
let count__37 = 0;
let i__48 = 0;
while(true){
if (cherry_core.truth_((i__48 < count__37))) {
let i9 = cherry_core._nth.call(null, chunk__26, i__48);
set_content.call(null, element, i9);
null;
let G__10 = seq__15;
let G__11 = chunk__26;
let G__12 = count__37;
let G__13 = cherry_core.unchecked_inc.call(null, i__48);
seq__15 = G__10;
chunk__26 = G__11;
count__37 = G__12;
i__48 = G__13;
continue;
} else {
let temp__22496__auto__14 = cherry_core.seq.call(null, seq__15);
if (cherry_core.truth_(temp__22496__auto__14)) {
let seq__115 = temp__22496__auto__14;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__115))) {
let c__22611__auto__16 = cherry_core.chunk_first.call(null, seq__115);
let G__17 = cherry_core.chunk_rest.call(null, seq__115);
let G__18 = c__22611__auto__16;
let G__19 = cherry_core.count.call(null, c__22611__auto__16);
let G__20 = 0;
seq__15 = G__17;
chunk__26 = G__18;
count__37 = G__19;
i__48 = G__20;
continue;
} else {
let i21 = cherry_core.first.call(null, seq__115);
set_content.call(null, element, i21);
null;
let G__22 = cherry_core.next.call(null, seq__115);
let G__23 = null;
let G__24 = 0;
let G__25 = 0;
seq__15 = G__22;
chunk__26 = G__23;
count__37 = G__24;
i__48 = G__25;
continue;
}}};break;
}
} else {
return null;}}}
}
;
var set_style = function (element, style) {
let seq__2630 = cherry_core.seq.call(null, style);
let chunk__2731 = null;
let count__2832 = 0;
let i__2933 = 0;
while(true){
if (cherry_core.truth_((i__2933 < count__2832))) {
let vec__3437 = cherry_core._nth.call(null, chunk__2731, i__2933);
let property38 = cherry_core.nth.call(null, vec__3437, 0, null);
let value39 = cherry_core.nth.call(null, vec__3437, 1, null);
set_property.call(null, element.style, cherry_core.name.call(null, property38), value39);
null;
let G__40 = seq__2630;
let G__41 = chunk__2731;
let G__42 = count__2832;
let G__43 = cherry_core.unchecked_inc.call(null, i__2933);
seq__2630 = G__40;
chunk__2731 = G__41;
count__2832 = G__42;
i__2933 = G__43;
continue;
} else {
let temp__22496__auto__44 = cherry_core.seq.call(null, seq__2630);
if (cherry_core.truth_(temp__22496__auto__44)) {
let seq__2645 = temp__22496__auto__44;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__2645))) {
let c__22611__auto__46 = cherry_core.chunk_first.call(null, seq__2645);
let G__47 = cherry_core.chunk_rest.call(null, seq__2645);
let G__48 = c__22611__auto__46;
let G__49 = cherry_core.count.call(null, c__22611__auto__46);
let G__50 = 0;
seq__2630 = G__47;
chunk__2731 = G__48;
count__2832 = G__49;
i__2933 = G__50;
continue;
} else {
let vec__5154 = cherry_core.first.call(null, seq__2645);
let property55 = cherry_core.nth.call(null, vec__5154, 0, null);
let value56 = cherry_core.nth.call(null, vec__5154, 1, null);
set_property.call(null, element.style, cherry_core.name.call(null, property55), value56);
null;
let G__57 = cherry_core.next.call(null, seq__2645);
let G__58 = null;
let G__59 = 0;
let G__60 = 0;
seq__2630 = G__57;
chunk__2731 = G__58;
count__2832 = G__59;
i__2933 = G__60;
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
 let f61 = function (var_args) {
let G__6465 = cherry_core.alength.call(null, arguments);
switch (G__6465) {case 1:
return f61.cljs$core$IFn$_invoke$arity$1((arguments[0]));
break;
case 2:
return f61.cljs$core$IFn$_invoke$arity$2((arguments[0]), (arguments[1]));
break;
case 3:
return f61.cljs$core$IFn$_invoke$arity$3((arguments[0]), (arguments[1]), (arguments[2]));
break;
default:
throw new Error(cherry_core.str.call(null, "Invalid arity: ", cherry_core.alength.call(null, arguments)))}
};
f61["cljs$core$IFn$_invoke$arity$1"] = function (tname) {
return tag.call(null, tname, cherry_core.array_map(), null);
};
f61["cljs$core$IFn$_invoke$arity$2"] = function (tname, arg) {
if (cherry_core.truth_(cherry_core.map_QMARK_.call(null, cherry_core.js__GT_clj.call(null, arg)))) {
return tag.call(null, tname, arg, null);} else {
return tag.call(null, tname, cherry_core.array_map(), arg);}
};
f61["cljs$core$IFn$_invoke$arity$3"] = function (tname, opts, content) {
let opts67 = cherry_core.js__GT_clj.call(null, opts, cherry_core.array_map(cherry_core.keyword("keywordize-keys"), true));
let element68 = create_element.call(null, tname, opts67);
let seq__6973 = cherry_core.seq.call(null, cherry_core.dissoc.call(null, opts67, cherry_core.keyword("is")));
let chunk__7074 = null;
let count__7175 = 0;
let i__7276 = 0;
while(true){
if (cherry_core.truth_((i__7276 < count__7175))) {
let vec__7780 = cherry_core._nth.call(null, chunk__7074, i__7276);
let key81 = cherry_core.nth.call(null, vec__7780, 0, null);
let value82 = cherry_core.nth.call(null, vec__7780, 1, null);
let G__8384 = key81;
let G__8385 = (cherry_core.keyword_QMARK_.call(null, G__8384)) ? (cherry_core.subs.call(null, cherry_core.str.call(null, G__8384), 1)) : (null);
switch (G__8385) {case "html":
element68["innerHTML"] = value82;

break;
case "class":
element68["className"] = value82;

break;
case "style":
set_style.call(null, element68, value82)
break;
default:
element68.setAttribute(cherry_core.name.call(null, key81), value82)};
null;
let G__87 = seq__6973;
let G__88 = chunk__7074;
let G__89 = count__7175;
let G__90 = cherry_core.unchecked_inc.call(null, i__7276);
seq__6973 = G__87;
chunk__7074 = G__88;
count__7175 = G__89;
i__7276 = G__90;
continue;
} else {
let temp__22496__auto__91 = cherry_core.seq.call(null, seq__6973);
if (cherry_core.truth_(temp__22496__auto__91)) {
let seq__6992 = temp__22496__auto__91;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__6992))) {
let c__22611__auto__93 = cherry_core.chunk_first.call(null, seq__6992);
let G__94 = cherry_core.chunk_rest.call(null, seq__6992);
let G__95 = c__22611__auto__93;
let G__96 = cherry_core.count.call(null, c__22611__auto__93);
let G__97 = 0;
seq__6973 = G__94;
chunk__7074 = G__95;
count__7175 = G__96;
i__7276 = G__97;
continue;
} else {
let vec__98101 = cherry_core.first.call(null, seq__6992);
let key102 = cherry_core.nth.call(null, vec__98101, 0, null);
let value103 = cherry_core.nth.call(null, vec__98101, 1, null);
let G__104105 = key102;
let G__104106 = (cherry_core.keyword_QMARK_.call(null, G__104105)) ? (cherry_core.subs.call(null, cherry_core.str.call(null, G__104105), 1)) : (null);
switch (G__104106) {case "html":
element68["innerHTML"] = value103;

break;
case "class":
element68["className"] = value103;

break;
case "style":
set_style.call(null, element68, value103)
break;
default:
element68.setAttribute(cherry_core.name.call(null, key102), value103)};
null;
let G__108 = cherry_core.next.call(null, seq__6992);
let G__109 = null;
let G__110 = 0;
let G__111 = 0;
seq__6973 = G__108;
chunk__7074 = G__109;
count__7175 = G__110;
i__7276 = G__111;
continue;
}}};break;
}
;
if (cherry_core.truth_(content)) {
set_content.call(null, element68, cherry_core.js__GT_clj.call(null, content))};
return element68;
};
f61["cljs$lang$maxFixedArity"] = 3;
return f61;
})()
;
var createStyleLink = function (path) {
return tag.call(null, "link", ({ "rel": "stylesheet", "href": path }));
}
;
var createWorker = function (path, msg_handler, err_handler) {
let worker112 = new Worker(path, ({ "type": "module" }));
if (cherry_core.truth_(msg_handler)) {
worker112.addEventListener("message", msg_handler)};
worker112.addEventListener("messageerror", function (_PERCENT_1) {
return console.log("Message can't be decoded", _PERCENT_1);
});
worker112.addEventListener("error", function (_PERCENT_1) {
return console.error("Worker error", _PERCENT_1);
});
if (cherry_core.truth_(err_handler)) {
worker112.addEventListener("error", err_handler)};
return worker112;
}
;
var tap = function (value, fun) {
fun.call(null, value);
return value;
}
;

export { set_content, set_style, create_element, tag, createStyleLink, createWorker, tap }
