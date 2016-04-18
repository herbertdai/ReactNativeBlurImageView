# ReactNativeBlurImageView

Navtive ImageView with FastBlur

See link [ReactNativeBlurImageViewSample](https://github.com/herbertdai/ReactNativeBlurImageViewSample)


------

# ReactNativeBlurImageViewSample
Sample project for React Native module: react-native-blur-image-view

See Android library: [ReactNativeBlurImageView](https://github.com/herbertdai/ReactNativeBlurImageView)


![App ScreenShot](https://cloud.githubusercontent.com/assets/880188/14610052/dc87711a-05be-11e6-95e5-bdfdb79b4451.png)


Set up project:

## 0 Setup npm
`$npm install react-native-blur-image-view`

## 1 Setup android


Add following config to **settings.gradle**:

    include ':react-native-blur-image-view'
    project(':react-native-blur-image-view').projectDir = new File(rootProject.projectDir.parent, 'node_modules/react-native-blur-image-view/android')


Add following code to **build.gradle**:

	dependencies {
   	  ...
   	  compile project(path: ':react-native-blur-image-view')
	}
	
	
## 2 Setup react native Js
    import BlurImageView from 'react-native-blur-image-view';

       ... 

       <BlurImageView style={{width:400, height:200}}
                       imageUrl={imageData}
          />
          
####TODO: Add sample and radius params.
