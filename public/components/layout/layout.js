import { tag, createStyleLink, unhide } from "framework"
import { dev } from "config"

class Layout extends HTMLElement {
  #resizeHandler
  constructor() {
    super()
    this.attachShadow({mode: "open"})

    this.shadowRoot.appendChild(this.styleLink)
    this.shadowRoot.appendChild(this.header)
    this.shadowRoot.appendChild(this.screen)
    this.shadowRoot.appendChild(this.main)
    this.shadowRoot.appendChild(this.footer)

    this.styleLink.addEventListener("load", (_) => {
      unhide(...[dev && this.screen, this.main, this.footer])
    })

    this.#updateScreenDebugInfo()

    this.#resizeHandler = (_) => this.#updateScreenDebugInfo()
  }

  connectedCallback() {
    if (dev) {
      window.addEventListener("resize", this.#resizeHandler)
    }
  }

  disconnectedCallback() {
    window.removeEventListener("resize", this.#resizeHandler)
  }

  #styleLink
  get styleLink() {
    return this.#styleLink ||= createStyleLink("components/layout/layout.css")
  }

  #screen
  get screen() {
    return this.#screen ||= tag("div", {id: "screen", style: {display: "none"}})
  }

  #header
  get header() {
    return this.#header ||= tag("bm-header")
  }

  #main
  get main() {
    return this.#main ||= tag("main", {style: {display: "none"}}, tag("slot"))
  }

  #footer
  get footer() {
    return this.#footer ||= tag("bm-footer", {style: {display: "none"}})
  }

  #screenDebugInfo() {
    return [this.screenSize()[0],
            `${window.innerWidth}x${window.innerHeight}`,
            `(${window.devicePixelRatio})`]
  }

  #updateScreenDebugInfo() {
    if (dev) {
      this.screen.innerHTML = this.#screenDebugInfo().join(" ")
    }
  }

  // TODO: change with media queries from tangle vars.
  screenSize() {
    return Object.entries({XS: 600, SM: 900}).find(([ _, breakpoint ]) =>
      window.matchMedia(`(max-width: ${breakpoint}px)`).matches
    ) || ["LG", 1200]
  }
}

customElements.define("bm-layout", Layout)

// Import map doesn't work with import().
// Doesn't it? It most definitely should based on the docs.
import("./header/header.js")
import("./footer/footer.js")
