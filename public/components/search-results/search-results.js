import { tag, tap, createStyleLink } from "framework"
import { errorEmail } from "config"

class SearchResults extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: "open"})
    this.shadowRoot.appendChild(this.style)
    this.shadowRoot.appendChild(this.modal)
    this.shadowRoot.appendChild(this.results)
  }

  get style() {
    return this._style ||= createStyleLink("components/search-results/search-results.css")
  }

  get results() {
    return this._results ||= tag("div", {id: "results"})
  }

  get modal() {
    return this._modal ||= tag("bm-modal")
  }

  // connectedCallback -> emit event
  showResults(items, term) {
    this.results.replaceChildren
    (...items.map((item) => (
      tap(tag("div",
              {class: `l${item.level === 1 ? 1 : 6}-item`},
              item.level === 1
              ?
                item.label
              :
                [tag("span", {class: "code"}, item.code),
                 tag("span", {class: "label", html: this.#highlight(item.label, term)})]),
          (div) => {
            div.addEventListener("click", (event) => {
              // TODO: Could we somehow get event.currentTarget
              // so we could highlight it (the last visited item)?
              this.modal.displayItem(item)
            })
          }))))
  }

  // TODO: Global link styling, no highlight, nicer colour.
  showError() {
    this.results.replaceChildren(
      tag("div", {id: "error"}, [
        tag("h3", "Error"),
        tag("p", {html: `An unexpected error occurred. We'd be very thankful if you could write us to <a href="mailto:${errorEmail}">${errorEmail}</a> and let us know. Thank you!`})
      ])
    )
  }

  clear() {
    this.results.replaceChildren()
  }

  #highlight(label, term) {
    // Using group capture we make sure to pass back correct case (rice vs Rice).
    return label.replaceAll(
      new RegExp(`\\b(${term})`, "ig"),
        `<span class="highlight">$1</span>`)
  }
}

customElements.define("search-results", SearchResults)

import("../modal/modal.js")
