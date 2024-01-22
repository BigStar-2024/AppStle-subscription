// tslint:disable
import * as React from 'react';

class YoutubeVideoPlayer extends React.Component<any, any> {
  getYoutubeVideoUrl = url => {
    var videoid = url.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
    if (videoid != null) {
      let youtubeUrl = 'https://www.youtube.com/embed/' + videoid[1];
      return youtubeUrl;
    } else {
      return '';
    }
  };

  render() {
    const { url } = this.props;
    return (
      <div>
        <iframe id="ytplayer" width="100%" src={this.getYoutubeVideoUrl(url)} frameBorder="0" allowFullScreen />
      </div>
    );
  }
}
export default YoutubeVideoPlayer;
