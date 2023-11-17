"use strict";
exports.__esModule = true;
exports.Spots = exports.Color = void 0;
var Color;
(function (Color) {
    Color[Color["RED"] = 0] = "RED";
    Color[Color["GREEN"] = 1] = "GREEN";
    Color[Color["YEllOW"] = 2] = "YEllOW";
    Color[Color["BLUE"] = 3] = "BLUE";
})(Color = exports.Color || (exports.Color = {}));
var Spots;
(function (Spots) {
    Spots[Spots["none"] = 0] = "none";
    Spots[Spots["hidden"] = 1] = "hidden";
    Spots[Spots["dotted"] = 2] = "dotted";
    Spots[Spots["dashed"] = 3] = "dashed";
    Spots[Spots["solid"] = 4] = "solid";
    Spots[Spots["double"] = 5] = "double";
    Spots[Spots["groove"] = 6] = "groove";
    Spots[Spots["ridge"] = 7] = "ridge";
    Spots[Spots["inset"] = 8] = "inset";
    Spots[Spots["outset"] = 9] = "outset";
})(Spots = exports.Spots || (exports.Spots = {}));
var mushrooms = [
    {
        name: 'nervous bell',
        spots: Spots.dashed,
        color: Color.RED,
        latlng: [52.082042, 5.236192]
    },
    {
        name: 'nice benz',
        spots: Spots.dotted,
        color: Color.BLUE,
        latlng: [52.080678, 5.236457]
    },
    {
        name: 'quizzical chaplygin',
        spots: Spots.double,
        color: Color.RED,
        latlng: [52.081624, 5.235895]
    },
    {
        name: 'nifty bhabha',
        spots: Spots.groove,
        color: Color.BLUE,
        latlng: [52.080671, 5.236392]
    },
    {
        name: 'peaceful dijkstra',
        spots: Spots.hidden,
        color: Color.GREEN,
        latlng: [52.081451, 5.235404]
    },
    {
        name: 'nostalgic bhaskara',
        spots: Spots.inset,
        color: Color.BLUE,
        latlng: [52.080552, 5.234156]
    },
    {
        name: 'silly burnell',
        spots: Spots.ridge,
        color: Color.BLUE,
        latlng: [52.080598, 5.234361]
    },
    {
        name: 'romantic cray',
        spots: Spots.groove,
        color: Color.GREEN,
        latlng: [52.080253, 5.234631]
    },
    {
        name: 'vigilant bose',
        spots: Spots.solid,
        color: Color.RED,
        latlng: [52.081063, 5.236819]
    },
    {
        name: 'quirky buck',
        spots: Spots.dashed,
        color: Color.GREEN,
        latlng: [52.081163, 5.236446]
    },
    {
        name: 'stoic cartwright',
        spots: Spots.double,
        color: Color.BLUE,
        latlng: [52.080861, 5.235876]
    },
    {
        name: 'pensive clarke',
        spots: Spots.dotted,
        color: Color.YEllOW,
        latlng: [52.081199, 5.234929]
    },
    {
        name: 'strange cannon',
        spots: Spots.hidden,
        color: Color.YEllOW,
        latlng: [52.080273, 5.234664]
    },
    {
        name: 'stupefied bohr',
        spots: Spots.none,
        color: Color.RED,
        latlng: [52.081883, 5.235415]
    },
    {
        name: 'pedantic colden',
        spots: Spots.hidden,
        color: Color.BLUE,
        latlng: [52.081813, 5.236781]
    },
    {
        name: 'priceless davinci',
        spots: Spots.dashed,
        color: Color.GREEN,
        latlng: [52.081538, 5.236694]
    },
    {
        name: 'vibrant chandrasekhar',
        spots: Spots.outset,
        color: Color.RED,
        latlng: [52.081182, 5.237143]
    },
    {
        name: 'wizardly booth',
        spots: Spots.groove,
        color: Color.GREEN,
        latlng: [52.080429, 5.237424]
    },
    {
        name: 'suspicious driscoll',
        spots: Spots.inset,
        color: Color.YEllOW,
        latlng: [52.080074, 5.236462]
    },
    {
        name: 'optimistic blackburn',
        spots: Spots.ridge,
        color: Color.RED,
        latlng: [52.080144, 5.235857]
    },
    {
        name: 'reverent curie',
        spots: Spots.groove,
        color: Color.BLUE,
        latlng: [52.081222, 5.235965]
    },
];
exports["default"] = (function () {
    return new Promise(function (resolve) {
        setTimeout(function () {
            resolve(mushrooms);
        }, 1500);
    });
});
// name: 'objective black',
// name: 'practical curran',
// name: 'recursing darwin',
// name: 'relaxed dhawan',
// name: 'sad chatterjee',
// name: 'serene cohen',
// name: 'sharp dubinsky',
// name: 'sleepy chebyshev',
// name: 'sweet brattain',
// name: 'tender dewdney',
// name: 'thirsty brahmagupta',
// name: 'trusting blackwell',
// name: 'unruffled cerf',
// name: 'upbeat carver',
// name: 'vigorous bouman',
// name: 'wonderful carson',
// name: 'xenodochial borg',
// name: 'youthful brown',
// name: 'zealous boyd',
// name: 'zen chatelet',
