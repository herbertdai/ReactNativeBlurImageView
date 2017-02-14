var { requireNativeComponent, View } = require('react-native');

var { PropTypes } = require('react')

var iface = {
    name: 'BlurImageView',
    propTypes: {
        ...View.propTypes,
        radius: PropTypes.number,
        sampling: PropTypes.number,
        color: PropTypes.number,
        imageUrl: PropTypes.string,
        androidDrawable: PropTypes.string,
        snapshotViewId: PropTypes.string,
        scaleType: PropTypes.string
    },
};

module.exports = requireNativeComponent('RCTBlurImageView', iface, {nativeOnly: {onChange: true}});
