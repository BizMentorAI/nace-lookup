import * as cherry_core from '/js/cljs_core.js';
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
var create_element = function (name, opts) {
if (cherry_core.truth_(cherry_core.keyword("is").call(null, opts))) {
return document.createElement(name, ({ "is": cherry_core.keyword("is").call(null, opts) }));} else {
return document.createElement(name);}
}
;
var tag = (function () {
 let f26 = function (var_args) {
let G__2930 = cherry_core.alength.call(null, arguments);
switch (G__2930) {case 1:
return f26.cljs$core$IFn$_invoke$arity$1((arguments[0]));
break;
case 2:
return f26.cljs$core$IFn$_invoke$arity$2((arguments[0]), (arguments[1]));
break;
case 3:
return f26.cljs$core$IFn$_invoke$arity$3((arguments[0]), (arguments[1]), (arguments[2]));
break;
default:
throw new Error(cherry_core.str.call(null, "Invalid arity: ", cherry_core.alength.call(null, arguments)))}
};
f26["cljs$core$IFn$_invoke$arity$1"] = function (tname) {
return tag.call(null, tname, cherry_core.array_map(), null);
};
f26["cljs$core$IFn$_invoke$arity$2"] = function (tname, arg) {
if (cherry_core.truth_(cherry_core.map_QMARK_.call(null, cherry_core.js__GT_clj.call(null, arg)))) {
return tag.call(null, tname, arg, null);} else {
return tag.call(null, tname, cherry_core.array_map(), arg);}
};
f26["cljs$core$IFn$_invoke$arity$3"] = function (tname, opts, content) {
let opts32 = cherry_core.js__GT_clj.call(null, opts, cherry_core.array_map(cherry_core.keyword("keywordize-keys"), true));
let element33 = create_element.call(null, tname, opts32);
let seq__3438 = cherry_core.seq.call(null, cherry_core.dissoc.call(null, opts32, cherry_core.keyword("is")));
let chunk__3539 = null;
let count__3640 = 0;
let i__3741 = 0;
while(true){
if (cherry_core.truth_((i__3741 < count__3640))) {
let vec__4245 = cherry_core._nth.call(null, chunk__3539, i__3741);
let key46 = cherry_core.nth.call(null, vec__4245, 0, null);
let value47 = cherry_core.nth.call(null, vec__4245, 1, null);
let G__4849 = key46;
switch (G__4849) {case "html":
element33["innerHTML"] = value47;

break;
case "class":
element33["className"] = value47;

break;
default:
element33.setAttribute(cherry_core.name.call(null, key46), value47)};
null;
let G__51 = seq__3438;
let G__52 = chunk__3539;
let G__53 = count__3640;
let G__54 = cherry_core.unchecked_inc.call(null, i__3741);
seq__3438 = G__51;
chunk__3539 = G__52;
count__3640 = G__53;
i__3741 = G__54;
continue;
} else {
let temp__22496__auto__55 = cherry_core.seq.call(null, seq__3438);
if (cherry_core.truth_(temp__22496__auto__55)) {
let seq__3456 = temp__22496__auto__55;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__3456))) {
let c__22611__auto__57 = cherry_core.chunk_first.call(null, seq__3456);
let G__58 = cherry_core.chunk_rest.call(null, seq__3456);
let G__59 = c__22611__auto__57;
let G__60 = cherry_core.count.call(null, c__22611__auto__57);
let G__61 = 0;
seq__3438 = G__58;
chunk__3539 = G__59;
count__3640 = G__60;
i__3741 = G__61;
continue;
} else {
let vec__6265 = cherry_core.first.call(null, seq__3456);
let key66 = cherry_core.nth.call(null, vec__6265, 0, null);
let value67 = cherry_core.nth.call(null, vec__6265, 1, null);
let G__6869 = key66;
switch (G__6869) {case "html":
element33["innerHTML"] = value67;

break;
case "class":
element33["className"] = value67;

break;
default:
element33.setAttribute(cherry_core.name.call(null, key66), value67)};
null;
let G__71 = cherry_core.next.call(null, seq__3456);
let G__72 = null;
let G__73 = 0;
let G__74 = 0;
seq__3438 = G__71;
chunk__3539 = G__72;
count__3640 = G__73;
i__3741 = G__74;
continue;
}}};break;
}
;
if (cherry_core.truth_(content)) {
set_content.call(null, element33, cherry_core.js__GT_clj.call(null, content))};
return element33;
};
f26["cljs$lang$maxFixedArity"] = 3;
return f26;
})()
;
var createStyleLink = function (path) {
return tag.call(null, "link", ({ "rel": "stylesheet", "href": path }));
}
;
var createWorker = function (path, msg_handler, err_handler) {
let worker75 = new Worker(path, ({ "type": "module" }));
worker75.addEventListener("message", msg_handler);
worker75.addEventListener("messageerror", function (_PERCENT_1) {
return console.log("Message can't be decoded", _PERCENT_1);
});
worker75.addEventListener("error", function (_PERCENT_1) {
return console.error("Worker error", _PERCENT_1);
});
worker75.addEventListener("error", err_handler);
return worker75;
}
;
var tap = function (value, fun) {
fun.call(null, value);
return value;
}
;

export { set_content, create_element, tag, createStyleLink, createWorker, tap }
