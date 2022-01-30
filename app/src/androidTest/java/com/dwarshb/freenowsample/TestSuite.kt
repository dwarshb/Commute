package com.dwarshb.freenowsample

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * It is suite which is used to run set of testcases file which includes multiple test cases.
 *
 * @author Darshan Bhanushali
 *
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    SplashActivityTest::class,
    MainActivityTest::class,
    MapsActivityTest::class
)
class TestSuite