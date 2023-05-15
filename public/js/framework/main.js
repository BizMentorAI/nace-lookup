function setContent (element, content) {
  if (typeof content === "string") {
    element.innerHTML = content
  } else if (content.nodeName) {
    element.appendChild(content)
  } else if (content.forEach) {
    content.forEach(i => setContent(element, i))
  }

  return element
}

function _tag (tag, opts = {}, content = null) {
  const element = opts.is ? document.createElement(tag, {is: opts.is}) : document.createElement(tag)
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

export function createStyleLink (path) {
  return tag("link", {rel: "stylesheet", href: path})
}

// Can I use import map with workers?
export function createWorker (path, messageHandler, errorHandler) {
  const name = path.replace(/.*\/([^/]+)\.js$/, '$1')
  const worker = new Worker(path, {name, type: "module"})

  worker.addEventListener("message", messageHandler)
  worker.addEventListener("error", (e) => console.error("Worker error", e))
  worker.addEventListener("error", errorHandler)

  return worker
}

export function tap (value, fn) {
  fn(value)
  return value
}
