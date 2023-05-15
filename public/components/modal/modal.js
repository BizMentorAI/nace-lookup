// https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dialog
// It is recommended to use the .show() or .showModal() methods to render dialogs, rather than the open attribute. If a <dialog> is opened using the open attribute, it will be non-modal
// The ::backdrop CSS pseudo-element can be used to style the backdrop that is displayed behind a <dialog> element when the dialog is displayed with HTMLDialogElement.showModal(). For example, to dim unreachable content behind the modal dialog.
// <form method="dialog"><button>OK</button></form>
// <form> elements can close a <dialog> if they have the attribute method="dialog" or if the button used to submit the form has formmethod="dialog" set. In this case, the state of the form controls are saved, not submitted, the <dialog> closes, and the returnValue property gets set to the value of the button that was used to save the form's state.
const button = tag("button", "OK")
button.addEventListener("click", (_) => dialog.close())
const dialog = tag("dialog", ["<h3>Test</h3>", button])
this.shadowRoot.appendChild(dialog)
dialog.open()
