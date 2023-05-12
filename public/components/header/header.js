import { BMElement, tag } from "/js/framework/main.js"

class Header extends BMElement {
  constructor() {
    super()
    this.shadowRoot.appendChild(this.style)
    this.shadowRoot.appendChild(this.logo)
    this.shadowRoot.appendChild(this.tagLine)
  }

  get style() {
    return this._style ||= tag("link", {rel: "stylesheet", href: "/components/header/header.css"})
  }

  get logo() {
    return this._logo ||= tag("h1", "NACE lookup")
  }

  get tagLine() {
    return this._tagLine ||= tag("ul", "<li>fast</li><li>easy</li><li>accurate</li>")
  }
}

customElements.define("bm-header", Header)
