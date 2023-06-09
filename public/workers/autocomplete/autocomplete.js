import * as cherry_core from '/js/cljs_core.js';
import { transducer } from '/workers/autocomplete/transducer.js';
var fetch_data = async function () {
let response1 = (await fetch("/workers/autocomplete/data.json"));
let body2 = (await response1.json());
return body2;
}
;
var data = cherry_core.js__GT_clj.call(null, (await fetch_data.call(null)), cherry_core.array_map(cherry_core.keyword("keywordize-keys"), true))
;
var filter_items = function (data, search_term) {
let regexp3 = cherry_core.re_pattern.call(null, cherry_core.str.call(null, "(?i)\\b", search_term));
return cherry_core.map.call(null, function (_PERCENT_1) {
return cherry_core.dissoc.call(null, cherry_core.merge.call(null, _PERCENT_1, cherry_core.array_map(cherry_core.keyword("label"), cherry_core.keyword("en").call(null, _PERCENT_1), cherry_core.keyword("extra"), cherry_core.array_map(), cherry_core.keyword("heading"), false, cherry_core.keyword("code"), cherry_core.keyword("prd").call(null, _PERCENT_1))), cherry_core.keyword("en"));
}, cherry_core.filter.call(null, function (_PERCENT_1) {
return cherry_core.re_find.call(null, regexp3, cherry_core.keyword("en").call(null, _PERCENT_1));
}, data));
}
;
var convert_format = function (items) {
return cherry_core.map.call(null, function (item) {
if (cherry_core.truth_(cherry_core._EQ_.call(null, cherry_core.keyword("level").call(null, item), 1))) {
return cherry_core.assoc.call(null, cherry_core.dissoc.call(null, item, cherry_core.keyword("level")), cherry_core.keyword("heading"), true);} else {
return cherry_core.dissoc.call(null, item, cherry_core.keyword("level"));}
}, items);
}
;
var handle_message = function (event) {
let search_term4 = event.data;
console.log("Worker received:", ({ "term": search_term4 }));
let result5 = convert_format.call(null, filter_items.call(null, data, search_term4));
console.log("Worker returned:", cherry_core.clj__GT_js.call(null, result5));
return postMessage(cherry_core.clj__GT_js.call(null, result5));
}
;
onmessage = handle_message;

export { fetch_data, data, filter_items, convert_format, handle_message }
