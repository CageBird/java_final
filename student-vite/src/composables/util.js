export function queryParams(query){
    let q = []
    for (const key in query) {
        if(query[key]){
            q.push('${key}=${encodeURIComponent(query[key])}')
        }
    }
    let r = q.join("&")
    r = r ? ("?"+r) : ""
    return r
}