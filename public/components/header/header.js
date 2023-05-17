import { tag, createStyleLink } from "framework"

class Header extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: "open"})
    this.shadowRoot.appendChild(this.style)
    this.style.addEventListener("load", (e) => {
      this.shadowRoot.appendChild(this.logo)
      this.shadowRoot.appendChild(this.tagLine)
    })
  }

  get style() {
    return this._style ||= createStyleLink("components/header/header.css")
  }

  get logo() {
    return this._logo ||= tag("h1", "NACE lookup")
  }

  get tagLine() {
    return this._tagLine ||= (
      tag("ul", ["fast", "easy", "accurate"].map(word => tag("li", word)))
    )
  }
}

customElements.define("bm-header", Header)
