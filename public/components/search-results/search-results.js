import { tag, tap, createStyleLink } from "framework"
import { inspectMode, errorEmail } from "config"

class SearchResults extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: "open"})
    this.shadowRoot.appendChild(this.styleLink)
    this.shadowRoot.appendChild(this.modal)
    this.shadowRoot.appendChild(this.results)

    if (this.dataset.items) {
      const items = JSON.parse(this.dataset.items)
      const term  = this.dataset.term || ""
      this.showResults(term, items)
    }
  }

  #styleLink
  get styleLink() {
    return this.#styleLink ||= createStyleLink("components/search-results/search-results.css")
  }

  #results
  get results() {
    return this.#results ||= tag("div", {id: "results"})
  }

  #modal
  get modal() {
    return this.#modal ||= tag("bm-modal")
  }

  // connectedCallback -> emit event
  showResults(term, items) {
    if (items.length) {
      this.results.replaceChildren
      // This should really be ul/li.
      (...items.map((item) => (
        tap(tag("div",
                {class: `l${item.heading ? 1 : 6}-item`,
                html: item.heading ? item.label : this.#highlight(item.label, term)}),
            (div) => {
              div.addEventListener("click", (event) => {
                // TODO: Could we somehow get event.currentTarget
                // so we could highlight it (the last visited item)?
                console.log(item) ////
                this.modal.renderBody([
                  // TODO: Should be somewhere in setup.
                  createStyleLink("components/search-results/modal.css"),

                  tag("h3", "Business NACE code is"),
                  tag("div", {class: "row"}, [
                    tag("div", {class: "code"}, item.l4Item.code),
                    tag("div", {class: "label"}, item.l4Item.label)]),

                  tag("h3", "Product or service CPA code is"),
                  tag("div", {class: "row"}, [
                    tag("div", {class: "code"}, item.code),
                    tag("div", {class: "label"}, item.label)]),

                  // TODO: singularise?
                  tag("div", {style: {
                              maxWidth: "500px",
                              display: inspectMode ? "block" : "none"}},
                      Object.entries(item.extra).map(([ key, text ]) => (
                        [tag("h3", key), tag("code", text)]
                      )).flat())])})}))))
    } else {
      this.results.replaceChildren(
        tag("div", {class: "no-results"}, [
         tag("h3", {class: "heading"}, "No matching results in the EU classification system"),
         tag("p", "Try a related search term.")]))
    }
  }

  // TODO: Global link styling, no highlight, nicer colour.
  showError() {
    this.results.replaceChildren(
      tag("div", {id: "error"}, [
        tag("h3", "Error"),
        tag("p", {html: `An unexpected error occurred. We'd be very thankful if you could write to us <a href="mailto:${errorEmail}">${errorEmail}</a> and let us know. Thank you!`})]))
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
