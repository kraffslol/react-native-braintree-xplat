require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = package['name']
  s.version      = package['version']
  s.summary      = package['description']
  s.license      = package['license']

  s.authors      = package['author']
  s.homepage     = package['homepage']
  s.platform     = :ios, "9.0"

  s.source       = { :git => "https://github.com/troublediehard/react-native-braintree-xplat.git", :tag => "v#{s.version}" }
  s.source_files  = "ios/**/*.{h,m}"

  s.dependency 'React'
  s.dependency 'Braintree', '~> 4.9.4'
  s.dependency 'Braintree/Apple-Pay'
  s.dependency 'Braintree/3D-Secure'
  s.dependency 'Braintree/DataCollector'
  s.dependency 'Braintree/Venmo'
end
