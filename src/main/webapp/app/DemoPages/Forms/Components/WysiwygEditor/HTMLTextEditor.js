import React, { useEffect, useState } from 'react'

import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { ContentState, convertToRaw, EditorState } from 'draft-js';
import { Editor } from "react-draft-wysiwyg";
import draftToHtml from 'draftjs-to-html';
import htmlToDraft from 'html-to-draftjs';
import { Link } from 'react-router-dom';

const HTMLTextEditor = ({ addHandler, defaultValue, ...props }) => {
    const [modal, setModal] = useState(false);
    const toggle = () => setModal(!modal);

    let [editorState, setEditorState] = useState(EditorState.createEmpty());

    useEffect(() => {
        if (defaultValue) {
            const contentBlock = htmlToDraft(defaultValue);
            if (contentBlock) {
                const contentState = ContentState.createFromBlockArray(contentBlock?.contentBlocks);
                setEditorState(EditorState.createWithContent(contentState));
            }
        }
    }, [defaultValue]);

    const closeBtn = (
        <button className="close" onClick={handleCancel}>
            &times;
        </button>
    );

    const handleCancel = () => {
        toggle();
    };

    const handleAdd = () => {
        addHandler(draftToHtml(convertToRaw(editorState.getCurrentContent())))
        toggle();
    };

    const onEditorStateChange = (editorState) => {
        setEditorState(editorState);
    };
    return (
        <>
            <Link color="primary mb-2" onClick={toggle} style={{ minWidth: '120px', marginLeft: "10px" }}>
                <i class="lnr-pencil btn-icon-wrapper" /> Edit
            </Link>

            <Modal isOpen={modal} toggle={toggle} size="lg">
                <ModalHeader toggle={toggle} onClick={toggle} close={closeBtn}>
                    Editor
                </ModalHeader>
                <ModalBody className="multiselect-modal-body">
                    <Editor
                        editorState={editorState}
                        wrapperClassName="demo-wrapper"
                        editorClassName="demo-editor"
                        onEditorStateChange={onEditorStateChange}
                    />
                    {/* <textarea disabled value={draftToHtml(convertToRaw(editorState.getCurrentContent()))} /> */}
                </ModalBody>
                <ModalFooter className="d-block">
                    <div className="d-flex">
                        <div>
                            <Button className="mr-2" outline onClick={handleCancel}> Cancel </Button>
                            <Button color="primary" onClick={handleAdd}> Add </Button>
                        </div>
                    </div>
                </ModalFooter>
            </Modal>
        </>
    );
}

export default HTMLTextEditor;