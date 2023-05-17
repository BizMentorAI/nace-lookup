import { tag, createStyleLink } from "framework"

/* This used to extend the footer element,
   but Safari doesn't support that just yet.

   The other issue is that you cannot query for :not(:defined)
   as built-in elements are always defined.
*/
class Footer extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: "open"})
    this.shadowRoot.appendChild(this.styleLink)
    this.styleLink.addEventListener("load", (e) => {
      this.shadowRoot.appendChild(this.footer)
    })
  }

  get styleLink() {
    return this._style ||= createStyleLink("components/footer/footer.css")
  }

  get footer() {
    return this._footer ||= tag("footer", {html: "&copy; 2023"})
  }
}

customElements.define("bm-footer", Footer)
