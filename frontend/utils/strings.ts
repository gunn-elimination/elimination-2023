export function lowercaseFirstLetter(str: string) {
    return str.charAt(0).toLowerCase() + str.slice(1);
}

export function ordinal(x: number) {
    const base = x % 10;
    if (base === 1) return `${x}st`;
    if (base === 2) return `${x}nd`;
    if (base === 3) return `${x}rd`;
    return `${x}th`;
}
