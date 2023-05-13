import { BMElement, tag, createStyleLink, createWorker } from "framework"

class Autocomplete extends BMElement {
  constructor() {
    super()
    this.shadowRoot.appendChild(this.style)
    this.shadowRoot.appendChild(this.autocomplete)
    this.shadowRoot.appendChild(this.results)
  }

  connectedCallback() {
    this.worker
  }

  disconnectedCallback() {
    this.worker.terminate()
  }

  get style() {
    return this._style ||= createStyleLink("components/autocomplete/autocomplete.css")
  }

  get autocomplete() {
    if (this._autocomplete) return this._autocomplete

    const placeholder = "🔎  Type in a product or service keyword."
    const input = tag("input", {placeholder})
    input.addEventListener("input", (e) => this.handleInput())
    return this._autocomplete = input
  }

  get results() {
    return this._results ||= tag("div", {id: "results"})
  }

  get worker() {
    return this._worker ||= (
      createWorker("/workers/autocomplete/autocomplete.js",
                   (e) => this.handleMessage(e))
    )
  }

  get value() {
    return this.autocomplete.value
  }

  handleInput() {
    if (this.value.length > 2) {
      this.worker.postMessage(this.value)
    } else {
      this.results.replaceChildren()
    }
  }

  handleMessage(event) {
    this.buildMenu(event.data)
  }

  highlight(label) {
    return label.replace(
      new RegExp(`\\b${this.value}`),
        `<span class="highlight">${this.value}</span>`)
  }

  buildMenu(items) {
    this.results.replaceChildren(...
      items.reduce((elements, { label, items }) => {
        elements.push(tag("div", {className: "l4-item"} , label))
        items.forEach(({ code, label }) => {
          elements.push(tag("div", {className: "l6-item"}, [
            tag("span", {className: "code"}, code),
            tag("span", {className: "label"}, this.highlight(label))
          ]))
        })

        return elements
      }, [])
    )
  }
}

customElements.define("bm-autocomplete", Autocomplete)
