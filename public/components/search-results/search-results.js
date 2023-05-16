import { tag, createStyleLink } from "framework"
import { errorEmail } from "config"

class SearchResults extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: "open"})
    this.shadowRoot.appendChild(this.style)
    this.shadowRoot.appendChild(this.results)
  }

  get style() {
    return this._style ||= createStyleLink("components/search-results/search-results.css")
  }

  get results() {
    return this._results ||= tag("div", {id: "results"})
  }

  showResults(items, term) {
    this.results.replaceChildren
    (...items.map((item) => (
      tag("div",
          {class:`l${item.level === 1 ? 1 : 6}-item`},
          this.#highlight(item.label))
    )))
  }

  // TODO: Global link styling, no highlight, nicer colour.
  showError() {
    this.results.replaceChildren(
      tag("div", {id: "error"}, [
        tag("h3", "Error"),
        tag("p", `An unexpected error occurred. We'd be very thankful if you could write us to <a href="mailto:${errorEmail}">${errorEmail}</a> and let us know. Thank you!`)
      ])
    )
  }

  #highlight(label, term) {
    return label.replace(
      new RegExp(`\\b${term}`, "i"),
        `<span class="highlight">${term}</span>`)
  }
}

customElements.define("search-results", SearchResults)

import("../modal/modal.js")
