function setContent (element, content) {
  if (typeof content === "string") {
    element.innerHTML = content
  } else if (content.nodeName) {
    element.appendChild(content)
  }

  return element
}

function _tag (tag, opts = {}, content = null) {
  const element = document.createElement(tag)
  Object.entries(opts).forEach(([ key, value ]) => element[key] = value)

  if (content) { setContent(element, content) }
  return element
}

export function tag (tag, ...args) {
  if (args.length === 0) {
    return _tag(tag)
  } else if (args.length === 2) {
    return _tag(tag, ...args)
  } else if (args.length === 1 && args[0]?.constructor === Object) {
    return _tag(tag, args[0])
  } else {
    return _tag(tag, {}, args[0])
  }
}

export class BMElement extends HTMLElement {
  constructor() {
    super()
    const shadowRoot = this.attachShadow({mode: "open"})
  }
}
