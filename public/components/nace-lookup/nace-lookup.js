import { tag, createStyleLink } from "framework"

class NaceLookup extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: "open"})
    this.shadowRoot.appendChild(this.style)
    this.shadowRoot.appendChild(this.heading)
    this.shadowRoot.appendChild(this.autocomplete)
  }

  get style() {
    return this._style ||= createStyleLink("components/nace-lookup/nace-lookup.css")
  }

  get heading() {
    return this._heading ||= tag("h4", "Instantly identify the NACE and CPA codes you require:")
  }

  get autocomplete() {
    return this._autocomplete ||= tag("bm-autocomplete")
  }
}

customElements.define("bm-nace-lookup", NaceLookup)

import("../autocomplete/autocomplete.js")
