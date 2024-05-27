import 'package:flutter/material.dart';

class TextFieldDialog extends StatefulWidget {
  final Widget? title;
  final String? labelText, initialValue;

  const TextFieldDialog({
    super.key,
    this.title,
    this.labelText,
    this.initialValue,
  });

  @override
  State<StatefulWidget> createState() => _TextFieldDialogState();
}

class _TextFieldDialogState extends State<TextFieldDialog> {
  late final TextEditingController _controller;

  @override
  void initState() {
    super.initState();
    _controller = TextEditingController(text: widget.initialValue);
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: widget.title,
      content: SingleChildScrollView(
        child: TextFormField(
          controller: _controller,
          decoration: InputDecoration(
            labelText: widget.labelText,
          ),
        ),
      ),
      actions: <Widget>[
        Row(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('취소'),
            ),
            TextButton(
              onPressed: () => Navigator.pop(context, _controller.text.trim()),
              child: const Text('확인'),
            ),
          ],
        ),
      ],
    );
  }
}
