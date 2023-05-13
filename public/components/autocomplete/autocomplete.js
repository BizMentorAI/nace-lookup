import { BMElement, tag, createStyleLink, createWorker } from "framework"

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
    return this._style ||= createStyleLink("components/autocomplete/autocomplete.css")
  }

  get autocomplete() {
    if (this._autocomplete) return this._autocomplete

    const placeholder = "🔎  Type in a product or service keyword."
    const input = tag("input", {placeholder})
    input.addEventListener("input", (e) => this.handleInput())
    return this._autocomplete = input
  }

  get worker() {
    return this._worker ||= (
      createWorker("/workers/autocomplete/autocomplete.js",
                   (e) => this.handleMessage(e))
    )
  }

  get value() {
    return this.autocomplete.value
  }

  handleInput() {
    console.log("handleInput", {term: this.value}) ///////
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
