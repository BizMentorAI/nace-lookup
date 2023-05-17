import * as cherry_core from '/js/cljs_core.js';
function set_property(object, property, value) {
        object[property] = value
      };
var showBlock = (function () {
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
let seq__1014 = cherry_core.seq.call(null, cherry_core.remove.call(null, function (_PERCENT_1) {
return cherry_core.not.call(null, _PERCENT_1["nodeName"]);
}, args));
let chunk__1115 = null;
let count__1216 = 0;
let i__1317 = 0;
while(true){
if (cherry_core.truth_((i__1317 < count__1216))) {
let element18 = cherry_core._nth.call(null, chunk__1115, i__1317);
set_property.call(null, element18.style, "display", "block");
null;
let G__19 = seq__1014;
let G__20 = chunk__1115;
let G__21 = count__1216;
let G__22 = cherry_core.unchecked_inc.call(null, i__1317);
seq__1014 = G__19;
chunk__1115 = G__20;
count__1216 = G__21;
i__1317 = G__22;
continue;
} else {
let temp__22496__auto__23 = cherry_core.seq.call(null, seq__1014);
if (cherry_core.truth_(temp__22496__auto__23)) {
let seq__1024 = temp__22496__auto__23;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__1024))) {
let c__22611__auto__25 = cherry_core.chunk_first.call(null, seq__1024);
let G__26 = cherry_core.chunk_rest.call(null, seq__1024);
let G__27 = c__22611__auto__25;
let G__28 = cherry_core.count.call(null, c__22611__auto__25);
let G__29 = 0;
seq__1014 = G__26;
chunk__1115 = G__27;
count__1216 = G__28;
i__1317 = G__29;
continue;
} else {
let element30 = cherry_core.first.call(null, seq__1024);
set_property.call(null, element30.style, "display", "block");
null;
let G__31 = cherry_core.next.call(null, seq__1024);
let G__32 = null;
let G__33 = 0;
let G__34 = 0;
seq__1014 = G__31;
chunk__1115 = G__32;
count__1216 = G__33;
i__1317 = G__34;
continue;
}}};break;
}

};
f1["cljs$lang$maxFixedArity"] = 0;
f1["cljs$lang$applyTo"] = function (seq4) {
let self__22127__auto__35 = this;
return self__22127__auto__35.cljs$core$IFn$_invoke$arity$variadic(cherry_core.seq.call(null, seq4));
};
return f1;
})()
;
var set_content = function (element, content) {
if (cherry_core.truth_(cherry_core.string_QMARK_.call(null, content))) {
return element["innerText"] = content;
;} else {
if (cherry_core.truth_(content["nodeName"])) {
return element.appendChild(content);} else {
if (cherry_core.truth_(cherry_core.vector_QMARK_.call(null, content))) {
let seq__3640 = cherry_core.seq.call(null, content);
let chunk__3741 = null;
let count__3842 = 0;
let i__3943 = 0;
while(true){
if (cherry_core.truth_((i__3943 < count__3842))) {
let i44 = cherry_core._nth.call(null, chunk__3741, i__3943);
set_content.call(null, element, i44);
null;
let G__45 = seq__3640;
let G__46 = chunk__3741;
let G__47 = count__3842;
let G__48 = cherry_core.unchecked_inc.call(null, i__3943);
seq__3640 = G__45;
chunk__3741 = G__46;
count__3842 = G__47;
i__3943 = G__48;
continue;
} else {
let temp__22496__auto__49 = cherry_core.seq.call(null, seq__3640);
if (cherry_core.truth_(temp__22496__auto__49)) {
let seq__3650 = temp__22496__auto__49;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__3650))) {
let c__22611__auto__51 = cherry_core.chunk_first.call(null, seq__3650);
let G__52 = cherry_core.chunk_rest.call(null, seq__3650);
let G__53 = c__22611__auto__51;
let G__54 = cherry_core.count.call(null, c__22611__auto__51);
let G__55 = 0;
seq__3640 = G__52;
chunk__3741 = G__53;
count__3842 = G__54;
i__3943 = G__55;
continue;
} else {
let i56 = cherry_core.first.call(null, seq__3650);
set_content.call(null, element, i56);
null;
let G__57 = cherry_core.next.call(null, seq__3650);
let G__58 = null;
let G__59 = 0;
let G__60 = 0;
seq__3640 = G__57;
chunk__3741 = G__58;
count__3842 = G__59;
i__3943 = G__60;
continue;
}}};break;
}
} else {
return null;}}}
}
;
var set_style = function (element, style) {
let seq__6165 = cherry_core.seq.call(null, style);
let chunk__6266 = null;
let count__6367 = 0;
let i__6468 = 0;
while(true){
if (cherry_core.truth_((i__6468 < count__6367))) {
let vec__6972 = cherry_core._nth.call(null, chunk__6266, i__6468);
let property73 = cherry_core.nth.call(null, vec__6972, 0, null);
let value74 = cherry_core.nth.call(null, vec__6972, 1, null);
set_property.call(null, element.style, cherry_core.name.call(null, property73), value74);
null;
let G__75 = seq__6165;
let G__76 = chunk__6266;
let G__77 = count__6367;
let G__78 = cherry_core.unchecked_inc.call(null, i__6468);
seq__6165 = G__75;
chunk__6266 = G__76;
count__6367 = G__77;
i__6468 = G__78;
continue;
} else {
let temp__22496__auto__79 = cherry_core.seq.call(null, seq__6165);
if (cherry_core.truth_(temp__22496__auto__79)) {
let seq__6180 = temp__22496__auto__79;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__6180))) {
let c__22611__auto__81 = cherry_core.chunk_first.call(null, seq__6180);
let G__82 = cherry_core.chunk_rest.call(null, seq__6180);
let G__83 = c__22611__auto__81;
let G__84 = cherry_core.count.call(null, c__22611__auto__81);
let G__85 = 0;
seq__6165 = G__82;
chunk__6266 = G__83;
count__6367 = G__84;
i__6468 = G__85;
continue;
} else {
let vec__8689 = cherry_core.first.call(null, seq__6180);
let property90 = cherry_core.nth.call(null, vec__8689, 0, null);
let value91 = cherry_core.nth.call(null, vec__8689, 1, null);
set_property.call(null, element.style, cherry_core.name.call(null, property90), value91);
null;
let G__92 = cherry_core.next.call(null, seq__6180);
let G__93 = null;
let G__94 = 0;
let G__95 = 0;
seq__6165 = G__92;
chunk__6266 = G__93;
count__6367 = G__94;
i__6468 = G__95;
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
 let f96 = function (var_args) {
let G__99100 = cherry_core.alength.call(null, arguments);
switch (G__99100) {case 1:
return f96.cljs$core$IFn$_invoke$arity$1((arguments[0]));
break;
case 2:
return f96.cljs$core$IFn$_invoke$arity$2((arguments[0]), (arguments[1]));
break;
case 3:
return f96.cljs$core$IFn$_invoke$arity$3((arguments[0]), (arguments[1]), (arguments[2]));
break;
default:
throw new Error(cherry_core.str.call(null, "Invalid arity: ", cherry_core.alength.call(null, arguments)))}
};
f96["cljs$core$IFn$_invoke$arity$1"] = function (tname) {
return tag.call(null, tname, cherry_core.array_map(), null);
};
f96["cljs$core$IFn$_invoke$arity$2"] = function (tname, arg) {
if (cherry_core.truth_(cherry_core.map_QMARK_.call(null, cherry_core.js__GT_clj.call(null, arg)))) {
return tag.call(null, tname, arg, null);} else {
return tag.call(null, tname, cherry_core.array_map(), arg);}
};
f96["cljs$core$IFn$_invoke$arity$3"] = function (tname, opts, content) {
let opts102 = cherry_core.js__GT_clj.call(null, opts, cherry_core.array_map(cherry_core.keyword("keywordize-keys"), true));
let element103 = create_element.call(null, tname, opts102);
let seq__104108 = cherry_core.seq.call(null, cherry_core.dissoc.call(null, opts102, cherry_core.keyword("is")));
let chunk__105109 = null;
let count__106110 = 0;
let i__107111 = 0;
while(true){
if (cherry_core.truth_((i__107111 < count__106110))) {
let vec__112115 = cherry_core._nth.call(null, chunk__105109, i__107111);
let key116 = cherry_core.nth.call(null, vec__112115, 0, null);
let value117 = cherry_core.nth.call(null, vec__112115, 1, null);
let G__118119 = key116;
let G__118120 = (cherry_core.keyword_QMARK_.call(null, G__118119)) ? (cherry_core.subs.call(null, cherry_core.str.call(null, G__118119), 1)) : (null);
switch (G__118120) {case "html":
element103["innerHTML"] = value117;

break;
case "class":
element103["className"] = value117;

break;
case "style":
set_style.call(null, element103, value117)
break;
default:
element103.setAttribute(cherry_core.name.call(null, key116), value117)};
null;
let G__122 = seq__104108;
let G__123 = chunk__105109;
let G__124 = count__106110;
let G__125 = cherry_core.unchecked_inc.call(null, i__107111);
seq__104108 = G__122;
chunk__105109 = G__123;
count__106110 = G__124;
i__107111 = G__125;
continue;
} else {
let temp__22496__auto__126 = cherry_core.seq.call(null, seq__104108);
if (cherry_core.truth_(temp__22496__auto__126)) {
let seq__104127 = temp__22496__auto__126;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__104127))) {
let c__22611__auto__128 = cherry_core.chunk_first.call(null, seq__104127);
let G__129 = cherry_core.chunk_rest.call(null, seq__104127);
let G__130 = c__22611__auto__128;
let G__131 = cherry_core.count.call(null, c__22611__auto__128);
let G__132 = 0;
seq__104108 = G__129;
chunk__105109 = G__130;
count__106110 = G__131;
i__107111 = G__132;
continue;
} else {
let vec__133136 = cherry_core.first.call(null, seq__104127);
let key137 = cherry_core.nth.call(null, vec__133136, 0, null);
let value138 = cherry_core.nth.call(null, vec__133136, 1, null);
let G__139140 = key137;
let G__139141 = (cherry_core.keyword_QMARK_.call(null, G__139140)) ? (cherry_core.subs.call(null, cherry_core.str.call(null, G__139140), 1)) : (null);
switch (G__139141) {case "html":
element103["innerHTML"] = value138;

break;
case "class":
element103["className"] = value138;

break;
case "style":
set_style.call(null, element103, value138)
break;
default:
element103.setAttribute(cherry_core.name.call(null, key137), value138)};
null;
let G__143 = cherry_core.next.call(null, seq__104127);
let G__144 = null;
let G__145 = 0;
let G__146 = 0;
seq__104108 = G__143;
chunk__105109 = G__144;
count__106110 = G__145;
i__107111 = G__146;
continue;
}}};break;
}
;
if (cherry_core.truth_(content)) {
set_content.call(null, element103, cherry_core.js__GT_clj.call(null, content))};
return element103;
};
f96["cljs$lang$maxFixedArity"] = 3;
return f96;
})()
;
var createStyleLink = function (path) {
return tag.call(null, "link", ({ "rel": "stylesheet", "href": path }));
}
;
var createWorker = function (path, msg_handler, err_handler) {
let worker147 = new Worker(path, ({ "type": "module" }));
if (cherry_core.truth_(msg_handler)) {
worker147.addEventListener("message", msg_handler)};
worker147.addEventListener("messageerror", function (_PERCENT_1) {
return console.log("Message can't be decoded", _PERCENT_1);
});
worker147.addEventListener("error", function (_PERCENT_1) {
return console.error("Worker error", _PERCENT_1);
});
if (cherry_core.truth_(err_handler)) {
worker147.addEventListener("error", err_handler)};
return worker147;
}
;
var tap = function (value, fun) {
fun.call(null, value);
return value;
}
;

export { showBlock, set_content, set_style, create_element, tag, createStyleLink, createWorker, tap }
