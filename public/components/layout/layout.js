import { tag, createStyleLink } from "framework"
import { dev } from "config"

class Layout extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: "open"})

    this.shadowRoot.appendChild(this.style)
    this.shadowRoot.appendChild(this.header)

    this.style.addEventListener("load", (_) => {
      this.shadowRoot.appendChild(this.screen)
      this.shadowRoot.appendChild(this.main)
      this.shadowRoot.appendChild(this.footer)
    })

    // Fix page height for mobile Safari.
    // Doing this in CSS doesn't work as Chrome matches it also.
    // https://allthingssmitty.com/2020/05/11/css-fix-for-100vh-in-mobile-webkit
    if (this.isMobileSafari()) {
      //this.shadowRoot.style.minHeight = "-webkit-fill-available"
      //this.shadowRoot.appendChild(tag("style", ":host { min-height: -webkit-fill-available; }"))
    }

    this.updateScreenDebugInfo()
  }

  connectedCallback() {
    window.addEventListener("resize", e => this.updateScreenDebugInfo())
  }

  get style() {
    return this._style ||= createStyleLink("components/layout/layout.css")
  }

  get screen() {
    return this._screen ||= tag("div", {id: "screen"})
  }

  get header() {
    return this._header ||= tag("header", {is: "bm-header"})
  }

  get main() {
    return this._main ||= tag("main", tag("slot"))
  }

  get footer() {
    return this._footer ||= tag("footer", {is: "bm-footer"})
  }

  isMobileSafari() {
    const ua = window.navigator.userAgent
    return ua.match(/iPhone|iPad/i) && ua.match(/WebKit/i) && !ua.match(/CriOS/i)
  }

  screenDebugInfo() {
    return [`${this.isMobileSafari() ? "(i)" : ""}${this.screenSize()[0]}`,
            `${window.innerWidth}x${window.innerHeight}`,
            `(${window.devicePixelRatio})`]
  }

  updateScreenDebugInfo() {
    if (dev) {
      this.screen.innerHTML = this.screenDebugInfo().join(" ")
    }
  }

  screenSize() {
    return Object.entries({XS: 600, SM: 900}).find(([ _, breakpoint ]) =>
      window.matchMedia(`(max-width: ${breakpoint}px)`).matches
    ) || ["LG", 1200]
  }
}

customElements.define("bm-layout", Layout)

// Import map doesn't work with import().
// Doesn't it? It most definitely should based on the docs.
import("../header/header.js")
import("../footer/footer.js")
