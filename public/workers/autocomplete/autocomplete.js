import * as cherry_core from '/js/cljs_core.js';
var fetch_data = async function () {
let response1 = (await fetch("/workers/autocomplete/data.json"));
let body2 = (await response1.json());
return body2;
}
;
var data = (await fetch_data.call(null))
;
var match_l6_item = function (fields, search_term) {
console.log("match-l6-item");
return cherry_core.first.call(null, cherry_core.filter.call(null, function (field) {
return cherry_core.re_find.call(null, search_term, field);
}, fields));
}
;
var match_l6_items = function (items, search_term) {
console.log("match-l6-items");
return cherry_core.map.call(null, function (p__3) {
let map__45 = p__3;
let map__46 = cherry_core.__destructure_map.call(null, map__45);
let code7 = map__46["code"];
let label8 = map__46["label"];
if (cherry_core.truth_(match_l6_item.call(null, cherry_core.vector(label8)))) {
return cherry_core.array_map(cherry_core.keyword("code"), code7, cherry_core.keyword("label"), label8);}
}, items);
}
;
var item_matches = function (p__9, search_term) {
let map__1011 = p__9;
let map__1012 = cherry_core.__destructure_map.call(null, map__1011);
let code13 = map__1012["code"];
let label14 = map__1012["label"];
let items15 = map__1012["items"];
console.log("item-matches");
let matched_items16 = match_l6_items.call(null, items15, search_term);
if (cherry_core.truth_(cherry_core.empty_QMARK_.call(null, matched_items16))) {
return null;} else {
return cherry_core.array_map(cherry_core.keyword("code"), code13, cherry_core.keyword("label"), cherry_core.keyword("label"), cherry_core.keyword("items"), matched_items16);}
}
;
var filter_items = function (data, search_term) {
console.log("filter-items");
return cherry_core.reduce.call(null, function (acc, l4_item) {
let match_result17 = item_matches.call(null, l4_item, search_term);
if (cherry_core.truth_(match_result17)) {
null};
cherry_core.conj.call(null, acc, match_result17);
return match_result17;
}, cherry_core.array_map(), data);
}
;
var handle_message = function (event) {
let search_term18 = event.data;
console.log("Worker received:", ({ "term": search_term18 }));
let result19 = filter_items.call(null, data, search_term18);
return postMessage(cherry_core.clj__GT_js.call(null, result19));
}
;
self.onmessage = handle_message;

export { fetch_data, data, match_l6_item, match_l6_items, item_matches, filter_items, handle_message }
