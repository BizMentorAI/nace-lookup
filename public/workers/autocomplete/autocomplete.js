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
return cherry_core.first.call(null, cherry_core.filter.call(null, function (field) {
return field.match(new RegExp(cherry_core.str.call(null, "\\b", search_term), "i"));
}, fields));
}
;
var match_l6_items = function (items, search_term) {
return cherry_core.filter.call(null, function (p__3) {
let map__45 = p__3;
let map__46 = cherry_core.__destructure_map.call(null, map__45);
let code7 = map__46["code"];
let label8 = map__46["label"];
let extra9 = map__46["extra"];
if (cherry_core.truth_(match_l6_item.call(null, cherry_core.vector(label8, extra9), search_term))) {
return cherry_core.array_map(cherry_core.keyword("code"), code7, cherry_core.keyword("label"), label8);}
}, items);
}
;
var item_matches = function (p__10, search_term) {
let map__1112 = p__10;
let map__1113 = cherry_core.__destructure_map.call(null, map__1112);
let code14 = map__1113["code"];
let label15 = map__1113["label"];
let items16 = map__1113["items"];
let matched_items17 = match_l6_items.call(null, items16, search_term);
if (cherry_core.truth_(cherry_core.empty_QMARK_.call(null, matched_items17))) {
return null;} else {
return cherry_core.array_map(cherry_core.keyword("code"), code14, cherry_core.keyword("label"), label15, cherry_core.keyword("items"), matched_items17);}
}
;
var filter_items = function (data, search_term) {
return cherry_core.reduce.call(null, function (acc, l4_item) {
let match_result18 = item_matches.call(null, l4_item, search_term);
if (cherry_core.truth_(match_result18)) {
return cherry_core.conj.call(null, acc, match_result18);} else {
return acc;}
}, cherry_core.vector(), data);
}
;
var handle_message = function (event) {
let search_term19 = event.data;
let result20 = filter_items.call(null, data, search_term19);
return postMessage(cherry_core.clj__GT_js.call(null, result20));
}
;
self.onmessage = handle_message;

export { fetch_data, data, match_l6_item, match_l6_items, item_matches, filter_items, handle_message }
