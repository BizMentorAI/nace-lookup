export const errorEmail = "info@nace-lookup.com"

function paramsToObject(entries) {
  const result = {}

  for(const [key, value] of entries) {
    // ?dev => {dev: true}
    result[key] = value === "" ? true : value
  }

  return result
}

const searchParams = new URLSearchParams(window.location.search)
const qs = paramsToObject(searchParams.entries())

if (Object.keys(qs).length) {
  console.log("Query string", qs)
}

export const dev = qs.dev || false
