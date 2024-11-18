def call(Map stageParams)
{
    gitCheckout(
            [
                $class: 'GitSCM',
                branches: [[name: stageParams.branch ]],
                userRemoteConfigs: [[url: stageParams.url]]
            ]
    )
}