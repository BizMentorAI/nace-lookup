import * as cherry_core from '/js/cljs_core.js';
var match_l6_item = function (item, regexp) {
let fields1 = cherry_core.flatten.call(null, cherry_core.vector(cherry_core.keyword("label").call(null, item), cherry_core.map.call(null, function (_PERCENT_1) {
return cherry_core.get.call(null, cherry_core.keyword("extra").call(null, item), _PERCENT_1);
}, cherry_core.keys.call(null, cherry_core.keyword("extra").call(null, item)))));
cherry_core.prn.call(null, fields1);
return cherry_core.first.call(null, cherry_core.filter.call(null, function (_PERCENT_1) {
return cherry_core.re_find.call(null, regexp, _PERCENT_1);
}, fields1));
}
;
var transducer = function (search_term, rf) {
let regexp2 = cherry_core.re_pattern.call(null, cherry_core.str.call(null, "(?i)\\b", search_term));
let f3 = function (var_args) {
let G__67 = cherry_core.alength.call(null, arguments);
switch (G__67) {case 0:
return f3.cljs$core$IFn$_invoke$arity$0();
break;
case 2:
return f3.cljs$core$IFn$_invoke$arity$2((arguments[0]), (arguments[1]));
break;
case 1:
return f3.cljs$core$IFn$_invoke$arity$1((arguments[0]));
break;
default:
throw new Error(cherry_core.str.call(null, "Invalid arity: ", cherry_core.alength.call(null, arguments)))}
};
f3["cljs$core$IFn$_invoke$arity$0"] = function () {
return rf.call(null);
};
f3["cljs$core$IFn$_invoke$arity$2"] = function (acc, l1_item) {
let filtered_l6_items9 = cherry_core.reduce.call(null, function (acc, l4_item) {
let filtered_l6_items10 = cherry_core.filter.call(null, function (l6_item) {
return match_l6_item.call(null, l6_item, regexp2);
}, cherry_core.keyword("items").call(null, l4_item));
let extended_l6_items11 = cherry_core.map.call(null, function (l6_item) {
return cherry_core.assoc.call(null, l6_item, cherry_core.keyword("l4Item"), cherry_core.array_map(cherry_core.keyword("code"), cherry_core.keyword("code").call(null, l4_item), cherry_core.keyword("label"), cherry_core.keyword("label").call(null, l4_item)));
}, filtered_l6_items10);
if (cherry_core.truth_(cherry_core.not.call(null, cherry_core.empty_QMARK_.call(null, extended_l6_items11)))) {
return cherry_core.apply.call(null, cherry_core.conj, acc, extended_l6_items11);} else {
return acc;}
}, cherry_core.vector(), cherry_core.keyword("items").call(null, l1_item));
if (cherry_core.truth_(cherry_core.empty_QMARK_.call(null, filtered_l6_items9))) {
null} else {
rf.call(null, acc, cherry_core.dissoc.call(null, l1_item, cherry_core.keyword("items")));
let seq__1216 = cherry_core.seq.call(null, filtered_l6_items9);
let chunk__1317 = null;
let count__1418 = 0;
let i__1519 = 0;
while(true){
if (cherry_core.truth_((i__1519 < count__1418))) {
let l6_item20 = cherry_core._nth.call(null, chunk__1317, i__1519);
rf.call(null, acc, l6_item20);
null;
let G__21 = seq__1216;
let G__22 = chunk__1317;
let G__23 = count__1418;
let G__24 = cherry_core.unchecked_inc.call(null, i__1519);
seq__1216 = G__21;
chunk__1317 = G__22;
count__1418 = G__23;
i__1519 = G__24;
continue;
} else {
let temp__22496__auto__25 = cherry_core.seq.call(null, seq__1216);
if (cherry_core.truth_(temp__22496__auto__25)) {
let seq__1226 = temp__22496__auto__25;
if (cherry_core.truth_(cherry_core.chunked_seq_QMARK_.call(null, seq__1226))) {
let c__22611__auto__27 = cherry_core.chunk_first.call(null, seq__1226);
let G__28 = cherry_core.chunk_rest.call(null, seq__1226);
let G__29 = c__22611__auto__27;
let G__30 = cherry_core.count.call(null, c__22611__auto__27);
let G__31 = 0;
seq__1216 = G__28;
chunk__1317 = G__29;
count__1418 = G__30;
i__1519 = G__31;
continue;
} else {
let l6_item32 = cherry_core.first.call(null, seq__1226);
rf.call(null, acc, l6_item32);
null;
let G__33 = cherry_core.next.call(null, seq__1226);
let G__34 = null;
let G__35 = 0;
let G__36 = 0;
seq__1216 = G__33;
chunk__1317 = G__34;
count__1418 = G__35;
i__1519 = G__36;
continue;
}}};break;
}
};
return acc;
};
f3["cljs$core$IFn$_invoke$arity$1"] = function (result) {
return result;
};
f3["cljs$lang$maxFixedArity"] = 2;
return f3;
}
;

export { match_l6_item, transducer }
