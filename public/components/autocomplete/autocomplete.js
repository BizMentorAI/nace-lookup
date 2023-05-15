import { BMElement, tag, tap, createStyleLink, createWorker } from "framework"

class Autocomplete extends BMElement {
  constructor() {
    super()
    this.shadowRoot.appendChild(this.style)
    this.shadowRoot.appendChild(this.autocomplete)
    this.shadowRoot.appendChild(this.results)
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
    return this._autocomplete ||= tap(tag("input"), (input) => {
      input.placeholder = "🔎  Type in a product or service keyword."
      // Autocorrect is Safari-only.
      // https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/search
      input.autocorrect = "off"
      input.addEventListener("input", (_) => this.handleInput())
    })
  }

  get results() {
    return this._results ||= tag("search-results")
  }

  get worker() {
    return this._worker ||= (
      createWorker("/workers/autocomplete/autocomplete.js",
                   (event) => this.results.showResults(event.data, this.value),
                   (event) => this.results.showError(event)))
  }

  get value() {
    return this.autocomplete.value
  }

  handleInput() {
    if (this.value.length > 2) {
      this.worker.postMessage(this.value)
    } else {
      this.results.replaceChildren()
    }
  }
}

customElements.define("bm-autocomplete", Autocomplete)

import("../search-results/search-results.js")
