//
//  Koin.swift
//  iosApp
//
//  Created by Kenneth Cartaña Mendoza on 4/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import ComposeApp
import Foundation

@MainActor func startKoin() {
    let koinApp = doInitKoinIos()
    _koin = koinApp.koin
}

private var _koin: Koin_coreKoin?
var koin: Koin_coreKoin {
    _koin!
}
