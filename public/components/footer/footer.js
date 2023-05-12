import { BMElement, tag } from "/js/framework/main.js"

class Footer extends BMElement {
  constructor() {
    super()
    this.shadowRoot.appendChild(this.style)
    this.shadowRoot.appendChild(this.footer)
  }

  get style() {
    return this._style ||= tag("link", {rel: "stylesheet", href: "/components/footer/footer.css"})
  }

  get footer() {
    return this._footer ||= tag("footer", "&copy; 2023")
  }
}

customElements.define("bm-footer", Footer)
