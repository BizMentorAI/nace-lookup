import { tag, tap, createStyleLink, createWorker } from "framework"

class Autocomplete extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: "open"})
    this.shadowRoot.appendChild(this.style)
    this.shadowRoot.appendChild(this.autocomplete)
    this.shadowRoot.appendChild(this.results)
  }

  connectedCallback() {
    this.results; this.worker

    customElements.whenDefined("search-results").then(() => {
      this.worker.addEventListener("error", (event) => {
        this.results.showError(event)
      })

      this.worker.addEventListener("message", (event) => {
        this.results.showResults(event.data, this.value)
      })
    })
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
      input.autofocus = true
      input.addEventListener("input", (_) => this.handleInput())
    })
  }

  get results() {
    return this._results ||= tag("search-results")
  }

  get worker() {
    return this._worker ||= createWorker("/workers/autocomplete/autocomplete.js")
  }

  get value() {
    return this.autocomplete.value
  }

  handleInput() {
    if (this.value.length > 2) {
      this.worker.postMessage(this.value)
    } else {
      this.results.clear()
    }
  }
}

customElements.define("bm-autocomplete", Autocomplete)

import("../search-results/search-results.js")
