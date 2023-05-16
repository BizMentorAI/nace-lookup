import * as cherry_core from '/js/cljs_core.js';
var fetch_data = async function () {
let response1 = (await fetch("/workers/autocomplete/data.json"));
let body2 = (await response1.json());
return body2;
}
;
var data = cherry_core.js__GT_clj.call(null, (await fetch_data.call(null)), cherry_core.array_map(cherry_core.keyword("keywordize-keys"), true))
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
f4["cljs$core$IFn$_invoke$arity$2"] = function (acc, l1_item) {
let seq__1014 = cherry_core.seq.call(null, cherry_core.keyword("items").call(null, l1_item));
let chunk__1115 = null;
let count__1216 = 0;
let i__1317 = 0;
while(true){
if (cherry_core.truth_((i__1317 < count__1216))) {
let l4_item18 = cherry_core._nth.call(null, chunk__1115, i__1317);
let filtered_l6_items19 = cherry_core.filter.call(null, function (l6_item) {
if (cherry_core.truth_(match_l6_item.call(null, cherry_core.vector(cherry_core.keyword("label").call(null, l6_item), cherry_core.keyword("extra").call(null, l6_item)), regexp3))) {
return xf.call(null, acc, l6_item);}
}, cherry_core.keyword("items").call(null, l4_item18));
if (cherry_core.truth_(cherry_core.empty_QMARK_.call(null, filtered_l6_items19))) {
null} else {
xf.call(null, acc, cherry_core.dissoc.call(null, l1_item, cherry_core.keyword("items")));
let seq__2024 = cherry_core.seq.call(null, filtered_l6_items19);
let chunk__2125 = null;
let count__2226 = 0;
let i__2327 = 0;
while(true){
if (cherry_core.truth_((i__2327 < count__2226))) {
let l6_item28 = cherry_core._nth.call(null, chunk__2125, i__2327);
xf.call(null, acc, cherry_core.assoc.call(null, l6_item28, cherry_core.keyword("l4"), l4_item18.call(null, cherry_core.keyword("label"))));
null;
let G__29 = seq__2024;
let G__30 = chunk__2125;
let G__31 = count__2226;
let G__32 = cherry_core.unchecked_inc.call(null, i__2327);
seq__2024 = G__29;
chunk__2125 = G__30;
count__2226 = G__31;
i__2327 = G__32;
continue;
} else {
let temp__22496__auto__33 = cherry_core.seq.call(null, seq__2024);
if (cherry_core.truth_(temp__22496__auto__33)) {
let seq__2034 = temp__22496__auto__33;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__2034))) {
let c__22611__auto__35 = cherry_core.chunk_first.call(null, seq__2034);
let G__36 = cherry_core.chunk_rest.call(null, seq__2034);
let G__37 = c__22611__auto__35;
let G__38 = cherry_core.count.call(null, c__22611__auto__35);
let G__39 = 0;
seq__2024 = G__36;
chunk__2125 = G__37;
count__2226 = G__38;
i__2327 = G__39;
continue;
} else {
let l6_item40 = cherry_core.first.call(null, seq__2034);
xf.call(null, acc, cherry_core.assoc.call(null, l6_item40, cherry_core.keyword("l4"), l4_item18.call(null, cherry_core.keyword("label"))));
null;
let G__41 = cherry_core.next.call(null, seq__2034);
let G__42 = null;
let G__43 = 0;
let G__44 = 0;
seq__2024 = G__41;
chunk__2125 = G__42;
count__2226 = G__43;
i__2327 = G__44;
continue;
}}};break;
}
};
null;
let G__45 = seq__1014;
let G__46 = chunk__1115;
let G__47 = count__1216;
let G__48 = cherry_core.unchecked_inc.call(null, i__1317);
seq__1014 = G__45;
chunk__1115 = G__46;
count__1216 = G__47;
i__1317 = G__48;
continue;
} else {
let temp__22496__auto__49 = cherry_core.seq.call(null, seq__1014);
if (cherry_core.truth_(temp__22496__auto__49)) {
let seq__1050 = temp__22496__auto__49;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__1050))) {
let c__22611__auto__51 = cherry_core.chunk_first.call(null, seq__1050);
let G__52 = cherry_core.chunk_rest.call(null, seq__1050);
let G__53 = c__22611__auto__51;
let G__54 = cherry_core.count.call(null, c__22611__auto__51);
let G__55 = 0;
seq__1014 = G__52;
chunk__1115 = G__53;
count__1216 = G__54;
i__1317 = G__55;
continue;
} else {
let l4_item56 = cherry_core.first.call(null, seq__1050);
let filtered_l6_items57 = cherry_core.filter.call(null, function (l6_item) {
if (cherry_core.truth_(match_l6_item.call(null, cherry_core.vector(cherry_core.keyword("label").call(null, l6_item), cherry_core.keyword("extra").call(null, l6_item)), regexp3))) {
return xf.call(null, acc, l6_item);}
}, cherry_core.keyword("items").call(null, l4_item56));
if (cherry_core.truth_(cherry_core.empty_QMARK_.call(null, filtered_l6_items57))) {
null} else {
xf.call(null, acc, cherry_core.dissoc.call(null, l1_item, cherry_core.keyword("items")));
let seq__5862 = cherry_core.seq.call(null, filtered_l6_items57);
let chunk__5963 = null;
let count__6064 = 0;
let i__6165 = 0;
while(true){
if (cherry_core.truth_((i__6165 < count__6064))) {
let l6_item66 = cherry_core._nth.call(null, chunk__5963, i__6165);
xf.call(null, acc, cherry_core.assoc.call(null, l6_item66, cherry_core.keyword("l4"), l4_item56.call(null, cherry_core.keyword("label"))));
null;
let G__67 = seq__5862;
let G__68 = chunk__5963;
let G__69 = count__6064;
let G__70 = cherry_core.unchecked_inc.call(null, i__6165);
seq__5862 = G__67;
chunk__5963 = G__68;
count__6064 = G__69;
i__6165 = G__70;
continue;
} else {
let temp__22496__auto__71 = cherry_core.seq.call(null, seq__5862);
if (cherry_core.truth_(temp__22496__auto__71)) {
let seq__5872 = temp__22496__auto__71;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__5872))) {
let c__22611__auto__73 = cherry_core.chunk_first.call(null, seq__5872);
let G__74 = cherry_core.chunk_rest.call(null, seq__5872);
let G__75 = c__22611__auto__73;
let G__76 = cherry_core.count.call(null, c__22611__auto__73);
let G__77 = 0;
seq__5862 = G__74;
chunk__5963 = G__75;
count__6064 = G__76;
i__6165 = G__77;
continue;
} else {
let l6_item78 = cherry_core.first.call(null, seq__5872);
xf.call(null, acc, cherry_core.assoc.call(null, l6_item78, cherry_core.keyword("l4"), l4_item56.call(null, cherry_core.keyword("label"))));
null;
let G__79 = cherry_core.next.call(null, seq__5872);
let G__80 = null;
let G__81 = 0;
let G__82 = 0;
seq__5862 = G__79;
chunk__5963 = G__80;
count__6064 = G__81;
i__6165 = G__82;
continue;
}}};break;
}
};
null;
let G__83 = cherry_core.next.call(null, seq__1050);
let G__84 = null;
let G__85 = 0;
let G__86 = 0;
seq__1014 = G__83;
chunk__1115 = G__84;
count__1216 = G__85;
i__1317 = G__86;
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
return cherry_core.persistent_BANG_.call(null, cherry_core.into.call(null, cherry_core.vector(), cherry_core.partial.call(null, data_transducer, search_term), data));
}
;
var handle_message = function (event) {
let search_term87 = event.data;
console.log("Worker received:", ({ "term": search_term87 }));
let result88 = filter_items.call(null, data, search_term87);
console.log("Worker returned:", cherry_core.clj__GT_js.call(null, result88));
return postMessage(cherry_core.clj__GT_js.call(null, result88));
}
;
onmessage = handle_message;

export { fetch_data, data, match_l6_item, data_transducer, filter_items, handle_message }
