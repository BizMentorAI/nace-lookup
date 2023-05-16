import * as cherry_core from '/js/cljs_core.js';
var fetch_data = async function () {
let response1 = (await fetch("/workers/autocomplete/data.json"));
let body2 = (await response1.json());
return body2;
}
;
var data = cherry_core.js__GT_clj.call(null, (await fetch_data.call(null)), cherry_core.array_map(cherry_core.keyword("keywordize-keys"), true))
;
var match_l6_item = function (item, regexp) {
let fields3 = cherry_core.vector(cherry_core.keyword("label").call(null, item), cherry_core.keyword("extra").call(null, item));
return cherry_core.first.call(null, cherry_core.filter.call(null, function (_PERCENT_1) {
return _PERCENT_1.match(regexp);
}, fields3));
}
;
var data_transducer = function (search_term, xf) {
let regexp4 = new RegExp(cherry_core.str.call(null, "\\b", search_term), "i");
let f5 = function (var_args) {
let G__89 = cherry_core.alength.call(null, arguments);
switch (G__89) {case 0:
return f5.cljs$core$IFn$_invoke$arity$0();
break;
case 2:
return f5.cljs$core$IFn$_invoke$arity$2((arguments[0]), (arguments[1]));
break;
case 1:
return f5.cljs$core$IFn$_invoke$arity$1((arguments[0]));
break;
default:
throw new Error(cherry_core.str.call(null, "Invalid arity: ", cherry_core.alength.call(null, arguments)))}
};
f5["cljs$core$IFn$_invoke$arity$0"] = function () {
return xf.call(null);
};
f5["cljs$core$IFn$_invoke$arity$2"] = function (acc, l1_item) {
let filtered_l6_items11 = cherry_core.reduce.call(null, function (acc, l4_item) {
let filtered_l6_items12 = cherry_core.filter.call(null, function (l6_item) {
return match_l6_item.call(null, l6_item, regexp4);
}, cherry_core.keyword("items").call(null, l4_item));
let extended_l6_items13 = cherry_core.map.call(null, function (l6_item) {
return cherry_core.assoc.call(null, l6_item, cherry_core.keyword("l4Item"), cherry_core.array_map(cherry_core.keyword("code"), cherry_core.keyword("code").call(null, l4_item), cherry_core.keyword("label"), cherry_core.keyword("label").call(null, l4_item)));
}, filtered_l6_items12);
if (cherry_core.truth_(cherry_core.not.call(null, cherry_core.empty_QMARK_.call(null, extended_l6_items13)))) {
return cherry_core.apply.call(null, cherry_core.conj, acc, extended_l6_items13);} else {
return acc;}
}, cherry_core.vector(), cherry_core.keyword("items").call(null, l1_item));
if (cherry_core.truth_(cherry_core.empty_QMARK_.call(null, filtered_l6_items11))) {
null} else {
xf.call(null, acc, cherry_core.dissoc.call(null, l1_item, cherry_core.keyword("items")));
let seq__1418 = cherry_core.seq.call(null, filtered_l6_items11);
let chunk__1519 = null;
let count__1620 = 0;
let i__1721 = 0;
while(true){
if (cherry_core.truth_((i__1721 < count__1620))) {
let l6_item22 = cherry_core._nth.call(null, chunk__1519, i__1721);
xf.call(null, acc, l6_item22);
null;
let G__23 = seq__1418;
let G__24 = chunk__1519;
let G__25 = count__1620;
let G__26 = cherry_core.unchecked_inc.call(null, i__1721);
seq__1418 = G__23;
chunk__1519 = G__24;
count__1620 = G__25;
i__1721 = G__26;
continue;
} else {
let temp__22496__auto__27 = cherry_core.seq.call(null, seq__1418);
if (cherry_core.truth_(temp__22496__auto__27)) {
let seq__1428 = temp__22496__auto__27;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__1428))) {
let c__22611__auto__29 = cherry_core.chunk_first.call(null, seq__1428);
let G__30 = cherry_core.chunk_rest.call(null, seq__1428);
let G__31 = c__22611__auto__29;
let G__32 = cherry_core.count.call(null, c__22611__auto__29);
let G__33 = 0;
seq__1418 = G__30;
chunk__1519 = G__31;
count__1620 = G__32;
i__1721 = G__33;
continue;
} else {
let l6_item34 = cherry_core.first.call(null, seq__1428);
xf.call(null, acc, l6_item34);
null;
let G__35 = cherry_core.next.call(null, seq__1428);
let G__36 = null;
let G__37 = 0;
let G__38 = 0;
seq__1418 = G__35;
chunk__1519 = G__36;
count__1620 = G__37;
i__1721 = G__38;
continue;
}}};break;
}
};
return acc;
};
f5["cljs$core$IFn$_invoke$arity$1"] = function (result) {
return result;
};
f5["cljs$lang$maxFixedArity"] = 2;
return f5;
}
;
var filter_items = function (data, search_term) {
return cherry_core.persistent_BANG_.call(null, cherry_core.into.call(null, cherry_core.vector(), cherry_core.partial.call(null, data_transducer, search_term), data));
}
;
var handle_message = function (event) {
let search_term39 = event.data;
console.log("Worker received:", ({ "term": search_term39 }));
let result40 = filter_items.call(null, data, search_term39);
console.log("Worker returned:", cherry_core.clj__GT_js.call(null, result40));
return postMessage(cherry_core.clj__GT_js.call(null, result40));
}
;
onmessage = handle_message;

export { fetch_data, data, match_l6_item, data_transducer, filter_items, handle_message }
