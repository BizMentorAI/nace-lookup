export const errorEmail = "info@nace-lookup.com"

export const dev = window.location.protocol == "http:"

// TODO: Extract out to the framework.
function paramsToObject(entries) {
  const result = {}

  for(const [key, value] of entries) {
    // ?inspect     => {inspect: true}
    // ?inspect=on  => {inspect: true}
    // ?inspect=off => {inspect: false}
    if (["", "on", "true", "yes"].includes(value)) {
      result[key] = true
    } else if (["off", "false", "no"].includes(value)) {
      result[key] = false
    } else {
      result[key] = value
    }
  }

  return result
}

const searchParams = new URLSearchParams(window.location.search)
const qs = paramsToObject(searchParams.entries())

if (Object.keys(qs).length) {
  console.log("Query string", qs)
}

export const inspectMode = qs.inspect || dev || false
