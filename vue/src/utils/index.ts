import { generateColors } from './color'
import { writeNewStyle } from './writeNewStyle'
import { getStyleTemplate } from './getStyleTemplate'

export { generateColors, writeNewStyle, getStyleTemplate }
/**
 * @param {date} time 需要转换的时间
 * @param {String} fmt 需要转换的格式 如 yyyy-MM-dd、yyyy-MM-dd HH:mm:ss
 * @returns {String}
 */
export const formatTime = (
    time: string | number | Date,
    fmt: string
): string => {
    if (!time) return ''
    const date = new Date(time)
    const o = {
        'M+': date.getMonth() + 1,
        'd+': date.getDate(),
        'H+': date.getHours(),
        'm+': date.getMinutes(),
        's+': date.getSeconds(),
        'q+': Math.floor((date.getMonth() + 3) / 3),
        S: date.getMilliseconds(),
    }
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(
            RegExp.$1,
            (date.getFullYear() + '').substr(4 - RegExp.$1.length)
        )
    for (const k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) {
            fmt = fmt.replace(
                RegExp.$1,
                // @ts-ignore: Unreachable code error
                RegExp.$1.length === 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length)
            )
        }
    }
    return fmt
}
export function deepMerge(target: any, merged: any) {
    for (const key in merged) {
        if (target[key] && typeof target[key] === 'object') {
            deepMerge(target[key], merged[key])

            continue
        }

        if (typeof merged[key] === 'object') {
            target[key] = deepClone(merged[key], true)

            continue
        }

        target[key] = merged[key]
    }

    return target
}

/**
 * @description Clone an object or array
 * @param {Object|Array} object Cloned object
 * @param {Boolean} recursion   Whether to use recursive cloning
 * @return {Object|Array} Clone object
 */
export function deepClone(object: any, recursion: boolean) {
    if (!object)
        return object
    const { parse, stringify } = JSON
    if (!recursion)
        return parse(stringify(object))
    const clonedObj: Record<string, any> = object instanceof Array ? [] : {}

    if (object && typeof object === 'object') {
        for (const key in object) {
            if (Object.prototype.hasOwnProperty.call(object, key)) {
                if (object[key] && typeof object[key] === 'object')
                    clonedObj[key] = deepClone(object[key], true)

                else
                    clonedObj[key] = object[key]
            }
        }
    }

    return clonedObj
}

