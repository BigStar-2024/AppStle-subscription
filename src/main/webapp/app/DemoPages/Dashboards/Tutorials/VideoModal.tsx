// tslint:disable
import * as React from 'react';
import { Modal } from 'reactstrap';
import YoutubeVideoPlayer from "app/DemoPages/Support/Tutorials/YoutubeVideoPlayer";
import './video-modal.scss';

class VideoModal extends React.Component<any, any> {
  render() {
    const { key, url, isOpen } = this.props;
    return (
      <Modal className="indexing-modal modal-dialog-centered" size="xl" isOpen={isOpen} toggle={this.props.toggle}>
        <div
          className="video"
          style={{
            position: "relative",
            paddingBottom: "56.25%" /* 16:9 */,
            paddingTop: 25,
            height: 0
          }}
        >
          <iframe
            style={{
              position: "absolute",
              top: 0,
              left: 0,
              width: "100%",
              height: "100%"
            }}
            src={'https://www.youtube.com/embed/Vlb6gdCR-bY'}
            frameBorder="0"
          />
        </div>
      </Modal>
    );
  }
}
export default VideoModal;
