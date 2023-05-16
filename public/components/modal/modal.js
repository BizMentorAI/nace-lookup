// https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dialog
// It is recommended to use the .show() or .showModal() methods to render dialogs, rather than the open attribute. If a <dialog> is opened using the open attribute, it will be non-modal
// The ::backdrop CSS pseudo-element can be used to style the backdrop that is displayed behind a <dialog> element when the dialog is displayed with HTMLDialogElement.showModal(). For example, to dim unreachable content behind the modal dialog.
// <form method="dialog"><button>OK</button></form>
// <form> elements can close a <dialog> if they have the attribute method="dialog" or if the button used to submit the form has formmethod="dialog" set. In this case, the state of the form controls are saved, not submitted, the <dialog> closes, and the returnValue property gets set to the value of the button that was used to save the form's state.

import { tag, tap, createStyleLink } from "framework"
import { dev } from "config"

class Modal extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: "open"})
    this.shadowRoot.appendChild(this.style)
    this.shadowRoot.appendChild(this.modal)
  }

  get style() {
    return this._style ||= createStyleLink("components/modal/modal.css")
  }

  get details() {
    return this._details ||= tag("div", {id: "details"})
  }

  get modal() {
    return this._modal ||= tap(tag("dialog"), (modal) => {
      modal.appendChild(tag("h3", "Details"))
      modal.appendChild(this.details)
      modal.appendChild(tag("form", tag("button", {type: "submit", formmethod: "dialog"}, "OK")))
    })
  }

  displayItem(item) {
    this.details.replaceChildren(
      tap(tag("dl"), (dl) => {
        dl.appendChild(tag("dt", "L4 label"))
        dl.appendChild(tag("dd", item.l4Item.label))

        dl.appendChild(tag("dt", "L4 code"))
        dl.appendChild(tag("dd", {class: "code"}, item.l4Item.code))

        dl.appendChild(tag("dt", "L6 label"))
        dl.appendChild(tag("dd", item.label))

        dl.appendChild(tag("dt", "L6 code"))
        dl.appendChild(tag("dd", {class: "code"}, item.code))

        if (dev && item.extra) {
          dl.appendChild(tag("p", {class: "includes"}, item.extra))
        }
      }))

    this.modal.showModal()
  }
}

customElements.define("bm-modal", Modal)
