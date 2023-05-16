import * as cherry_core from '/js/cljs_core.js';
var fetch_data = async function () {
let response1 = (await fetch("/workers/autocomplete/data.json"));
let body2 = (await response1.json());
return body2;
}
;
var data = (await fetch_data.call(null))
;
var match_l6_item = function (fields, regexp) {
return cherry_core.first.call(null, cherry_core.filter.call(null, function (_PERCENT_1) {
return _PERCENT_1.match(regexp);
}, fields));
}
;
var data_transducer = function (search_term, xf) {
let regexp3 = new RegExp(cherry_core.str.call(null, "\\b", search_term), "i");
let f4 = function (var_args) {
let G__78 = cherry_core.alength.call(null, arguments);
switch (G__78) {case 0:
return f4.cljs$core$IFn$_invoke$arity$0();
break;
case 2:
return f4.cljs$core$IFn$_invoke$arity$2((arguments[0]), (arguments[1]));
break;
case 1:
return f4.cljs$core$IFn$_invoke$arity$1((arguments[0]));
break;
default:
throw new Error(cherry_core.str.call(null, "Invalid arity: ", cherry_core.alength.call(null, arguments)))}
};
f4["cljs$core$IFn$_invoke$arity$0"] = function () {
return xf.call(null);
};
f4["cljs$core$IFn$_invoke$arity$2"] = function (acc, input) {
let seq__1014 = cherry_core.seq.call(null, cherry_core.keyword("items").call(null, input));
let chunk__1115 = null;
let count__1216 = 0;
let i__1317 = 0;
while(true){
if (cherry_core.truth_((i__1317 < count__1216))) {
let l4_item18 = cherry_core._nth.call(null, chunk__1115, i__1317);
xf.call(null, acc, l4_item18);
let seq__1923 = cherry_core.seq.call(null, cherry_core.keyword("items").call(null, l4_item18));
let chunk__2024 = null;
let count__2125 = 0;
let i__2226 = 0;
while(true){
if (cherry_core.truth_((i__2226 < count__2125))) {
let l6_item27 = cherry_core._nth.call(null, chunk__2024, i__2226);
if (cherry_core.truth_(match_l6_item.call(null, cherry_core.vector(cherry_core.keyword("label").call(null, l6_item27), cherry_core.keyword("extra").call(null, l6_item27)), regexp3))) {
xf.call(null, acc, l6_item27)};
null;
let G__28 = seq__1923;
let G__29 = chunk__2024;
let G__30 = count__2125;
let G__31 = cherry_core.unchecked_inc.call(null, i__2226);
seq__1923 = G__28;
chunk__2024 = G__29;
count__2125 = G__30;
i__2226 = G__31;
continue;
} else {
let temp__22496__auto__32 = cherry_core.seq.call(null, seq__1923);
if (cherry_core.truth_(temp__22496__auto__32)) {
let seq__1933 = temp__22496__auto__32;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__1933))) {
let c__22611__auto__34 = cherry_core.chunk_first.call(null, seq__1933);
let G__35 = cherry_core.chunk_rest.call(null, seq__1933);
let G__36 = c__22611__auto__34;
let G__37 = cherry_core.count.call(null, c__22611__auto__34);
let G__38 = 0;
seq__1923 = G__35;
chunk__2024 = G__36;
count__2125 = G__37;
i__2226 = G__38;
continue;
} else {
let l6_item39 = cherry_core.first.call(null, seq__1933);
if (cherry_core.truth_(match_l6_item.call(null, cherry_core.vector(cherry_core.keyword("label").call(null, l6_item39), cherry_core.keyword("extra").call(null, l6_item39)), regexp3))) {
xf.call(null, acc, l6_item39)};
null;
let G__40 = cherry_core.next.call(null, seq__1933);
let G__41 = null;
let G__42 = 0;
let G__43 = 0;
seq__1923 = G__40;
chunk__2024 = G__41;
count__2125 = G__42;
i__2226 = G__43;
continue;
}}};break;
}
;
null;
let G__44 = seq__1014;
let G__45 = chunk__1115;
let G__46 = count__1216;
let G__47 = cherry_core.unchecked_inc.call(null, i__1317);
seq__1014 = G__44;
chunk__1115 = G__45;
count__1216 = G__46;
i__1317 = G__47;
continue;
} else {
let temp__22496__auto__48 = cherry_core.seq.call(null, seq__1014);
if (cherry_core.truth_(temp__22496__auto__48)) {
let seq__1049 = temp__22496__auto__48;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__1049))) {
let c__22611__auto__50 = cherry_core.chunk_first.call(null, seq__1049);
let G__51 = cherry_core.chunk_rest.call(null, seq__1049);
let G__52 = c__22611__auto__50;
let G__53 = cherry_core.count.call(null, c__22611__auto__50);
let G__54 = 0;
seq__1014 = G__51;
chunk__1115 = G__52;
count__1216 = G__53;
i__1317 = G__54;
continue;
} else {
let l4_item55 = cherry_core.first.call(null, seq__1049);
xf.call(null, acc, l4_item55);
let seq__5660 = cherry_core.seq.call(null, cherry_core.keyword("items").call(null, l4_item55));
let chunk__5761 = null;
let count__5862 = 0;
let i__5963 = 0;
while(true){
if (cherry_core.truth_((i__5963 < count__5862))) {
let l6_item64 = cherry_core._nth.call(null, chunk__5761, i__5963);
if (cherry_core.truth_(match_l6_item.call(null, cherry_core.vector(cherry_core.keyword("label").call(null, l6_item64), cherry_core.keyword("extra").call(null, l6_item64)), regexp3))) {
xf.call(null, acc, l6_item64)};
null;
let G__65 = seq__5660;
let G__66 = chunk__5761;
let G__67 = count__5862;
let G__68 = cherry_core.unchecked_inc.call(null, i__5963);
seq__5660 = G__65;
chunk__5761 = G__66;
count__5862 = G__67;
i__5963 = G__68;
continue;
} else {
let temp__22496__auto__69 = cherry_core.seq.call(null, seq__5660);
if (cherry_core.truth_(temp__22496__auto__69)) {
let seq__5670 = temp__22496__auto__69;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__5670))) {
let c__22611__auto__71 = cherry_core.chunk_first.call(null, seq__5670);
let G__72 = cherry_core.chunk_rest.call(null, seq__5670);
let G__73 = c__22611__auto__71;
let G__74 = cherry_core.count.call(null, c__22611__auto__71);
let G__75 = 0;
seq__5660 = G__72;
chunk__5761 = G__73;
count__5862 = G__74;
i__5963 = G__75;
continue;
} else {
let l6_item76 = cherry_core.first.call(null, seq__5670);
if (cherry_core.truth_(match_l6_item.call(null, cherry_core.vector(cherry_core.keyword("label").call(null, l6_item76), cherry_core.keyword("extra").call(null, l6_item76)), regexp3))) {
xf.call(null, acc, l6_item76)};
null;
let G__77 = cherry_core.next.call(null, seq__5670);
let G__78 = null;
let G__79 = 0;
let G__80 = 0;
seq__5660 = G__77;
chunk__5761 = G__78;
count__5862 = G__79;
i__5963 = G__80;
continue;
}}};break;
}
;
null;
let G__81 = cherry_core.next.call(null, seq__1049);
let G__82 = null;
let G__83 = 0;
let G__84 = 0;
seq__1014 = G__81;
chunk__1115 = G__82;
count__1216 = G__83;
i__1317 = G__84;
continue;
}}};break;
}
;
return acc;
};
f4["cljs$core$IFn$_invoke$arity$1"] = function (result) {
return result;
};
f4["cljs$lang$maxFixedArity"] = 2;
return f4;
}
;
var filter_items = function (data, search_term) {
return cherry_core.into.call(null, cherry_core.vector(), cherry_core.partial.call(null, data_transducer, search_term), cherry_core.js__GT_clj.call(null, data, cherry_core.array_map(cherry_core.keyword("keywordize-keys"), true)));
}
;
var handle_message = function (event) {
let search_term85 = event.data;
console.log("Worker received:", ({ "term": search_term85 }));
let result86 = filter_items.call(null, data, search_term85);
console.log("CLJ", result86);
console.log("JS", cherry_core.clj__GT_js.call(null, result86));
return postMessage(result86);
}
;
onmessage = handle_message;

export { fetch_data, data, match_l6_item, data_transducer, filter_items, handle_message }
