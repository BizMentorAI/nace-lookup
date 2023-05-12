import { BMElement, tag } from "/js/framework/main.js"

class Autocomplete extends BMElement {
  constructor() {
    super()
    this.shadowRoot.appendChild(this.style)
    this.shadowRoot.appendChild(this.autocomplete)
  }

  connectedCallback() {
    this.worker
  }

  disconnectedCallback() {
    this.worker.terminate()
  }

  get style() {
    return this._style ||= tag("link", {rel: "stylesheet", href: "/components/autocomplete/autocomplete.css"})
  }

  get autocomplete() {
    if (this._autocomplete) return this._autocomplete

    const input = tag("input", {placeholder: "🔎  Type in a product or service keyword."})
    input.addEventListener("input", (e) => this.handleInput())
    return this._autocomplete = input
  }

  get worker() {
    if (this._worker) return this._worker

    const worker = new Worker("/workers/autocomplete/autocomplete.js", {name: "ac", type: "module"})
    worker.addEventListener("message", (e) => this.handleMessage(e))
    worker.addEventListener("error", () => console.log("ERR", arguments))

    return this._worker = worker
  }

  get value() {
    return this.autocomplete.value
  }

  handleInput() {
    this.worker.postMessage(this.value)
  }

  handleMessage(event) {
    console.log("Main thread got back: ", event.data)
    this.buildMenu(event.data)
  }

  buildMenu(items) {
    console.log("buildMenu", items)
  }
}

customElements.define("bm-autocomplete", Autocomplete)

//                  (event-listener autocomplete :input (.handleInput this))))
//                      (set! worker (js* "new Worker('/js/workers/autocomplete.js', {type: 'module'})"))
//                      (event-listener worker :message (.handleMessage this)))
//   (handleMessage [this] (fn [event] (.buildMenu this (.-data event))))
