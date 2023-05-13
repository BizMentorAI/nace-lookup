import * as cherry_core from '/js/cljs_core.js';
var fetch_data = async function () {
let response1 = (await fetch("/workers/autocomplete/data.json"));
let body2 = (await response1.json());
return body2;
}
;
var data = (await fetch_data.call(null))
;
var filter_items = function (search_term) {
return cherry_core.array_map(cherry_core.keyword("a"), 7);
}
;
var handle_message = function (event) {
let search_term3 = event.data;
console.log("Worker received:", ({ "term": search_term3 }));
let result4 = filter_items.call(null, search_term3);
return postMessage(cherry_core.clj__GT_js.call(null, result4));
}
;
self.onmessage = handle_message;

export { fetch_data, data, filter_items, handle_message }
