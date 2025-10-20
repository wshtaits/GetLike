Pod::Spec.new do |s|
  s.name                     = 'PhoneNumberKitBridge'
  s.version                  = '1.0'
  s.summary                  = 'PhoneNumberKit bridge'
  s.homepage                 = 'https://getlike.space'
  s.license                  = ''
  s.author                   = ''
  s.source                   = { :path => '.'}
  s.static_framework         = true
  s.ios.deployment_target    = '16.0'
  s.swift_version            = '5.0'
  s.source_files             = 'PhoneNumberKitBridge/**/*'
  s.dependency 'PhoneNumberKit'
end
