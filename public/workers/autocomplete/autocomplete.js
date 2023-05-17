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
return cherry_core.persistent_BANG_.call(null, cherry_core.into.call(null, cherry_core.vector(), cherry_core.partial.call(null, transducer, search_term), data));
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
let search_term3 = event.data;
console.log("Worker received:", ({ "term": search_term3 }));
let result4 = convert_format.call(null, filter_items.call(null, data, search_term3));
console.log("Worker returned:", cherry_core.clj__GT_js.call(null, result4));
return postMessage(cherry_core.clj__GT_js.call(null, result4));
}
;
onmessage = handle_message;

export { fetch_data, data, filter_items, convert_format, handle_message }
