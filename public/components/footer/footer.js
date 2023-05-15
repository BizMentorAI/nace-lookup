import { tag, createStyleLink } from "framework"

class Footer extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: "open"})
    this.shadowRoot.appendChild(this.style)
    this.shadowRoot.appendChild(this.footer)
  }

  get style() {
    return this._style ||= createStyleLink("components/footer/footer.css")
  }

  get footer() {
    return this._footer ||= tag("footer", "&copy; 2023")
  }
}

customElements.define("bm-footer", Footer, {extends: "footer"})
