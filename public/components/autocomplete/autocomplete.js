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

    // Timeout is a terrible solution for this, but I cannot get
    // either of these to work:
    // - this.results load listener
    // - document's DOMContentLoaded
    setTimeout(() => {
      this.worker.addEventListener("error", (event) => {
        this.results.showError(event)
      })

      this.worker.addEventListener("message", (event) => {
        this.results.showResults(event.data, this.value)
      })
    }, 250)
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
      input.autocorrect = "off" // can I use false here? Alternative way with setAttribute.
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
      this.results.replaceChildren()
    }
  }
}

customElements.define("bm-autocomplete", Autocomplete)

import("../search-results/search-results.js")
