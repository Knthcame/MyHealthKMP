//
//  iosAppTestsLaunchTests.swift
//  iosAppTests
//
//  Created by Kenneth Cartaña Mendoza on 28/11/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import XCTest

final class iosAppTestsLaunchTests: XCTestCase {

    override class var runsForEachTargetApplicationUIConfiguration: Bool {
        false
    }

    override func setUpWithError() throws {
        continueAfterFailure = false
    }

    @MainActor
    func testLaunch() throws {
        let app = XCUIApplication()
        app.launch()

        app.activate()
        assert(app.staticTexts["dashboardTopBarTitle"].firstMatch.exists)
        let attachment = XCTAttachment(screenshot: app.screenshot())
        attachment.name = "Dashboard Screen"
        attachment.lifetime = .keepAlways
        add(attachment)
        
        app.buttons["Diary"].firstMatch.tap()
        assert(app.staticTexts.matching(identifier: "Diary").element(boundBy: 0).exists)
        app.buttons["Settings"].firstMatch.tap()
        assert(app.staticTexts.matching(identifier: "Settings").element(boundBy: 0).exists)
    }
}
