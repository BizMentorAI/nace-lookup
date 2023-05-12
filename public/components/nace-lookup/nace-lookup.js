import { BMElement, tag } from "/js/framework/main.js"

class NaceLookup extends BMElement {
  constructor() {
    super()
    this.shadowRoot.appendChild(this.style)
    this.shadowRoot.appendChild(this.heading)
    this.shadowRoot.appendChild(this.autocomplete)
  }

  get style() {
    return this._style ||= tag("link", {rel: "stylesheet", href: "/components/nace-lookup/nace-lookup.css"})
  }

  get heading() {
    return this._heading ||= tag("h4", "Instantly identify the NACE and CPA codes you require:")
  }

  get autocomplete() {
    return this._autocomplete ||= tag("bm-autocomplete")
  }
}

customElements.define("bm-nace-lookup", NaceLookup)

import("/components/autocomplete/autocomplete.js")
